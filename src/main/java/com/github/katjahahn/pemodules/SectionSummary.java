package com.github.katjahahn.pemodules;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.github.katjahahn.pemodules.sections.RSRCSection;
import com.github.katjahahn.pemodules.sections.SectionTable;

public class SectionSummary extends PEModule {

	private final byte[] filebytes;
	private RSRCSection rsrc;
	private final SectionTable table;
	private final Integer virtualRSRCAddress;

	public SectionSummary(byte[] filebytes, SectionTable table,
			Integer virtualRSRCAddress) {
		this.filebytes = filebytes;
		this.table = table;
		this.virtualRSRCAddress = virtualRSRCAddress;
	}

	@Override
	public String getInfo() {
//		String[] sections = { ".data", ".bss", ".text", ".idata" }; // TODO temporary -
//															// get sectionnames
//															// from table
//		StringBuilder b = new StringBuilder();
//		for (String section : sections) {
//			b.append(getSectionInfo(section));
//			b.append(NEWLINE + NEWLINE);
//		}
//		return b.toString() + NEWLINE + NEWLINE + getRSRCInfo();
		return "only .rsrc implemented by now!" + NL + NL + getRSRCInfo();
	}

	/**
	 * Returns a section dump as UTF-8 string
	 * 
	 * @param sectionName
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getSectionInfo(String sectionName) {
		try {
			long pointer = table.getPointerToRawData(sectionName);
			long sectionEndPoint = pointer + table.getSize(sectionName);
			byte[] sectionbytes = Arrays.copyOfRange(filebytes, (int) pointer,
					(int) sectionEndPoint);
			return sectionName + " section " + NL + "............."
					+ NL + NL + new String(sectionbytes, "UTF-8")
					+ NL + NL + "hex dump:" + NL
					+ convertByteToHex(sectionbytes);
		} catch (ArrayIndexOutOfBoundsException | IllegalArgumentException
				| UnsupportedEncodingException e) { // XXX
			return "error occured for section " + sectionName + ": "
					+ e.getMessage();
		}
	}

	private String getRSRCInfo() {
		if (virtualRSRCAddress != null) {
			long pointer = table.getPointerToRawData(".rsrc");
			byte[] rsrcbytes = Arrays.copyOfRange(filebytes, (int) pointer,
					filebytes.length);
			rsrc = new RSRCSection(rsrcbytes, virtualRSRCAddress);
			return ".rsrc section" + NL + "............." + NL
					+ NL + rsrc.getInfo();
		}
		return "no .rsrc section";
	}

}
