package com.appsflyerValidation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.extent.ExtentReporter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.metadata.ResponseInstance;
import com.opencsv.CSVReader;
import com.sun.mail.iap.ResponseInputStream;

import com.extent.ExtentReporter;

public class AppsFlyer extends ExtentReporter{

	

	static String sheet = "KeyValue";
	static String xlpath = null;
	static int rownumber;
	private static String value;
	private static String key;

	static String showIDUpdate = null;
	static String packIDUpdate = null;
	static String musicIDUpdate = null;
	
	public static Properties expectedData = new Properties();

	static ExtentReporter extent = new ExtentReporter();

	public void deleteOldAppsFlyerFiles() {
		File directory = new File(System.getProperty("user.dir") + File.separator + "AppsflyerReport");
		directory.mkdir();
		int fileCount = directory.list().length;
		System.out.println("File Count:" + fileCount);

		for (int i = 1; i <= fileCount; i++) {
			Path parentFolder = Paths.get(System.getProperty("user.dir") + File.separator + "AppsflyerReport");

			Optional<File> mostRecentFile = Arrays.stream(parentFolder.toFile().listFiles())
					.max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
			String fileName = null;
			if (mostRecentFile.isPresent()) {
				File mostRecent = mostRecentFile.get();
				fileName = mostRecent.getPath();
				System.out.println("most recent is " + mostRecent.getPath());
				// Delete file
				File myObj = new File(fileName);
				if (myObj.delete()) {
					System.out.println("Deleted the file: " + myObj.getName());
				} else {
					System.out.println("Failed to delete the file.");
				}
			} else {
				System.out.println("folder is empty!");
			}
		}
	}

	
	
	public void deleteOldAppsFlyerEventExcelFiles() {
		File directory = new File(System.getProperty("user.dir")+"//eventsReport");
		directory.mkdir();
		int fileCount = directory.list().length;
		System.out.println("File Count:" + fileCount);

		for (int i = 1; i <= fileCount; i++) {
			Path parentFolder = Paths.get(System.getProperty("user.dir") + "//eventsReport");

			Optional<File> mostRecentFile = Arrays.stream(parentFolder.toFile().listFiles())
					.max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
			String fileName = null;
			if (mostRecentFile.isPresent()) {
				File mostRecent = mostRecentFile.get();
				fileName = mostRecent.getPath();
				System.out.println("most recent is " + mostRecent.getPath());
				// Delete file
				File myObj = new File(fileName);
				if (myObj.delete()) {
					System.out.println("Deleted the file: " + myObj.getName());
				} else {
					System.out.println("Failed to delete the file.");
				}
			} else {
				System.out.println("folder is empty!");
			}
		}
	}

	
	
	public static String fetchTheDownloadedAppsFlyerReportName() {

		Path parentFolder = Paths.get(System.getProperty("user.dir") + "//AppsflyerReport");

		Optional<File> mostRecentFile = Arrays.stream(parentFolder.toFile().listFiles())
				.max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
		String filePath = null;
		String fileName = null;
		if (mostRecentFile.isPresent()) {
			File mostRecent = mostRecentFile.get();
			filePath = mostRecent.getPath();
			fileName = mostRecent.getName();
			System.out.println("most file recent is " + mostRecent.getName());

		} else {
			System.out.println("folder is empty!");
		}

		return fileName;
	}

	public static final char FILE_DELIMITER = ',';
	public static final String FILE_EXTN = ".xlsx";
	public static final String FILE_NAME = "EXCEL_DATA";

	public static String convertCsvToXlsxReport(String downloadedFileName) {
		SXSSFSheet sheet = null;
		CSVReader reader = null;
		Workbook workBook = null;
		String generatedXlsxReportFilePath = "";
		FileOutputStream fileOutputStream = null;

		try {

			/****
			 * Get the CSVReader Instance & Specify The Delimiter To Be Used
			 ****/
			String[] nextLine;
			reader = new CSVReader(new FileReader(
					System.getProperty("user.dir") +"//AppsflyerReport" + "\\" + downloadedFileName),
					FILE_DELIMITER);

			workBook = new SXSSFWorkbook();
			sheet = (SXSSFSheet) workBook.createSheet("Sheet");

			int rowNum = 0;
			// logger.info("Creating New .Xls File From The Already Generated
			// .Csv File");
			System.out.println("Creating New .Xls File From The Already Generated .Csv File");
			while ((nextLine = reader.readNext()) != null) {
				Row currentRow = sheet.createRow(rowNum++);
				for (int i = 0; i < nextLine.length; i++) {
					if (NumberUtils.isDigits(nextLine[i])) {
						currentRow.createCell(i).setCellValue(Integer.parseInt(nextLine[i]));
					} else if (NumberUtils.isNumber(nextLine[i])) {
						currentRow.createCell(i).setCellValue(Double.parseDouble(nextLine[i]));
					} else {
						currentRow.createCell(i).setCellValue(nextLine[i]);
					}
				}
			}

			File file = new File(System.getProperty("user.dir") +"\\AppsflyerConvertedReport");
				file.mkdir();
			generatedXlsxReportFilePath = System.getProperty("user.dir") +"\\AppsflyerConvertedReport"
					+ "\\" + FILE_NAME + FILE_EXTN;
			
			// logger.info("The File Is Generated At The Following Location?= "
			// + generatedXlsFilePath);
			System.out.println("The File Is Generated At The Following Location?= " + generatedXlsxReportFilePath);
			fileOutputStream = new FileOutputStream(generatedXlsxReportFilePath.trim());
			workBook.write(fileOutputStream);
		} catch (Exception exObj) {
			// logger.error("Exception In convertCsvToXls() Method?= " + exObj);
			System.out.println("Exception In convertCsvToXls() Method?=  " + exObj);
		} finally {
			try {

				/**** Closing The Excel Workbook Object ****/
				workBook.close();

				/**** Closing The File-Writer Object ****/
				fileOutputStream.close();

				/**** Closing The CSV File-ReaderObject ****/
				reader.close();
			} catch (IOException ioExObj) {
				System.out.println("Exception While Closing I/O Objects In convertCsvToXls() Method?=  " + ioExObj);
				// logger.error("Exception While Closing I/O Objects In
				// convertCsvToXls() Method?= " + ioExObj);
			}
		}

		return generatedXlsxReportFilePath;
	}

	public static void ExtractEventSpecificData(String userType, String idNumber,String Event) throws IOException, ParseException {

		// Event = "Tab_View";

		if (Event.contains("Tab_View")) {
			fetchEventValueFromAppsFlyerReport(Event, "Home");
			if(xlpath!=null){
				validate(userType,idNumber,Event, "Home");
			}else{
				System.out.println("event not present");
			}
			fetchEventValueFromAppsFlyerReport(Event, "Movies");
			if(xlpath!=null){
				validate(userType,idNumber,Event, "Movies");
			}else{
				System.out.println("event not present");
			}
			fetchEventValueFromAppsFlyerReport(Event, "TV Shows");
			if(xlpath!=null){
				validate(userType,idNumber,Event, "TV Shows");
			}else{
				System.out.println("event not present");
			}
			fetchEventValueFromAppsFlyerReport(Event, "Web Series");
			if(xlpath!=null){
				validate(userType,idNumber,Event, "Web Series");
			}else{
				System.out.println("event not present");
			}
			fetchEventValueFromAppsFlyerReport(Event, "News");
			if(xlpath!=null){
				validate(userType,idNumber,Event, "News");
			}else{
				System.out.println("event not present");
			}
			fetchEventValueFromAppsFlyerReport(Event, "Premium");
			if(xlpath!=null){
				validate(userType,idNumber,Event, "Premium");
			}else{
				System.out.println("event not present");
			}
			fetchEventValueFromAppsFlyerReport(Event, "Live TV");
			if(xlpath!=null){
				validate(userType,idNumber,Event, "Live TV");
			}else{
				System.out.println("event not present");
			}
			fetchEventValueFromAppsFlyerReport(Event, "Music");
			if(xlpath!=null){
				validate(userType,idNumber,Event, "Music");
			}else{
				System.out.println("event not present");
			}
			fetchEventValueFromAppsFlyerReport(Event, "Eduauraa");
			if(xlpath!=null){
				validate(userType,idNumber,Event, "Eduauraa");
			}else{
				System.out.println("event not present");
			}
		} else if(Event.contains("screen_view")){
			
				fetchEventValueFromAppsFlyerReport(Event, "UpcomingPage");
				if(xlpath!=null){
					validate(userType,idNumber,Event, "UpcomingPage");
				}else{
					System.out.println("event not present");
				}
				fetchEventValueFromAppsFlyerReport(Event, "DownloadPage");
				if(xlpath!=null){
					validate(userType,idNumber,Event, "DownloadPage");
				}else{
					System.out.println("event not present");
				}
				fetchEventValueFromAppsFlyerReport(Event, "More");
				if(xlpath!=null){
					validate(userType,idNumber,Event, "More");
				}else{
					System.out.println("event not present");
				}
				fetchEventValueFromAppsFlyerReport(Event, "Homepage");
				if(xlpath!=null){
					validate(userType,idNumber,Event, "Homepage");
				}else{
					System.out.println("event not present");
				}
		
			     }else{
		
					fetchEventValueFromAppsFlyerReport(Event, "");
					validate(userType,idNumber,Event, "");
				 }
	}

	public static void fetchEventValueFromAppsFlyerReport(String Event, String tab) throws IOException {
		String EventValueString = null;
		ArrayList<String> mpparameters = new ArrayList<String>();
		InputStream ExcelFileToRead = new FileInputStream(System.getProperty("user.dir")+ "//AppsflyerConvertedReport" + "\\" + "EXCEL_DATA" + ".xlsx");

		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row;
		XSSFCell cell;

		Iterator rows = sheet.rowIterator();

		int starRow = sheet.getFirstRowNum();
		System.out.println("Row start : " + starRow);
		int endRow = sheet.getLastRowNum();
		System.out.println("Row end : " + endRow);

		int i = 1;
		int j = 1;
		String tabValue = null;
		//String screenValue = null;
		int RowOfTheEvent = 0;
		xlpath = null;

		// for(int k=i ; k<=endRow ; k++){
		// XSSFCell EventName = sheet.getRow(i).getCell(4);
		// System.out.println("Event Name : "+EventName);
		//
		// if(EventName.getStringCellValue().contains(Event)){
		//
		// if(Event.equalsIgnoreCase("Tab_View")){
		// System.out.println("Tab_View EVENT Search");
		//
		// RowOfTheEvent = i;
		// XSSFCell EventValue = sheet.getRow(i).getCell(5);
		// System.out.println(EventValue);
		// EventValueString = EventValue.toString();
		// tabValue = tab;
		// String val= "\""+"Tab Name"+"\""+":"+"\""+tabValue+"\"";
		// System.out.println(val);
		//
		// if(EventValueString.contains(val)){
		// System.out.println(tabValue+" PRESENT");
		//
		// XSSFCell EventValue1 = sheet.getRow(i).getCell(5);
		// System.out.println(EventValue1);
		// EventValueString = EventValue1.toString();
		//
		// break;
		// }else{
		// System.out.println(tabValue+" NOT PRESENT");
		// }
		//
		// }else if(Event.equalsIgnoreCase("screen_view")){
		//
		// System.out.println("screen_view EVENT Search");
		//
		// RowOfTheEvent = i;
		// XSSFCell EventValue = sheet.getRow(i).getCell(5);
		// System.out.println(EventValue);
		// EventValueString = EventValue.toString();
		// screenValue = "UpcomingPage";
		// String val= "\""+"Page Name"+"\""+":"+"\""+screenValue+"\"";
		// System.out.println(val);
		//
		//
		// if(EventValueString.contains(val)){
		// System.out.println(screenValue+" PRESENT");
		//
		// XSSFCell EventValue1 = sheet.getRow(i).getCell(5);
		// System.out.println(EventValue1);
		// EventValueString = EventValue1.toString();
		//
		// break;
		// }else{
		// System.out.println(screenValue+" NOT PRESENT");
		// }
		//
		//
		// }else{
		// System.out.println("EVENT PRESENT");
		// RowOfTheEvent = i;
		// XSSFCell EventValue = sheet.getRow(i).getCell(5);
		// System.out.println(EventValue);
		// EventValueString = EventValue.toString();
		// break;
		// }
		//
		// }
		// i++;
		// }

		try {
			flow: while (rows.hasNext()) {
				XSSFCell EventName = sheet.getRow(i).getCell(4);
				System.out.println("Event Name : " + EventName);

				if (EventName.getStringCellValue().contains(Event)) {

					if (Event.equalsIgnoreCase("Tab_View")) {
						System.out.println("- - - - - - - Tab View - - - - - - -");
						System.out.println("Tab_View EVENT Search");

						RowOfTheEvent = i;
						XSSFCell EventValue = sheet.getRow(i).getCell(5);
						System.out.println(EventValue);
						EventValueString = EventValue.toString();
						tabValue = tab;
						String val = "\"" + "Tab Name" + "\"" + ":" + "\"" + tabValue + "\"";
						System.out.println(val);

						if (EventValueString.contains(val)) {
							System.out.println(tabValue + " PRESENT");

							XSSFCell EventValue1 = sheet.getRow(i).getCell(5);
							System.out.println(EventValue1);
							EventValueString = EventValue1.toString();

							break;
						} else {
							System.out.println(tabValue + " NOT PRESENT");
						}

					} else if (Event.equalsIgnoreCase("screen_view")) {
						System.out.println("- - - - - - - Screen View - - - - - - -");
						System.out.println("screen_view EVENT Search");

						RowOfTheEvent = i;
						XSSFCell EventValue = sheet.getRow(i).getCell(5);
						System.out.println(EventValue);
						EventValueString = EventValue.toString();
						tabValue = tab;
						String val = "\"" + "Page Name" + "\"" + ":" + "\"" + tabValue + "\"";
						System.out.println(val);

						if (EventValueString.contains(val)) {
							System.out.println(tabValue + " PRESENT");

							XSSFCell EventValue1 = sheet.getRow(i).getCell(5);
							System.out.println(EventValue1);
							EventValueString = EventValue1.toString();

							break;
						} else {
							System.out.println(tabValue + " NOT PRESENT");
						}

					}else if (Event.equalsIgnoreCase("Moresection_visitied")) {
						System.out.println("- - - - - - - Moresection Visited- - - - - - -");
						System.out.println("Moresection_Visited EVENT Search");

						RowOfTheEvent = i;
						XSSFCell EventValue = sheet.getRow(i).getCell(5);
						System.out.println(EventValue);
						EventValueString = EventValue.toString();
						//tabValue = tab;
						//String val = "\"" + "Page Name" + "\"" + ":" + "\"" + tabValue + "\"";
						//System.out.println(val);

						if (!EventValueString.equals("{}")) {
							//System.out.println(tabValue + " PRESENT");

							XSSFCell EventValue1 = sheet.getRow(i).getCell(5);
							System.out.println(EventValue1);
							EventValueString = EventValue1.toString();

							break;
						} else {
							System.out.println("Moresection_Visited NOT PRESENT");
						}

					}else if (Event.equalsIgnoreCase("TVShows_Content_Play")||Event.equalsIgnoreCase("af_svod_first_episode_free")||Event.equalsIgnoreCase("video_view_50_percent")||Event.equalsIgnoreCase("video_view_85_percent")||Event.equalsIgnoreCase("videos_viewed_is_20")) {
						System.out.println("- - - - - - - Consumption Event - - - - - - -");
						System.out.println("Consumption Event Search");

						RowOfTheEvent = i;
						XSSFCell EventValue = sheet.getRow(i).getCell(5);
						System.out.println(EventValue);
						EventValueString = EventValue.toString();

						String val ="show_id";
						System.out.println(val);
						String showID = null;						
						
						JsonObject jsonObject = new JsonParser().parse(EventValueString).getAsJsonObject();

						for(Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
						    String key = (String) iterator.next();
						    if(key.contains(val)){
						    	showID = jsonObject.get(key).toString();
						    }
						}

						showIDUpdate = showID.replace("\"","").toString();
						System.out.println(showIDUpdate);
						
						
						
						if (!EventValueString.equals("{}")) {
							//System.out.println(tabValue + " PRESENT");

							XSSFCell EventValue1 = sheet.getRow(i).getCell(5);
							System.out.println(EventValue1);
							EventValueString = EventValue1.toString();

							break;
						} else {
							System.out.println("Consumption Event NOT PRESENT");
						}

					} else if(Event.equalsIgnoreCase("af_add_to_cart")){
						System.out.println("- - - - - - - af_add_to_cart Event - - - - - - -");
						System.out.println("af_add_to_cart Event Search");

						RowOfTheEvent = i;
						XSSFCell EventValue = sheet.getRow(i).getCell(5);
						System.out.println(EventValue);
						EventValueString = EventValue.toString();

						String val ="pack_id";
						System.out.println(val);
						String packID = null;						
						
						JsonObject jsonObject = new JsonParser().parse(EventValueString).getAsJsonObject();

						for(Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
						    String key = (String) iterator.next();
						    if(key.contains(val)){
						    	packID = jsonObject.get(key).toString();
						    }
						}

						packIDUpdate = packID.replace("\"","").toString();
						System.out.println(packIDUpdate);
						
						
						
						if (!EventValueString.equals("{}")) {
							//System.out.println(tabValue + " PRESENT");

							XSSFCell EventValue1 = sheet.getRow(i).getCell(5);
							System.out.println(EventValue1);
							EventValueString = EventValue1.toString();

							break;
						} else {
							System.out.println("af_add_to_cart Event NOT PRESENT");
						}

					}else if (Event.equalsIgnoreCase("Videos_Content_Play")) {
						System.out.println("- - - - - - - Videos_Content_Play Event - - - - - - -");
						System.out.println("Videos_Content_Play Event Search");

						RowOfTheEvent = i;
						XSSFCell EventValue = sheet.getRow(i).getCell(5);
						System.out.println(EventValue);
						EventValueString = EventValue.toString();

						String val ="show_id";
						System.out.println(val);
						String showID = null;						
						
						JsonObject jsonObject = new JsonParser().parse(EventValueString).getAsJsonObject();

						for(Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
						    String key = (String) iterator.next();
						    if(key.contains(val)){
						    	showID = jsonObject.get(key).toString();
						    }
						}

						musicIDUpdate = showID.replace("\"","").toString();
						System.out.println(musicIDUpdate);
						
						
						
						if (!EventValueString.equals("{}")) {
							//System.out.println(tabValue + " PRESENT");

							XSSFCell EventValue1 = sheet.getRow(i).getCell(5);
							System.out.println(EventValue1);
							EventValueString = EventValue1.toString();

							break;
						} else {
							System.out.println("Videos_Content_Play Event NOT PRESENT");
						}

					} else {
						System.out.println("EVENT PRESENT");
						RowOfTheEvent = i;
						XSSFCell EventValue = sheet.getRow(i).getCell(5);
						System.out.println(EventValue);
						EventValueString = EventValue.toString();
						break;
					}

				}
				i++;
			}
//			if(!EventValueString.equals("{}")){
			// CODE TO CONVERT THE JSON VALUE TO KEY VALUE
			JsonObject obj = new JsonParser().parse(EventValueString).getAsJsonObject();

			obj.keySet().forEach(keyStr -> {
				Object keyvalue = obj.get(keyStr);
				System.out.println("key: " + keyStr + " value: " + keyvalue);
				mpparameters.add(keyStr.replace("\"", "").replace("$", "") + "keyvalue"
						+ keyvalue.toString().replace("\"", "").replace("$", "").replace(",", "-").trim());
			});

			System.out.println(mpparameters);

			// GET COLUMN VALUE
			String[] eventsColumnValue = { "Country Code", "WIFI", "Device Type", "OS Version" };

			for (int h = 0; h < eventsColumnValue.length; h++) {

				String columnVaue = eventsColumnValue[h];
				row = sheet.getRow(0);
				String cellValue;
				int EventPresentAtColumn = 0;
				for (int jk = 0; jk < 200; jk++) {
					cellValue = row.getCell(jk).getStringCellValue();

					if (cellValue.contains(columnVaue)) {
						// System.out.println("Column Present");
						// System.out.println("Getting Column value");
						EventPresentAtColumn = jk;
						// System.out.println("EventValue is present at Column
						// number : "+EventPresentAtColumn);
						break;
					}
				}
				// System.out.println("RowOfTheEvent : "+RowOfTheEvent);
				// System.out.println("EventPresentAtColumn :
				// "+EventPresentAtColumn);
				System.out.println(columnVaue + " : " + sheet.getRow(RowOfTheEvent).getCell(EventPresentAtColumn));

				mpparameters.add(columnVaue + "keyvalue" + sheet.getRow(RowOfTheEvent).getCell(EventPresentAtColumn));
			}

			System.out.println(mpparameters);

			parseResponse(mpparameters, Event, tabValue);
//			}
		} catch (Exception e) {
		
		}
		
	}

	public static void parseResponse(ArrayList<String> response, String fileName, String tab) {
		String file = fileName + tab;
		if (fileName.contains("Tab_View")) {
			creatExcel(file); // Create an excel file
			for (int i = 0; i < response.size(); i++) {
				write(i, response.get(i).split("keyvalue")[0], response.get(i).split("keyvalue")[1], file);
			}
		} else if (fileName.contains("screen_view")) {
			creatExcel(file); // Create an excel file
			for (int i = 0; i < response.size(); i++) {
				write(i, response.get(i).split("keyvalue")[0], response.get(i).split("keyvalue")[1], file);
			}
		} else {
			creatExcel(fileName); // Create an excel file
			for (int i = 0; i < response.size(); i++) {
				write(i, response.get(i).split("keyvalue")[0], response.get(i).split("keyvalue")[1], fileName);
			}
		}
	}

	/**
	 * Function to create excel file of format .xlsx Function to create sheet
	 */
	public static void creatExcel(String fileName) {
		try {
			xlpath = System.getProperty("user.dir") + File.separator + "eventsReport" + "\\" + fileName + ".xlsx";
			File file = new File(xlpath);
			if (!file.exists()) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				workbook.createSheet(sheet); // Create sheet
				FileOutputStream fos = new FileOutputStream(new File(xlpath));
				workbook.write(fos);
				workbook.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void write(int i, String key, String value, String fileName) {
		String xlpath = System.getProperty("user.dir") +"//eventsReport" + "\\" + fileName + ".xlsx";
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			FileOutputStream output = new FileOutputStream(xlpath);
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
			XSSFRow row = myExcelSheet.createRow(i);
			if (row == null) {
				row = myExcelSheet.createRow(i); // create row if not created
			}
			row.createCell(0).setCellValue(key); // write parameter key into
													// excel into first column
			row.createCell(1).setCellValue(value); // write parameter value into
													// excel second column
			myExcelBook.write(output);
			myExcelBook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void fillCellColor() {
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
			Row Cellrow = myExcelSheet.getRow(rownumber);
			XSSFCellStyle cellStyle = myExcelBook.createCellStyle();
			cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
			cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
			if (Cellrow.getCell(0) == null) {
				Cellrow.createCell(0);
			}
			Cell cell1 = Cellrow.getCell(0);
			cell1.setCellStyle(cellStyle);
			FileOutputStream fos = new FileOutputStream(new File(xlpath));
			myExcelBook.write(fos);
			fos.close();
		} catch (Exception e) {
		}
	}
	
	
	public static void fillCellColorGreen() {
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
			Row Cellrow = myExcelSheet.getRow(rownumber);
			XSSFCellStyle cellStyle = myExcelBook.createCellStyle();
			cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
			cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
			if (Cellrow.getCell(0) == null) {
				Cellrow.createCell(0);
			}
			Cell cell1 = Cellrow.getCell(0);
			cell1.setCellStyle(cellStyle);
			FileOutputStream fos = new FileOutputStream(new File(xlpath));
			myExcelBook.write(fos);
			fos.close();
		} catch (Exception e) {
		}
	}
	
	

	/**
	 * Get Row count
	 */
	// Generic method to return the number of rows in the sheet.
	public static int getRowCount() {
		int rc = 0;
		try {
			System.out.println(xlpath);
			FileInputStream fis = new FileInputStream(xlpath);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(sheet);
			rc = s.getLastRowNum();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rc;
	}

	public static void validation(String Event,String tab,String userType) {
		int NumberOfRows = getRowCount();
		System.out.println(NumberOfRows);
		System.out.println("xlPath : "+xlpath);

		String file = Event + tab;
		if(Event.contains("Tab_View")){
			extent.HeaderChildNode(file +" Parameter Validation");
		}else if(Event.contains("screen_view")){
			extent.HeaderChildNode(file +" Parameter Validation");
		}else {
			extent.HeaderChildNode(Event +" Parameter Validation");
		}
		
		
		
		
		if (NumberOfRows != 0) {
			for (rownumber = 0; rownumber <= NumberOfRows; rownumber++) {
				try {
					XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
					XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);

					key = myExcelSheet.getRow(rownumber).getCell(0).toString();
					value = myExcelSheet.getRow(rownumber).getCell(1).toString();

					if (value.trim().isEmpty()) {
						System.out.println("Paramter is empty :- Key:" + key + " - value" + value);
						extent.extentLoggerFail("Empty parameter",
								"Paramter is empty :- <b>Key : " + key + " \n value : " + value + "</b>");
						fillCellColor();
					} else if(key.contains("User Type")){
						
						String expectedValue = expectedData.getProperty(key);

						System.out.println("Key: " + key + " - value: " + value+" - Expected Data: "+expectedValue);
						if (expectedValue != null) {
								
							
							if(userType.contains("Guest")){
								if (expectedValue.toString().contains("Guest") || expectedValue.toString().contains("Free")) {
									fillCellColorGreen();
									extent.extentLoggerPass("Parameter", "Parameter : <b>Key : " + key + " <br/> Actual value : "
											+ value + "<br/>Expected Value : " + "Guest / Free" + "</b>");
							}else{
								fillCellColor();
								extent.extentLoggerFail("Parameter", "Parameter : <b>Key : " + key + " <br/> Actual value : "
										+ value + "<br/>Expected Value : " + expectedValue + "</b>");
								}
							}else if(userType.contains("NonSubscribedUser")){
								if (expectedValue.toString().contains("Registered")) {
									fillCellColorGreen();
									extent.extentLoggerPass("Parameter", "Parameter : <b>Key : " + key + " <br/> Actual value : "
											+ value + "<br/>Expected Value : " + "Registered" + "</b>");
									
								}else{
									fillCellColor();
									extent.extentLoggerFail("Parameter", "Parameter : <b>Key : " + key + " <br/> Actual value : "
											+ value + "<br/>Expected Value : " + expectedValue + "</b>");
								}
							}else if(userType.contains("SubscribedUser")){
								if (expectedValue.toString().contains("Premium") || expectedValue.toString().contains("Expired")) {
									fillCellColorGreen();
									extent.extentLoggerPass("Parameter", "Parameter : <b>Key : " + key + " <br/> Actual value : "
											+ value + "<br/>Expected Value : " + "Premium / Expired" + "</b>");
									
								}else{
									fillCellColor();
									extent.extentLoggerFail("Parameter", "Parameter : <b>Key : " + key + " <br/> Actual value : "
											+ value + "<br/>Expected Value : " + expectedValue + "</b>");
								}
							}
							
							
							if (!expectedValue.toString().equalsIgnoreCase(value.toString())) {
								fillCellColor();
								extent.extentLoggerFail("Parameter", "Parameter : <b>Key : " + key + " <br/> Actual value : "
										+ value + "<br/>Expected Value : " + expectedValue + "</b>");
							}else{
								fillCellColorGreen();
								extent.extentLoggerPass("Parameter", "Parameter : <b>Key : " + key + " <br/> Actual value : "
										+ value + "<br/>Expected Value : " + expectedValue + "</b>");
							}
						}
						
					} else if(key.contains("Current Subscription")){
						String expectedValue = expectedData.getProperty(key);

						System.out.println("Key: " + key + " - value: " + value+" - Expected Data: "+expectedValue);
						if (expectedValue != null) {
							if (expectedValue.toString().contains("N/A") || expectedValue.toString().contains("false")) {
								fillCellColorGreen();
								extent.extentLoggerPass("Parameter", "Parameter : <b>Key : " + key + " <br/> Actual value : "
										+ value + "<br/>Expected Value : " + "false / N/A" + "</b>");
								
							}else{
								fillCellColor();
								extent.extentLoggerFail("Parameter", "Parameter : <b>Key : " + key + " <br/> Actual value : "
										+ value + "<br/>Expected Value : " + expectedValue + "</b>");
							}
						}
					}else {
						String expectedValue = expectedData.getProperty(key);

						System.out.println("Key: " + key + " - value: " + value+" - Expected Data: "+expectedValue);
						if (expectedValue != null) {
														
							if (!expectedValue.toString().equalsIgnoreCase(value.toString())) {
								fillCellColor();
								extent.extentLoggerFail("Parameter", "Parameter : <b>Key : " + key + " <br/> Actual value : "
										+ value + "<br/>Expected Value : " + expectedValue + "</b>");
							}else{
								fillCellColorGreen();
								extent.extentLoggerPass("Parameter", "Parameter : <b>Key : " + key + " <br/> Actual value : "
										+ value + "<br/>Expected Value : " + expectedValue + "</b>");
							}
						}
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		} else {
			System.out.println("Event not triggered");
			extent.extentLoggerWarning("Event not triggered", "Event not triggered");
		}
		expectedData.clear();
	}

	
	
	public void extractFilesForValidationFunction(String userType, String idNumber,String event) throws IOException, ParseException{
		
		
		System.out.println("------------------------------------------------------");
		ExtractEventSpecificData(userType,idNumber,event);
		System.out.println("------------------------------------------------------");

	}
	
	
	public static void validate(String userType, String idNumber,String event,String tab) throws IOException, ParseException{
		String userValue = null;
		if(userType.equals("Guest")){
		//	userValue = "Free";
			userValue = "Guest";
		}else if(userType.equals("NonSubscribedUser")){
			userValue = "Registered";
		}else{
			userValue = "Premium";
		}
		String topnavtab = null;
		String tabname = null;
		if(event.equalsIgnoreCase("Tab_View")){
			if(xlpath.contains("Movies")){
				
				topnavtab = "movies";
				tabname = "Movies";
				
			}else if(xlpath.contains("TV Shows")){
				
				topnavtab = "tvshows";
				tabname = "TV Shows";
			}else if(xlpath.contains("Web Series")){
				
				topnavtab = "originals";
				tabname = "Web Series";
			}else if(xlpath.contains("News")){
				
				topnavtab = "news";
				tabname = "News";
			}else if(xlpath.contains("Premium")){
				
				topnavtab = "premium";
				tabname = "Premium";
			}else if(xlpath.contains("Live TV")){
				
				topnavtab = "livetv";
				tabname = "Live TV";
			}else if(xlpath.contains("Music")){
				
				topnavtab = "music";
				tabname = "Music";
			}else if(xlpath.contains("Eduauraa")){
				
				topnavtab = "kids";
				tabname = "Eduauraa";
			}
		}else if(event.equalsIgnoreCase("screen_view")){
			if(xlpath.contains("UpcomingPage")){
				
				topnavtab = "Homepage";
				tabname = "UpcomingPage";
			}else if(xlpath.contains("DownloadPage")){
				
				topnavtab = "UpcomingPage";
				tabname = "DownloadPage";
			}else if(xlpath.contains("More")){
				
				topnavtab = "DownloadPage";
				tabname = "More";
			}else if(xlpath.contains("Homepage")){
				
				topnavtab = "More";
				tabname = "Homepage";
			}
		}else if(event.equalsIgnoreCase("landing_on_subscriber_plans_screen")){
				topnavtab = "ConsumptionPage";
				tabname = "Subscription";
		}else if(event.equalsIgnoreCase("af_add_to_cart")){
				topnavtab = "ConsumptionPage";
				tabname = "Plan Selection";
		}
		
		
		
		ResponseInstance.fetchExpectedDataforAppsFlyer(event,userType,idNumber,userValue,topnavtab,tabname,showIDUpdate,packIDUpdate,musicIDUpdate);
		System.out.println("------------------------------------------------------");
		validation(event,tab,userType);
	}
	
	
}
