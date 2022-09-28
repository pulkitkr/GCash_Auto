package com.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.extent.ExtentReporter;

public class ExcelUpdate {

	public static String xlpath;
	public static String xlFileName;
	static String sheet = "Analysed_Reports"; 
	public static String UserType = "NA"; 
	public static String ModuleName = "NA";
	static int row = (getRowCount()+1);
	static int counter = 0;
	public static int passCounter = 0;
	public static int failCounter = 0;
	public static int warningCounter = 0;
	static String sheet1 = "Module Result";

	public static void creatExcel() { 
		try {
			String currentDate = getDate();
			xlpath = System.getProperty("user.dir") + "\\Analysed_Reports\\Analysed_Reports_"+currentDate+".xlsx";
			xlFileName = "Analysed_Reports_"+currentDate+".xlsx";
			File dir = new File(System.getProperty("user.dir")+"\\Analysed_Reports");
			if(!dir.isDirectory()) {
				dir.mkdir();
			}
			File file = new File(xlpath);
			if (!file.exists()) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				workbook.createSheet(sheet);
				FileOutputStream fos = new FileOutputStream(
						new File(xlpath));
				workbook.write(fos);
				workbook.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String name = dateFormat.format(date).toString().replaceFirst(" ", "_").replaceAll("/", "_").replaceAll(":","_");
		return name;
	}

	public static void writeData(String Validation, String result, String error) {
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			FileOutputStream output = new FileOutputStream(xlpath);
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
//			myExcelSheet
			XSSFRow xrow = myExcelSheet.getRow(row);
			if (xrow == null) {
				xrow = myExcelSheet.createRow(row);
			}
			Cell cell = null;
			if(counter == 0) {
				xrow = myExcelSheet.getRow(0);
				if (xrow == null) {
					xrow = myExcelSheet.createRow(0);
				}
				if (cell == null) {
					cell = xrow.createCell(0);
					cell.setCellValue("Module");
					cell = xrow.createCell(1);
					cell.setCellValue("Scenario");
					cell = xrow.createCell(2);
					cell.setCellValue("User Type");
					cell = xrow.createCell(3);
					cell.setCellValue("Validations");
					cell = xrow.createCell(4);
					cell.setCellValue("Results");
					cell = xrow.createCell(5);
					cell.setCellValue("Remarks");
					counter++;
				}
			}
			// Update the value of cell
			if (result.equals("Pass")) {
				if (cell == null) {
					cell = xrow.createCell(0);
					cell.setCellValue(ModuleName);
					cell = xrow.createCell(3);
					cell.setCellValue(Validation);
					cell = xrow.createCell(4);
					cell.setCellValue(result);
					cell = xrow.createCell(2);
					cell.setCellValue(UserType);
					row++;
					passCounter++;
				}
			} else if (result.equals("Fail")) {
				if (cell == null) {
					cell = xrow.createCell(0);
					cell.setCellValue(ModuleName);
					cell = xrow.createCell(4);
					cell.setCellValue(result);
					cell = xrow.createCell(5);
					cell.setCellValue(error);
					cell = xrow.createCell(2);
					cell.setCellValue(UserType);
					row++;
					failCounter++;
				}
			} else if (result.equals("Warning")) {
				if (cell == null) {
					cell = xrow.createCell(1);
					cell.setCellValue(ModuleName);
					cell = xrow.createCell(4);
					cell.setCellValue(result);
					cell = xrow.createCell(5);
					cell.setCellValue(error);
					cell = xrow.createCell(2);
					cell.setCellValue(UserType);
					row++;
					warningCounter++;
				}
			}
			myExcelBook.write(output);
			myExcelBook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void Node(String NodeName) {
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			FileOutputStream output = new FileOutputStream(xlpath);
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
//			myExcelSheet
			XSSFRow xrow = myExcelSheet.getRow(row);
			if (xrow == null) {
				xrow = myExcelSheet.createRow(row);
			}
			Cell cell = null;
			// Update the value of cell
			if (cell == null) {
				cell = xrow.createCell(1);
				cell.setCellValue(NodeName);
			}
			myExcelBook.write(output);
			myExcelBook.close();
		} catch (Exception e) {
		}
	}

	public int getMatchRow(String matchData) {
		String data = "";
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
			for (int i = 0; i < getRowCount(); i++) {
				data = myExcelSheet.getRow(i).getCell(0).toString();
				if (data.equals(matchData)) {
					return i;
				}
			}
		} catch (Exception e) {
		}
		return 0;
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

	public static String Iterator(String toFind) throws IOException {
//		String toFind = ID;
		XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
		XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
		for (Row row : myExcelSheet) {
			for (Cell cell : row) {
				CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
				DataFormatter formatter = new DataFormatter();
				String text = formatter.formatCellValue(cell);
				if (toFind.equals(text)) {
					return cellRef.formatAsString().replaceAll("\\D+", "");
				} else if (text.contains(toFind)) {
					System.out.println("Text found as part of " + cellRef.formatAsString());
				}
			}
		}
		return "";
	}

	public static void updateResult() {
		if (ExtentReporter.mailBodyPart.size() > 0) {
			for (int i = 0; i < ExtentReporter.mailBodyPart.size(); i++) {
				String result[] = ExtentReporter.mailBodyPart.get(i).toString().split(",");
//				System.out.println(result[0]+result[1]+result[2]);
				try {
					XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
					FileOutputStream output = new FileOutputStream(xlpath);
					XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet1);
					// Update the value of cell
					if (i == 0) {
						XSSFRow xrow = myExcelSheet.getRow(i);
						if (xrow == null) {
							xrow = myExcelSheet.createRow(i);
						}

						Cell cell = null;
						if (cell == null) {
							cell = xrow.createCell(1);
							cell.setCellValue("Module Name, APP verison - 20.21106.3");
							cell = xrow.createCell(2);
							cell.setCellValue("Module Result");
						}
					}
//						myExcelSheet
					XSSFRow xrow = myExcelSheet.getRow((i + 1));
					if (xrow == null) {
						xrow = myExcelSheet.createRow((i + 1));
					}

					Cell cell = null;
					if (cell == null) {
						cell = xrow.createCell(1);
						cell.setCellValue(result[0]);
						cell = xrow.createCell(2);
						if (failCounter == 0) {
							cell.setCellValue("Pass");
						} else {
							cell.setCellValue("Fail");
						}
					}
					myExcelBook.write(output);
					myExcelBook.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
//		for (int i = 1; i < 100; i++) {
//			System.out.println(getCellValue(i, 0));
//		}
//		System.out.println(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
//		writeData(2102,7,"Pass");
//		EnterResult("TC_1130", "Pass");
//		EnterResult("TC_1108");
//		EnterResult("TC_1028");
//		System.out.println(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
//		System.out.println((getRowCount()+1));
//		row = (getRowCount()+1);
//		writeData("ABC","Fail","Error");
		
	}

}
