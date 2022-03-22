package com.bcad.application.web.rest.util;

import com.bcad.application.config.PropertiesUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;



public class StyleUtil {

	PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");

	private static final String PRECISION = "#,##0";
	//private static final String PRECISION = "0.0000";
	private static final String PERC_PRECISION = "0.00%";
	
	public static CellStyle getStyleDataFormat(HSSFWorkbook workBook) {
		DataFormat df = workBook.createDataFormat();
		CellStyle csDF = workBook.createCellStyle();
		csDF.setDataFormat(df.getFormat(PRECISION));
		return csDF;
	}

	public static CellStyle getStyleBoldDataFormat(HSSFWorkbook workBook) {
		HSSFFont fFont = workBook.createFont();
		fFont.setBold(true);
		fFont.setFontHeightInPoints((short) 11);
		fFont.setFontName("Calibri");
		DataFormat df = workBook.createDataFormat();
		CellStyle csDFBold = workBook.createCellStyle();
		csDFBold.setDataFormat(df.getFormat(PRECISION));
		csDFBold.setFont(fFont);
		return csDFBold;
	}

	public static CellStyle getStylePercDataFormat(HSSFWorkbook workBook) {
		DataFormat dfPerc = workBook.createDataFormat();
		CellStyle csPerc = workBook.createCellStyle();
		csPerc.setDataFormat(dfPerc.getFormat(PERC_PRECISION));
		return csPerc;
	}
}
