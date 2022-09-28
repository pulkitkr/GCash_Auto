package com.utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUpdate {

	static String xlpath = "C:\\Users\\My pc\\Downloads\\Microsoft.SkypeApp_kzf8qxf38zg5c!App\\TC_ID\\TC_ID.xlsx";
	static String sheet = "TC_ID";

	public static void writeData(int row, int col, String data) {
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			FileOutputStream output = new FileOutputStream(xlpath);
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
			XSSFRow xrow = myExcelSheet.getRow(row);
			if (xrow == null) {
				xrow = myExcelSheet.createRow(row);
			}
			Cell cell = null;
			// Update the value of cell
			if (cell == null) {
				cell = xrow.createCell(col);
			}
			cell.setCellValue(data);
			myExcelBook.write(output);
			myExcelBook.close();
		} catch (Exception e) {
		}
	}

	// Generic method to return the column values in the sheet.
	public static String getCellValue(int row, int col) {
		String data = "";
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
			data = myExcelSheet.getRow(row).getCell(col).toString();
		} catch (Exception e) {
		}
		return data;
	}

	// Generic method to return the number of rows in the sheet.
	public static int getRowCount() {
		int rc = 0;
		try {
			FileInputStream fis = new FileInputStream(xlpath);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(sheet);
			rc = s.getLastRowNum();
		} catch (Exception e) {
		}
		return rc;
	}

	public static void main(String[] args) {
		System.out.println("Count : "+getRowCount());
//		for(int i=1;i<getRowCount();i++) {
//			System.out.println(getCellValue(i,0));
//		}
		Date date = new Date();
		System.out.println(date.getTime());
		writeData(2102,7,"Pass");
		Date date1 = new Date();
		System.out.println(date1.getTime());
	}

}
