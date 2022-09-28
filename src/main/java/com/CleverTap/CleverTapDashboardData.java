package com.CleverTap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;

import com.appsflyerValidation.AppsFlyer;
import com.extent.ExtentReporter;
import com.metadata.ResponseInstance;

import io.restassured.RestAssured;

public class CleverTapDashboardData {

	static String filePath = "";
	static String xlpath = "";
	static int LastRow = 0;
	public static Properties CTDashboardData = new Properties();
	public static ExtentReporter report = new ExtentReporter();
	public static ArrayList<String> ExpectedData = new ArrayList<>();
	public static Properties ActualCleverTapData = new Properties();

	/**
	 * Function to create Excel file
	 */
	public static void creatExcelCleverTap() {
		try {
			 filePath = System.getProperty("user.dir") + "\\CleverTap\\"+TodayDate();
			 xlpath = filePath+"\\SubscriptionPageViewed"+TodayDateTime()+".xlsx";
			File dir = new File(filePath);
			if (!dir.isDirectory()) {
				dir.mkdirs();
			}
			File file = new File(xlpath);
			if (!file.exists()) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				workbook.createSheet("SubscriptionPageViewed");
				FileOutputStream fos = new FileOutputStream(new File(xlpath));
				workbook.write(fos);
				workbook.close();
				XSSFWorkbook workbook1 = new XSSFWorkbook(new FileInputStream(xlpath));
				XSSFSheet sheet = workbook1.getSheetAt(0);
				XSSFRow xrow = sheet.getRow(0);
				if (xrow == null) {
					xrow = sheet.createRow(0);
				}
				Cell cell = null;
				if (cell == null) {
					cell = xrow.createCell(0);
					cell.setCellValue("Key");
					cell = xrow.createCell(1);
					cell.setCellValue("Value");
				}
				FileOutputStream outputStream = new FileOutputStream(xlpath);
				workbook1.write(outputStream);
				workbook1.close();
				outputStream.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static String TodayDateTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now);
		System.out.println("Current Date : " + currentDate);
		return currentDate;
	}
	
	public static String TodayDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now);
		System.out.println("Current Date : " + currentDate);
		return currentDate;
	}

	public static void InsertEventProperties(int row, String Key, String Value) {
		try {
			File file = new File(xlpath);
			if (file.exists()) {
				XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(xlpath));
				XSSFSheet sheet = workbook.getSheetAt(0);
				XSSFRow xrow = sheet.getRow(row);
				if (xrow == null) {
					xrow = sheet.createRow(row);
				}
				Cell cell = null;
				if (cell == null) {
					cell = xrow.createCell(0);
					cell.setCellValue(Key);
					cell = xrow.createCell(1);
					cell.setCellValue(Value);
				}
				FileOutputStream outputStream = new FileOutputStream(xlpath);
				workbook.write(outputStream);
				workbook.close();
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getRowCount() {
		int rc = 0;
		try {
			FileInputStream fis = new FileInputStream(xlpath);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheetAt(0);
			rc = s.getLastRowNum();
		} catch (Exception e) {
		}
		return rc;
	}

	public static String getCellValue(int row, int col) {
		String data = "";
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
			data = myExcelSheet.getRow(row).getCell(col).toString();
			myExcelBook.close();
		} catch (Exception e) {
		}
		return data;
	}
	
	
	public static void dashboardData(String EventName) {
		ListProperties(EventName);
		int row = getRowCount();
		System.out.println(row);
		for (int i = 1; i < row; i++) {
			XSSFWorkbook myExcelBook;
			try {
				myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
				XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
				String key = myExcelSheet.getRow(i).getCell(0).toString();
				String value = myExcelSheet.getRow(i).getCell(1).toString();
				System.out.println(key+"  :::::::  "+value);
				CTDashboardData.put(key,value);
				if(value == null) {
					report.extentLoggerFail("Null", "Property is empty :- <b>Key : " + key + " \n value : " + value + "</b>");
				}
				if(ExpectedData.contains(key)) {
					ExpectedData.remove(key);
				}
				myExcelBook.close();
			} catch (IOException e) {
			}
		}
		report.extentLogger("Missed Properties", "Missed Properties : "+ExpectedData);
	}
	
	public static void ListProperties(String EventName) {
		int row = getRowCount();
		for (int i = 1; i < row; i++) {
			XSSFWorkbook myExcelBook;
			try {
				myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
				XSSFSheet myExcelSheet = myExcelBook.getSheet(EventName);
				String key = myExcelSheet.getRow(i).getCell(0).toString();
				ExpectedData.add(key);
				myExcelBook.close();
			} catch (IOException e) {
			}
		}
	}

	public static void main(String[] args) throws IOException, ParseException {
//		creatExcelCleverTap();
		 xlpath = "E:\\Zee5_Project\\Zee5\\zee5_updated\\CleverTap\\08_07_2021\\CleverTap08_07_2021_13_31_03.xlsx";
//		dashboardData();
//		System.out.println(CTDashboardData);
		 String userType = "SubscribedUser";
//		 ResponseInstance.getLanguageforAppsFlyer(userType);

			// LOCATION
//		 ResponseInstance.getUserLocationforAppsFlyer();
		 ResponseInstance.SubcribedDetailsforCleverTap(userType);

			// DEVICE DETAILS
		 ResponseInstance.getDeviceDetails();
		System.out.println("Done");
		// SETTINGS
		 ResponseInstance.getUserSettingsDetailsforCleverTap(userType);
		// USERDATA
		 ResponseInstance.getUserDataforClevertap(userType);
	}

}
