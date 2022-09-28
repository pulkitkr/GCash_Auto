package com.CleverTap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class QOEMatrix {
	
//	static String xlpath = System.getProperty("user.dir") + "\\Performance\\performance.xlsx";
	static String xlpath = "C:\\Users\\IGS0026\\Documents\\Performance\\performance.xlsx";
	static int LastRow = 0;
	public static ArrayList<String> performanceResult = new ArrayList<>();
	public static boolean Count = false;
	
	public static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		Date date = new Date();
		String name = dateFormat.format(date).toString();
		return name;
	}
	
	
	public static void creatExcelPerformance() {
		try {
			File dir = new File(System.getProperty("user.dir") + "\\Performance");
			if (!dir.isDirectory()) {
				dir.mkdir();
			}
			File file = new File(xlpath);
			if (!file.exists()) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				workbook.createSheet("Performance");
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
					cell = xrow.createCell(1);
					cell.setCellValue("Scenario");
					cell = xrow.createCell(2);
					cell.setCellValue("Time Taken (Sec)");
					cell = xrow.createCell(3);
					cell.setCellValue("App Native Heap Memory");
					cell = xrow.createCell(4);
					cell.setCellValue("App Total Memory");
					cell = xrow.createCell(5);
					cell.setCellValue("CPU Usage");
					cell = xrow.createCell(6);
					cell.setCellValue("GPU Memory");
					cell = xrow.createCell(7);
					cell.setCellValue("GPU FPS");
					cell = xrow.createCell(8);
					cell.setCellValue("App Traffic usage");
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

	
	public static void InsertEventProperties(int row,String scenario,int Timetaken,double HeapMemo,double TotalMemo,double CPUUsage
			,double GPUUsage, double GPUFPS, int TrafficUsage){
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
						if(Count) {
							XSSFRow row1 = sheet.getRow(0);
							cell = row1.createCell(0);
							int c = getCellValue(0,0);
							cell.setCellValue((c+1));
							Count = false;
							if(c == 0) {
								cell = xrow.createCell(9);
								cell.setCellValue(getDate());
							}
						}
						cell = xrow.createCell(1);
						cell.setCellValue(scenario);
						cell = xrow.createCell(2);
						cell.setCellValue(Timetaken);
						cell = xrow.createCell(3);
						cell.setCellValue(HeapMemo);
						cell = xrow.createCell(4);
						cell.setCellValue(TotalMemo);
						cell = xrow.createCell(5);
						cell.setCellValue(CPUUsage);
						cell = xrow.createCell(6);
						cell.setCellValue(GPUUsage);
						cell = xrow.createCell(7);
						cell.setCellValue(GPUFPS);
						cell = xrow.createCell(8);
						cell.setCellValue(TrafficUsage);
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
	
	public static void AddFormulaToCell() throws IOException {
		FileInputStream inputStream = new FileInputStream(new File(xlpath));
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow xrow = sheet.getRow(73);
		if (xrow == null) {
			xrow = sheet.createRow(73);
		}
		Cell cell = null;
		if (cell == null) {
			cell = xrow.createCell(2);
			cell.setCellFormula("SUM(C2,C7,C12,C17,C22,C27,C32,C37,C42,C47)/10");
			cell = xrow.createCell(3);
			cell.setCellFormula("SUM(D2,D7,D12,D17,D22,D27,D32,D37,D42,D47)/10");
			cell = xrow.createCell(4);
			cell.setCellFormula("SUM(E2,E7,E12,E17,E22,E27,E32,E37,E42,E47)/10");
			cell = xrow.createCell(5);
			cell.setCellFormula("SUM(F2,F7,F12,F17,F22,F27,F32,F37,,F42,F47)/10*100");
			cell = xrow.createCell(6);
			cell.setCellFormula("SUM(G2,G7,G12,G17,G22,G27,G32,G37,,G42,G47)/10");
			cell = xrow.createCell(7);
			cell.setCellFormula("SUM(H2,H7,H12,H17,H22,H27,H32,H37,,H42,H47)/10");
			cell = xrow.createCell(8);
			cell.setCellFormula("SUM(I6,I11,I16,I21,I26,I31,I36,I41,,I46,I51)/10");
		}
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		evaluator.evaluateAll();
		inputStream.close();
		FileOutputStream outputStream = new FileOutputStream(xlpath);
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
	
	static ArrayList<String> performaceDetails = new ArrayList<String>();

	public static void insertToExcel() {
		LastRow = getRowCount();
		if (performaceDetails.size() > 0) {
			for (int i = 0; i < performaceDetails.size(); i++) {
				int row = getRowCount();
				String result[] = performaceDetails.get(i).toString().split(",");
				InsertEventProperties((row+1), result[0], Integer.valueOf(result[1]), Integer.valueOf(result[2]), 
						Integer.valueOf(result[3]), Integer.valueOf(result[4]), Integer.valueOf(result[5]), Double.parseDouble(result[6]),
								Integer.valueOf(result[7]));
			}
		} else {
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
	
	public static int getCellValue(int row, int col) {
		int data = 0;
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
			data = (int)myExcelSheet.getRow(row).getCell(col).getNumericCellValue();
			myExcelBook.close();
		} catch (Exception e) {
			return data;
		}
		return data;
	}
	
	public static void main(String[] args) throws IOException {
//		creatExcelPerformance();
//		insertToExcel();
//		System.out.println(VerifyIteration());
//		System.out.println("Done");
//		String Token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOjE2MjU1NDc1MTksImV4cCI6MTYzNTkxNTUxOSwiaXNzIjoiaHR0cHM6Ly91c2VyYXBpLnplZTUuY29tIiwiYXVkIjpbImh0dHBzOi8vdXNlcmFwaS56ZWU1LmNvbS9yZXNvdXJjZXMiLCJzdWJzY3JpcHRpb25hcGkiLCJ1c2VyYXBpIl0sImNsaWVudF9pZCI6InJlZnJlc2hfdG9rZW5fY2xpZW50Iiwic3ViIjoiNDcxZTEwYmUtNTYyOS00YzkwLThkMDAtZjA0ZjFjMWUwNzExIiwiYXV0aF90aW1lIjoxNjI1NTQ3NTE5LCJpZHAiOiJsb2NhbCIsInVzZXJfaWQiOiI0NzFlMTBiZS01NjI5LTRjOTAtOGQwMC1mMDRmMWMxZTA3MTEiLCJzeXN0ZW0iOiJaNSIsImFjdGl2YXRpb25fZGF0ZSI6IjIwMjAtMDMtMTlUMDY6MDk6NDYiLCJjcmVhdGVkX2RhdGUiOiIyMDIwLTAzLTE5VDA2OjA5OjQ2IiwicmVnaXN0cmF0aW9uX2NvdW50cnkiOiJJTiIsInVzZXJfZW1haWwiOiJ6ZWU1bGF0ZXN0QGdtYWlsLmNvbSIsInVzZXJfbW9iaWxlIjoiOTE4NjY5MDA1NTg0Iiwic3Vic2NyaXB0aW9ucyI6IltdIiwic2NvcGUiOlsic3Vic2NyaXB0aW9uYXBpIiwidXNlcmFwaSIsIm9mZmxpbmVfYWNjZXNzIl0sImFtciI6WyJkZWxlZ2F0aW9uIl19.YNhsIcVG2G_T4k_mYL-Muacxwz1oYDQoix3i0zMIoyHUcyLkrgq-bUnpJvh0ao3jq7e4JE8RfOcAqBjqR1t2t4N-3HH714rPAzVMIQ-dSUNBSjvIShiKEKxzdYn0wO5eZs38HFP9R9kYZ5BAwx3Hfg6XJAWFTq6X3RG0MsZgMdeOxZTn0P0buhdaUxj3SmWKELkTsVocm4qz44IhaHEIbv_m9eKzfuCBc_o1IazJin-lVp-lsI433cjk3hAjVw57ff8tww0FRwiTvTx17sz_m5Bmg1i3ps2CxdowSRUofDSomxwwYDrQiPOAsFm9mEI_YewKKuC6thU8N8TIQm58Rw";
//		decodeTokenParts(Token);
		calculateReadings(1,"App Launch");
		calculateReadings(2,"Login");
		calculateReadings(3,"Navigation to Premium");
		calculateReadings(4,"Initiate Content playback");
		calculateReadings(5,"DeepLink to Consumption screen");
		calculateReadings(6,"DeepLink to SubscriptionScreen screen");
		calculateReadings(7,"Initiate content playback (Episode)");
		calculateReadings(8,"ConsumpitonScreen for Shows");
		calculateReadings(9,"ConsumpitonScreen for Movies");
		
		System.out.println(performanceResult);
	}
	
	public static void calculateaverage() {
		try {
		calculateReadings(1,"App Launch");
		calculateReadings(2,"Login");
		calculateReadings(3,"Navigation to Premium");
		calculateReadings(4,"Initiate Content playback");
		calculateReadings(5,"DeepLink to Consumption screen");
		calculateReadings(6,"DeepLink to SubscriptionScreen screen");
		calculateReadings(7,"Initiate content playback (Episode)");
		calculateReadings(8,"ConsumpitonScreen for Shows");
		calculateReadings(9,"ConsumpitonScreen for Movies");
		System.out.println(performanceResult);
		}catch(Exception e) {
		}
	}
	
	public static void decodeTokenParts(String token)
	{
	    String[] parts = token.split("\\.", 0);

//	    for (String part : parts) {
	        byte[] bytes = Base64.getUrlDecoder().decode(parts[1]);
	        String decodedString = new String(bytes, StandardCharsets.UTF_8);
	        System.out.println("Decoded: " + decodedString);
//	    }
	}
	
	public static void clearOldData() {
		 File file = new File(xlpath);
	        file.delete();
	}
	
	public static boolean VerifyIteration() {
		int data = 0;
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
			data = (int)myExcelSheet.getRow(0).getCell(0).getNumericCellValue();
			myExcelBook.close();
			if(data != 10) {
				return true;
			}else {
				AddFormulaToCell();
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	public static void calculateReadings(int resultRow,String compareScenario) throws FileNotFoundException, IOException {
		int row = getRowCount();
		int counter = 0 ;
		NumberFormat nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
		XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
		XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
		double TimeTakenSec = 0, AppNativeHeapMemory = 0,AppTotalMemory = 0,CPUUsage= 0 ,GPUMemory = 0,GPUFPS = 0,AppTrafficusage = 0;
		for (int i = 1; i < row; i++) {
			String scenario = myExcelSheet.getRow(i).getCell(1).toString();
			if(scenario.contains(compareScenario)) {
				TimeTakenSec  = TimeTakenSec + (int)myExcelSheet.getRow(i).getCell(2).getNumericCellValue();
			 AppNativeHeapMemory = AppNativeHeapMemory + (int)myExcelSheet.getRow(i).getCell(3).getNumericCellValue();	
			 AppTotalMemory	= AppTotalMemory  +(int)myExcelSheet.getRow(i).getCell(4).getNumericCellValue();
			 CPUUsage	= CPUUsage + (int)myExcelSheet.getRow(i).getCell(5).getNumericCellValue();
			 GPUMemory	= GPUMemory + (int)myExcelSheet.getRow(i).getCell(6).getNumericCellValue();
			 GPUFPS	= GPUFPS + (int)myExcelSheet.getRow(i).getCell(7).getNumericCellValue();
			 AppTrafficusage = AppTrafficusage+(int)myExcelSheet.getRow(i).getCell(8).getNumericCellValue();
			 counter++;
			}
		}
		FinalResult(resultRow,compareScenario,TimeTakenSec/counter,AppNativeHeapMemory/counter,AppTotalMemory/counter,CPUUsage/counter,GPUMemory/counter,GPUFPS/counter,AppTrafficusage/counter);
		performanceResult.add(compareScenario+","+nf.format(TimeTakenSec/counter)+","+nf.format(AppNativeHeapMemory/counter)+","+nf.format(AppTotalMemory/counter)
				+","+nf.format(CPUUsage/counter)+","+nf.format(GPUMemory/counter)+","+nf.format(GPUFPS/counter)+","+nf.format(AppTrafficusage/counter));
	}
	
	
	public static void FinalResult(int row,String scenario,double Timetaken,double HeapMemo,double TotalMemo,double CPUUsage
			,double GPUUsage, double GPUFPS, double TrafficUsage){
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
						cell = xrow.createCell(11);
						cell.setCellValue(scenario);
						cell = xrow.createCell(12);
						cell.setCellValue(Timetaken);
						cell = xrow.createCell(13);
						cell.setCellValue(HeapMemo);
						cell = xrow.createCell(14);
						cell.setCellValue(TotalMemo);
						cell = xrow.createCell(15);
						cell.setCellValue(CPUUsage);
						cell = xrow.createCell(16);
						cell.setCellValue(GPUUsage);
						cell = xrow.createCell(17);
						cell.setCellValue(GPUFPS);
						cell = xrow.createCell(18);
						cell.setCellValue(TrafficUsage);
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
}
