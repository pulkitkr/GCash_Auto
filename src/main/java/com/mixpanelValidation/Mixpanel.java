package com.mixpanelValidation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;
import com.aventstack.extentreports.Status;
import com.extent.ExtentReporter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.jayway.restassured.RestAssured;
//import com.jayway.restassured.config.EncoderConfig;
//import com.jayway.restassured.response.Response;
import com.metadata.ResponseInstance;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.response.Response;
import com.propertyfilereader.PropertyFileReader;

public class Mixpanel extends ExtentReporter {

	/**
	 * Global variables
	 */
	static String sheet = "Sheet1";
	static String fileName = "Excel";// ReportName;
	static String xlpath;
	static String booleanParameters = "";
	static String integerParameters = "";
	static int rownumber;
	protected static Response resp = null;
	public static String DOB;
	public static Properties FEProp = new Properties();
	private static String value;
	private static String key;
	static ExtentReporter extent = new ExtentReporter();
	static String UserID = "Unique ID";
	static String UserType = "guest";
	static String APIKey;
	static String Modelname;
	static String propValue = "Empty";
	public static boolean fetchUserdata = false;
	public static String DistinctId;
	static PropertyFileReader Prop;
	public static boolean SubcribedDetails = false;
	public static boolean Language = true;
	public static boolean parentControl = false;

	public static void ValidateParameter(String distinctID, String eventName)
			throws JsonParseException, JsonMappingException, IOException, InterruptedException, ParseException {
		System.out.println("Parameter Validation " + distinctID);
		Prop = new PropertyFileReader("properties/MixpanelKeys.properties");
		booleanParameters = Prop.getproperty("Boolean");
		integerParameters = Prop.getproperty("Integer");
		fileName = ReportName;
		xlpath = System.getProperty("user.dir") + "\\" + fileName + ".xlsx";
		StaticValues(distinctID);
		getParameterValue();
		fetchEvent(distinctID, eventName);
		SubcribedDetails = false;
	}

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {

		System.out.println(System.getProperty("os.name"));
	}

	@SuppressWarnings("unused")
	private static void getDOB(String s) {
		LocalDate dob = LocalDate.parse(s);
		LocalDate curDate = LocalDate.now();
		System.out.println(String.valueOf(Period.between(dob, curDate).getYears()));
	}

	/**
	 * Function to fetch logs from mixpanel dash board using rest assured API
	 * 
	 * @param distinct_id
	 * @param eventName
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public static void fetchEvent(String distinct_id, String eventName)
			throws JsonParseException, JsonMappingException, IOException {
		try {
			Thread.sleep(180000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now); // Get current date in formate yyyy-MM-dd
		System.out.println("Current Date : " + currentDate);
		
		if (platform.equals("Android")) {
			APIKey = "b2514b42878a7e7769945befa7857ef1";
			UserID = "$model";
			distinct_id = modelName();
		} else if (platform.equalsIgnoreCase("Web") || platform.equalsIgnoreCase("MPWA")){
			APIKey = "58baafb02e6e8ce03d9e8adb9d3534a6";
			if (distinct_id.contains("-")) {
				UserID = "Unique ID";
				UserType = "Login";
			}
		}else if(platform.equals("TV")) {
			APIKey = "e45c2466330383c493ba355fd0819bf4";
			UserID = "$model";
			distinct_id = modelName();
		} 
	
		Response request=null;
		for(int trial=0;trial<5;trial++) {
			request = RestAssured.given().auth().preemptive().basic(APIKey, "")
					.config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()))
					.contentType("application/x-www-form-urlencoded; charset=UTF-8").formParam("from_date", currentDate)
					.formParam("to_date", currentDate).formParam("event", "[\"" + eventName + "\"]")
					.formParam("where", "properties[\"" + UserID + "\"]==\"" + distinct_id + "\"")
					.post("https://data.mixpanel.com/api/2.0/export/");
			if(request.equals(null) || request.equals("")) extent.extentLogger("", "Failed to fetch MP Response");
			else break;
		}
		
		System.out.println("Response : "+request.asString());
		sheet = eventName.trim().replace(" ", "").replace("/", "");
		if (request.toString() != null) {
			if (platform.equals("Android")) {
				parseResponse(getLatestEvent(request));
			} else {
				String response = request.asString();
				String s[] = response.split("\n");
//				String time = checkLatestEvent(s[s.length - 1]);
//				if(time == null) {
					parseResponse(s[s.length - 1]);
//				}else {
//					System.out.println("Event not triggered");
//					extentReportFail("Event not triggered", "Event not triggered");
//					return;
//				}
			}
			validation();
		}else {
			System.out.println("Event not triggered");
			extentReportFail("Event not triggered", "Event not triggered");
		}
	}

	public static String checkLatestEvent(String s) {
		String empty = null;
		String commaSplit[] = s.replace("\"properties\":{", "").replace("}", "").replaceAll("[.,](?=[^\\[]*\\])", "-")
				.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
		String com[] = commaSplit[1].split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
		int MPTime = Integer.valueOf(com[1].replace("\"", "").replace("$", ""));
		long currentTime = Instant.now().getEpochSecond();
		long subCurrentTime = (currentTime - 360);
		if (subCurrentTime <= MPTime) {
			return s;
		}
		return empty;
	}
	
	
	/**
	 * Parse the response and split the response
	 * 
	 * @param reponse
	 */
	public static void parseResponse(String response) {
		String commaSplit[] = response.replace("\"properties\":{", "").replace("}", "")
				.replaceAll("[.,](?=[^\\[]*\\])", "-").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
		creatExcel(); // Create an excel file
		for (int i = 0; i < commaSplit.length; i++) {
			if (i != 0) {
				String com[] = commaSplit[i].split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
				/** Write key value into excel */
				write(i, com[0].replace("\"", "").replace("$", ""), com[1].replace("\"", "").replace("$", ""));
			}
		}
	}

	@SuppressWarnings("unused")
	private static boolean validateEventTriggerTime(String time) {
		int eventTime = Integer.valueOf(time);
		int elapseTime = (eventTime + 360);
		System.out.println(eventTime + "  " + elapseTime);
		if (eventTime < elapseTime) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Function to create excel file of format .xlsx Function to create sheet
	 */
	public static void creatExcel() {
		try {
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

	/**
	 * Function to write values into excel
	 * 
	 * @param i
	 * @param parameter
	 */
	public static void write(int i, String key, String value) {
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			FileOutputStream output = new FileOutputStream(xlpath);
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
			XSSFRow row = myExcelSheet.createRow(i);
			if (row == null) {
				row = myExcelSheet.createRow(i); // create row if not created
			}
			row.createCell(0).setCellValue(key); // write parameter key into excel into first column
			row.createCell(1).setCellValue(value); // write parameter value into excel second column
			myExcelBook.write(output);
			myExcelBook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Validation
	 */
	public static void validation() {
		int NumberOfRows = getRowCount();
		System.out.println(NumberOfRows);
		extent.HeaderChildNode("Parameter Validation");
		if(NumberOfRows != 0) {
		for (rownumber = 1; rownumber < NumberOfRows; rownumber++) {
			try {
				XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
				XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
				value = myExcelSheet.getRow(rownumber).getCell(1).toString();
				key = myExcelSheet.getRow(rownumber).getCell(0).toString();
				if (value.trim().isEmpty()) {
					System.out.println("Paramter is empty :- Key:" + key + " - value" + value);
					extentReportFail("Empty parameter",
							"Paramter is empty :- <b>Key : " + key + " \n value : " + value + "</b>");
					fillCellColor();
				} else {
					if (isContain(booleanParameters, key)) {
						validateBoolean(value);
					} else if (isContain(integerParameters, key)) {
						validateInteger(value);
					}else if (isContain(integerParameters, key)) {
						validateFloat(value);
					}
					validateParameterValue(key, value);
					extentInfo();
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		}else {
			System.out.println("Event not triggered");
			extentReportFail("Event not triggered", "Event not triggered");
		}
		FEProp.clear();
	}

	public static void validateParameterValue(String key, String value) {
		try {
			propValue = FEProp.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (propValue != null) {
			if (propValue.equals("[]")) {
				propValue = "N/A";
			}
			if (!propValue.replaceAll("\\s", "").equalsIgnoreCase(value.replaceAll("\\s", ""))) {
				fillCellColor();
				extentReportFail("Parameter", "Parameter : <b>Key : " + key + " <br/> value : " + value + "<br/>Expected Value : "+propValue+"</b>");
			}
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

	private static void validateInteger(String value) {
		if (!value.equals("N/A")) {
			Pattern p = Pattern.compile("[0-9]+");
			Matcher m = p.matcher(value);
			if (!m.matches()) {
				fillCellColor();
				extentReportFail("Empty parameter",
						"Value is not a Integer Data-Type :- <b>Key : " + key + " <br/> value : " + value + "</b>");
			}
		}
	}

	private static void validateFloat(String value) {
		if (!value.equals("N/A")) {
//			Pattern p = Pattern.compile("[0-9]+");
//			Matcher m = p.matcher(value);
//			if (!m.matches()) {
//				fillCellColor();
//				extentReportFail("Empty parameter",
//						"Value is not a Integer Data-Type :- <b>Key : " + key + " <br/> value : " + value + "</b>");
//			}
		}
	}
	
	private static boolean isContain(String source, String subItem) {
		String pattern = "\\b" + subItem + "\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		return m.find();
	}

	private static void validateBoolean(String value) {
		if (!value.equals("N/A")) {
			if (!Stream.of("true", "false", "TRUE", "FALSE").anyMatch(value::equals)) {
				fillCellColor();
				extentReportFail("Empty parameter",
						"Value is not a boolean Data-Type :- <b>Key : " + key + " <br/> value : " + value + "</b>");
			}
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

	public static void StaticValues(String UniqueID) throws ParseException {
		platform = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getSuite().getName();
		if (platform.equals("Mpwa")) {
			FEProp.setProperty("Platform Name", "Web");
			FEProp.setProperty("os", "Android");
		} else if (platform.equals("Android")) {
			FEProp.setProperty("Platform Name", platform);
			FEProp.setProperty("os", "Android");
		} else if (platform.equals("Web")) {
			FEProp.setProperty("Platform Name", platform);
			FEProp.setProperty("os", System.getProperty("os.name").split(" ")[0]);
		}
		Mixpanel.FEProp.setProperty("Landing Page Name", "home");
		if(!platform.equals("Android")) {
		Mixpanel.FEProp.setProperty("Unique ID", UniqueID);
		}
		
		userType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
		
		if(userType.equals("Guest")) {
			if(SubcribedDetails == false) {
			Mixpanel.FEProp.setProperty("Gender", "N/A");
			Mixpanel.FEProp.setProperty("Age", "N/A");
			Mixpanel.FEProp.setProperty("Free Trial Expiry Date", "N/A");
			Mixpanel.FEProp.setProperty("Free Trial Package", "N/A");
			Mixpanel.FEProp.setProperty("Latest Subscription Pack", "N/A");
			Mixpanel.FEProp.setProperty("Latest Subscription Pack Expiry", "N/A");
			Mixpanel.FEProp.setProperty("Next Expiring Pack", "N/A");
			Mixpanel.FEProp.setProperty("Next Pack Expiry Date", "N/A");
			Mixpanel.FEProp.setProperty("Pack Duration", "N/A");
			Mixpanel.FEProp.setProperty("Parent Control Setting", "N/A");
		    Mixpanel.FEProp.setProperty("User Type", "Free");
			Mixpanel.FEProp.setProperty("Partner Name", "N/A");
			Mixpanel.FEProp.setProperty("HasRental", "false");
			Mixpanel.FEProp.setProperty("hasEduauraa", "false");
			if(Language != false) {
			Mixpanel.FEProp.setProperty("New App Language", "en");
			if(platform.equals("Android")) {
				Mixpanel.FEProp.setProperty("New Content Language", "en,kn");
			}else {
			Mixpanel.FEProp.setProperty("New Content Language", "[en-kn]");}
			}
			}
		}else if(userType.equals("NonSubscribedUser"))
		{
			NonSubcribedDetails();
		}else if(userType.equals("SubscribedUser")) {
			SubcribedDetails();
		}
	}

	
	public static void NonSubcribedDetails() {
		Prop = new PropertyFileReader("properties/MixpanelKeys.properties");
		Mixpanel.FEProp.setProperty("Free Trial Expiry Date", Prop.getproperty("NonSub_Free_Trial_Expiry_Date"));
		Mixpanel.FEProp.setProperty("Free Trial Package", Prop.getproperty("NonSub_Free_Trial_Package"));
		Mixpanel.FEProp.setProperty("Latest Subscription Pack", Prop.getproperty("NonSub_Latest_Subscription_Pack"));
		Mixpanel.FEProp.setProperty("Latest Subscription Pack Expiry", Prop.getproperty("NonSub_Latest_Subscription_Pack_Expiry"));
		Mixpanel.FEProp.setProperty("Next Expiring Pack", Prop.getproperty("NonSub_Next_Expiring_Pack"));
		Mixpanel.FEProp.setProperty("Next Pack Expiry Date", Prop.getproperty("NonSub_Next_Pack_Expiry_Date"));
		Mixpanel.FEProp.setProperty("Pack Duration", Prop.getproperty("NonSub_Pack_Duration"));
		Mixpanel.FEProp.setProperty("HasRental", Prop.getproperty("NonSub_HasRental"));
		Mixpanel.FEProp.setProperty("hasEduauraa", "false");
		SubcribedDetails = true;
	}
	
	public static void SubcribedDetails() throws ParseException {	
		String UserType;
		UserType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
		String username = null,password = null;
		if(UserType.equals("SubscribedUser")) {
			username = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedUserName");
			password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedUserPassword");
		}
//		username = "zeein7@mailnesia.com";
//		password = "123456";
		Response subscriptionResp=ResponseInstance.getSubscriptionDetails(username, password);
		subscriptionResp.print();
		int subscriptionItems=subscriptionResp.jsonPath().get("subscription_plan.size()");		
		String id=subscriptionResp.jsonPath().get("subscription_plan["+(subscriptionItems-1)+"].id").toString();
		String subscription_plan_type=subscriptionResp.jsonPath().get("subscription_plan["+(subscriptionItems-1)+"].subscription_plan_type").toString();
		String title=subscriptionResp.jsonPath().get("subscription_plan["+(subscriptionItems-1)+"].title").toString();
		String Latest_Subscription_Pack=id+"_"+title+"_"+subscription_plan_type;
		String packExpiry=subscriptionResp.jsonPath().get("subscription_end["+(subscriptionItems-1)+"]").toString();
		SimpleDateFormat requiredFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		java.text.DateFormat actualFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		actualFormat.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
		java.util.Date packExpiryDate = actualFormat.parse(packExpiry);
		String Latest_Subscription_Pack_Expiry=requiredFormat.format(packExpiryDate).toString();
		String Next_Expiring_Pack=Latest_Subscription_Pack;
		String Next_Pack_Expiry_Date=Latest_Subscription_Pack_Expiry;
		String billing_frequency=subscriptionResp.jsonPath().get("subscription_plan["+(subscriptionItems-1)+"].billing_frequency").toString();	
		Response tvodResp=ResponseInstance.getTVODDetails(username, password);
		int tvodItems=tvodResp.jsonPath().get("playback_state.size()");
		String HasRental="";
		try {			
			if(tvodResp.jsonPath().get("playback_state["+(tvodItems-1)+"]").toString().equalsIgnoreCase("purchased")) HasRental="true";
			else HasRental="false";
		}catch(Exception e) {HasRental="false";}
		
		Response settingsResp=ResponseInstance.getSettingsDetails(username, password);
		String hasEduauraa="false",key="";
		int pairs=settingsResp.jsonPath().get("key.size()");
		for (int i=0;i<pairs;i++) {
			key=settingsResp.jsonPath().get("key["+i+"]").toString();
			if(key.equals("eduauraaClaimed")) { 			
				hasEduauraa=settingsResp.jsonPath().get("value["+i+"]").toString();
				if(hasEduauraa.equals("")) hasEduauraa="false";
				break;
			}
		}
		Mixpanel.FEProp.setProperty("Free Trial Expiry Date", "N/A");
		Mixpanel.FEProp.setProperty("Free Trial Package", "N/A");		
		Mixpanel.FEProp.setProperty("Latest Subscription Pack", Latest_Subscription_Pack);
		Mixpanel.FEProp.setProperty("Latest Subscription Pack Expiry", Latest_Subscription_Pack_Expiry);
		Mixpanel.FEProp.setProperty("Next Expiring Pack", Next_Expiring_Pack);
		Mixpanel.FEProp.setProperty("Next Pack Expiry Date", Next_Pack_Expiry_Date);
		Mixpanel.FEProp.setProperty("Pack Duration", billing_frequency);
		Mixpanel.FEProp.setProperty("HasRental", HasRental);
		Mixpanel.FEProp.setProperty("hasEduauraa", hasEduauraa);
	}

	@SuppressWarnings("static-access")
	public static void extentReportFail(String info, String details) {
		extent.childTest.get().log(Status.FAIL, details);
	}

	@SuppressWarnings("static-access")
	public static void extentReportInfo(String info, String details) {
		extent.childTest.get().log(Status.INFO, details);
	}

	
	
	public static void extentInfo() {
		try {
			if (propValue.equals("Empty")) {
				extentReportInfo("Empty parameter", "Parameter :- <b>Key : " + key + " <br/> value : " + value);
			} else {
				extentReportInfo("Empty parameter", "Parameter :- <b>Key : " + key + " <br/> value : " + value
					+ "<br/> Expected value : " + propValue + "</b>");
			}
		}catch(Exception e) {
			extentReportInfo("Empty parameter", "Parameter :- <b>Key : " + key + " <br/> value : " + value);
		}
	}

	public static String modelName() {
		try {
			String cmd3 = "adb shell getprop ro.product.model";
			Process process = Runtime.getRuntime().exec(cmd3);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			Modelname = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Modelname;
	}

	public static void getParameterValue() {
		UserType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
		String pUsername = null;
		String pPassword = null;
		if (!UserType.equals("Guest")) {
			if (!fetchUserdata) {
				if(UserType.equals("NonSubscribedUser")) {
				pUsername = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonSubscribedUserName");
				pPassword = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonsubscribedPassword");
				}else if(UserType.equals("SubscribedUser")) {
					pUsername = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedUserName");
					pPassword = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedPassword");
				}
				ResponseInstance.getUserData(pUsername, pPassword);
				ResponseInstance.getUserSettingsValues(pUsername, pPassword);
			}
		}
		fetchUserdata = false;
	}

	public static void getAdID() {
		try {
			String cmd3 = "adb shell \"grep adid_key /data/data/com.google.android.gms/shared_prefs/adid_settings.xml\"";
			Process process = Runtime.getRuntime().exec(cmd3);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			Modelname = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(Modelname);
	}
	
	public static String getLatestEvent(Response responseEvent) {
		try {
		String response = responseEvent.asString();
		String s[] = response.split("\n");
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < s.length; i++) {
			String commaSplit[] = s[i].replace("\"properties\":{", "").replace("}", "")
					.replaceAll("[.,](?=[^\\[]*\\])", "-").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			String com[] = commaSplit[1].split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			list.add(Integer.valueOf(com[1].replace("\"", "").replace("$", "")));
			System.out.println(Integer.valueOf(com[1].replace("\"", "").replace("$", "")));
		}
		System.out.println(String.valueOf(Collections.max(list)));
		for(int i = 0; i < s.length; i++) {
			if(s[i].contains(String.valueOf(Collections.max(list)))) {
//				System.out.println(s[i]);
				return s[i];
			}
		}
		}catch(Exception e) {
			
		}
		return "";
	}
	
	// added by Kushal
	
	public static String fetchContentId(String distinct_id, String eventName)
			throws JsonParseException, JsonMappingException, IOException {
		try {
			Thread.sleep(180000);
		} catch (InterruptedException e) {
		}
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now); // Get current date in formate yyyy-MM-dd
		System.out.println("Current Date : " + currentDate);
		if (platform.equals("Android")) {
			APIKey = "b2514b42878a7e7769945befa7857ef1";
			UserID = "$model";
			distinct_id = modelName();
		} else {
			APIKey = "58baafb02e6e8ce03d9e8adb9d3534a6";
			if (distinct_id.contains("-")) {
				UserID = "Unique ID";
				UserType = "Login";
			}
		}
		Response request = RestAssured.given().auth().preemptive().basic(APIKey, "")
				.config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()))
				.contentType("application/x-www-form-urlencoded; charset=UTF-8").formParam("from_date", currentDate)
				.formParam("to_date", currentDate).formParam("event", "[\"" + eventName + "\"]")
				.formParam("where", "properties[\"" + UserID + "\"]==\"" + distinct_id + "\"")
				.post("https://data.mixpanel.com/api/2.0/export/");
		request.print();
		String getContentId = null,getDistinctId=null;
		sheet = eventName.trim().replace(" ", "");
		if (request.toString() != null) {
			if (platform.equals("Web") || platform.equals("MPWA")) {
				parseResponse(getLatestEvent(request));
			} else {
				String response = request.asString();
				String s[] = response.split("\n");
				parseResponse(s[s.length - 1]);
				System.out.println("LATEST RESPONSE: \n"+s[s.length - 1]);
				getContentId = parseContentId(s[s.length-1]);
				getDistinctId= parseDistinctId(s[s.length-1]);
				DistinctId = getDistinctId;
			}
		}else {
			System.out.println("Event not triggered");
			extentReportFail("Event not triggered", "Event not triggered");
		}
		return getContentId;
	}
	
	public static String parseContentId(String response) {
		String strContentID = response.split("Content ID")[1].split(",")[0].replace("\":\"", "").replace("\"", "");
		System.out.println("CONTENT ID : "+strContentID);
		return strContentID;
	}


	public static String parseDistinctId(String response) {
		String strDistinctID = response.split("distinct_id")[1].split(",")[0].replace("\":\"", "").replace("\"", "");
		System.out.println("Distinct ID : " + strDistinctID);
		return strDistinctID;
	}

	public static void parentalValidateParameter(String distinctID, String eventName)
			throws JsonParseException, JsonMappingException, IOException, InterruptedException, ParseException {
		System.out.println("Parameter Validation " + distinctID);
		PropertyFileReader Prop = new PropertyFileReader("properties/MixpanelKeys.properties");
		booleanParameters = Prop.getproperty("Boolean");
		integerParameters = Prop.getproperty("Integer");
		fileName = ReportName;
		xlpath = System.getProperty("user.dir") + "\\" + fileName + ".xlsx";
		StaticValues(distinctID);
		getParentalParameterValue();
		fetchEvent(distinctID, eventName);
	}

	public static void getParentalParameterValue() {
		UserType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
		if (!UserType.equals("Guest")) {
			if (!fetchUserdata) {
				String pUsername = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
						.getParameter("SettingsNonSubscribedUserName");
				String pPassword = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
						.getParameter("SettingsNonSubscribedPassword");
				ResponseInstance.getUserData(pUsername, pPassword);
			}
		}
	}
	
	public String languageShortform(String lang) {
		PropertyFileReader handler = new PropertyFileReader("properties/MixpanelKeys.properties");
		String lng = handler.getproperty(lang.toLowerCase());
		return lng;
	}

	
	public static void ValidateParameterForCarouselClick(String distinctID, String eventName, String contentLang) throws JsonParseException, JsonMappingException, IOException, InterruptedException, ParseException {
		System.out.println("Parameter Validation " + distinctID);
		Prop = new PropertyFileReader("properties/MixpanelKeys.properties");
		booleanParameters = Prop.getproperty("Boolean");
		integerParameters = Prop.getproperty("Integer");
		fileName = ReportName;
		xlpath = System.getProperty("user.dir") + "\\" + fileName + ".xlsx";
		StaticValues(distinctID);
		userType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");		
		if(userType.equals("Guest")) {
			Mixpanel.FEProp.setProperty("New App Language", "en");
			Mixpanel.FEProp.setProperty("New Content Language", "["+contentLang.replace(",", "-")+"]");
		}
		getParameterValue();
		fetchEvent(distinctID, eventName);
		SubcribedDetails = false;
	}
	
	
//===============================================================================================
	public static void ValidateParameterForPlayer(String distinctID, String eventName, String contentLang) throws JsonParseException, JsonMappingException, IOException, InterruptedException, ParseException {
		System.out.println("Parameter Validation " + distinctID);
		Prop = new PropertyFileReader("properties/MixpanelKeys.properties");
		booleanParameters = Prop.getproperty("Boolean");
		integerParameters = Prop.getproperty("Integer");
		fileName = ReportName;
		xlpath = System.getProperty("user.dir") + "\\" + fileName + ".xlsx";
		StaticValues(distinctID);
		userType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");		
		if(userType.equals("Guest")) {
			Mixpanel.FEProp.setProperty("New App Language", "en");
			Mixpanel.FEProp.setProperty("New Content Language", "["+contentLang.replace(",", "-")+"]");
		}
		getParameterValue();
		try {//Wait before fetching MP
			Thread.sleep(18000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		fetchEventWithNoWait(distinctID, eventName);//Idea for calling multiple events with just 1 wait time
		//SubcribedDetails = false;
	}


	public static void fetchEventWithNoWait(String distinct_id, String eventName) throws JsonParseException, JsonMappingException, IOException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now); // Get current date in formate yyyy-MM-dd
		System.out.println("Current Date : " + currentDate);		
		if (platform.equalsIgnoreCase("Web") || platform.equalsIgnoreCase("MPWA")){
			APIKey = "58baafb02e6e8ce03d9e8adb9d3534a6";
			if (distinct_id.contains("-")) {
				UserID = "Unique ID";
				UserType = "Login";
			}
		}	
		Response mpresponse=null;
		for(int trial=0;trial<5;trial++) {
			mpresponse = RestAssured.given().auth().preemptive().basic(APIKey, "")
					.config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()))
					.contentType("application/x-www-form-urlencoded; charset=UTF-8").formParam("from_date", currentDate)
					.formParam("to_date", currentDate).formParam("event", "[\"" + eventName + "\"]")
					.formParam("where", "properties[\"" + UserID + "\"]==\"" + distinct_id + "\"")
					.post("https://data.mixpanel.com/api/2.0/export/");
			if(mpresponse.equals(null) || mpresponse.equals("")) extent.extentLogger("", "Failed to fetch MP Response");
			else break;
		}		
		mpresponse.prettyPrint();
		sheet = eventName.trim().replace(" ", "").replace("/", "");
		if (mpresponse.toString() != null) {
				String response = mpresponse.asString();
				String s[] = response.split("\n");
				String str = s[s.length-1];
				JsonObject obj = new JsonParser().parse(str).getAsJsonObject();
				String properties=obj.get("properties").toString();
				JsonObject objprop = new JsonParser().parse(properties).getAsJsonObject();			
				ArrayList<String> mpparameters=new ArrayList<String>();
				objprop.keySet().forEach(keyStr ->{
			        Object keyvalue = objprop.get(keyStr);
			        System.out.println("key: "+ keyStr + " value: " + keyvalue);
			        mpparameters.add(keyStr.replace("\"", "").replace("$", "")+"keyvalue"+keyvalue.toString().replace("\"", "").replace("$", "").replace(",", "-"));	        
			    });
				parseResponse(mpparameters);
				validation();
		}else {
			System.out.println("Event not triggered");
			extentReportFail("Event not triggered", "Event not triggered");
		}
	}
	
	public static void parseResponse(ArrayList<String> response) {
		creatExcel(); // Create an excel file
		for (int i = 1; i < response.size(); i++) {
			write(i, response.get(i).split("keyvalue")[0], response.get(i).split("keyvalue")[1]);
		}
	}
	
	
	public static void ValidateParameterForPlayer(String[] mpresponse, int slno,String distinctID, String eventName, String contentLang,ArrayList<HashMap<String,String>> valuesPendingValidation,String contentID) throws JsonParseException, JsonMappingException, IOException, InterruptedException, ParseException {
		System.out.println("Parameter Validation for " + eventName+" : "+distinctID);
		Prop = new PropertyFileReader("properties/MixpanelKeys.properties");
		booleanParameters = Prop.getproperty("Boolean");
		integerParameters = Prop.getproperty("Integer");
		fileName = ReportName;
		String eventForFileName=eventName.replace("/", "_");
		makeDirectory(System.getProperty("user.dir"), "mp_excel_reports");
		xlpath = System.getProperty("user.dir") + "\\mp_excel_reports"+ "\\" +fileName +"_"+eventForFileName+".xlsx";
		System.out.println("Distinct ID:"+distinctID);
		StaticValuesFromAPI(distinctID);
		userType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");		
		if(userType.equals("Guest")) {
			Mixpanel.FEProp.setProperty("New App Language", "en");
			Mixpanel.FEProp.setProperty("New Content Language", "["+contentLang.replace(",", "-")+"]");
		}
		getParameterValue();
		fetchEventWithNoWait(mpresponse,slno,distinctID, eventName,valuesPendingValidation,contentID);
	}
	
	
	
	public static void fetchEventWithNoWait(String[] mpresponse,int slno,String distinct_id, String eventName,ArrayList<HashMap<String,String>> valuesPendingValidation,String contentID) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		System.out.println("inside fetchEventWithNoWait");
		sheet = eventName.trim().replace(" ", "").replace("/", "");
		ArrayList eventresponse= new ArrayList<String>();
		int eventresponseLength=0;
		for(int i=0;i<(mpresponse.length);i++) {
			if(mpresponse[i].toString().contains("\"event\":\""+eventName+"\"")){			
				eventresponse.add(mpresponse[i]);
				eventresponseLength++;
			}
		}
		int totalEventEntries=eventresponse.size();
		System.out.println("Total Event Entries for :"+eventName+" : "+totalEventEntries);
		extent.extentLogger("", "Total Event Entries for :"+eventName+" : "+totalEventEntries);
		if(totalEventEntries!=0) {
			String str =eventresponse.get(totalEventEntries-1).toString();
			System.out.println(str);
			JsonObject obj = new JsonParser().parse(str).getAsJsonObject();
			String properties=obj.get("properties").toString();
			JsonObject objprop = new JsonParser().parse(properties).getAsJsonObject();			
			ArrayList<String> mpparameters=new ArrayList<String>();
			objprop.keySet().forEach(keyStr ->{
				 Object keyvalue = objprop.get(keyStr);
				 System.out.println("key: "+ keyStr + " value: " + keyvalue);
				        mpparameters.add(keyStr.replace("\"", "").replace("$", "")+"keyvalue"+keyvalue.toString().replace("\"", "").replace("$", "").replace(",", "-"));	        
			});
			System.out.println(mpparameters);
			parseResponseWithHandleEmptyValues(mpparameters);
			validation(slno,eventName);
			DisplayMPValue("Conviva Session ID");
			DisplayMPValue("Talamoos clickID");
			DisplayPartialValues(valuesPendingValidation);
		}		
	}
	
	public static void parseResponseWithHandleEmptyValues(ArrayList<String> response) {
		deleteAndCreatExcel();
		String key="",value="";
		System.out.println("Excel created");
		for (int i = 1; i < response.size(); i++) {
			try {
				key=response.get(i).split("keyvalue")[0];
				try {
					value=response.get(i).split("keyvalue")[1];
					write(i, key, value);
				}
				catch(Exception e) {}
			}
			catch(Exception e) {}	
		}
	}
	

	public static void validation(int slno,String event) {
		int NumberOfRows = getRowCount();
		System.out.println(NumberOfRows);
		extent.HeaderChildNode(slno+": "+"Parameter Validation ->"+event);
		if(NumberOfRows != 0) {
		for (rownumber = 1; rownumber < NumberOfRows; rownumber++) {
			try {
				XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
				XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
				value = myExcelSheet.getRow(rownumber).getCell(1).toString();
				key = myExcelSheet.getRow(rownumber).getCell(0).toString();
				if (value.trim().isEmpty()) {
					System.out.println("Paramter is empty :- Key:" + key + " - value" + value);
					extentReportFail("Empty parameter","Paramter is empty :- <b>Key : " + key + " \n value : " + value + "</b>");
					fillCellColor();
				} else {
					if (isContain(booleanParameters, key)) {
						validateBoolean(value);
					} else if (isContain(integerParameters, key)) {
						validateInteger(value);
					}else if (isContain(integerParameters, key)) {
						validateFloat(value);
					}
					validateParameterValuePWA(key, value);
					//extentInfo();
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		}else {
			System.out.println("Event not triggered");
			extentReportFail("Event not triggered", "Event not triggered");
		}
		//FEProp.clear();
	}
	
	public static void validateParameterValuePWA(String key, String value) {
		try {
			propValue = FEProp.getProperty(key);
			System.out.println("key0"+key+"00propValue-----"+propValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (propValue != null) {
			if (propValue.equals("[]")) {
				propValue = "N/A";
			}
			if (!propValue.replaceAll("\\s", "").equalsIgnoreCase(value.replaceAll("\\s", ""))) {
				fillCellColor();
				extentReportFail("Parameter", "Parameter : <b>Key : " + key + " <br/> value : " + value + "<br/>Expected Value : "+propValue+"</b>");
			}else {
				extentReportInfo("Parameter", "Parameter : <b>Key : " + key + " <br/> value : " + value + "<br/>Expected Value : "+propValue+"</b>");
			}
		}
	}


	
	public static void makeDirectory(String path,String directoryName) {
		try {
			path=path+"/"+directoryName;
			File file = new File(path);
			if (!file.exists()){
				file.mkdirs();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void StaticValuesFromAPI(String UniqueID) throws ParseException {
		platform = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getSuite().getName();
		if (platform.equals("Mpwa")) {
			FEProp.setProperty("Platform Name", "Web");
			FEProp.setProperty("os", "Android");
		} else if (platform.equals("Android")) {
			FEProp.setProperty("Platform Name", platform);
			FEProp.setProperty("os", "Android");
		} else if (platform.equals("Web")) {
			FEProp.setProperty("Platform Name", platform);
			FEProp.setProperty("os", System.getProperty("os.name").split(" ")[0]);
		}
		Mixpanel.FEProp.setProperty("Landing Page Name", "home");
		Mixpanel.FEProp.setProperty("Unique ID", UniqueID);
		
		userType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
		
		if(userType.equals("Guest")) {
				Mixpanel.FEProp.setProperty("Gender", "N/A");
				Mixpanel.FEProp.setProperty("Age", "N/A");
				Mixpanel.FEProp.setProperty("Free Trial Expiry Date", "N/A");
				Mixpanel.FEProp.setProperty("Free Trial Package", "N/A");
				Mixpanel.FEProp.setProperty("Latest Subscription Pack", "N/A");
				Mixpanel.FEProp.setProperty("Latest Subscription Pack Expiry", "N/A");
				Mixpanel.FEProp.setProperty("Next Expiring Pack", "N/A");
				Mixpanel.FEProp.setProperty("Next Pack Expiry Date", "N/A");
				Mixpanel.FEProp.setProperty("Pack Duration", "N/A");
				Mixpanel.FEProp.setProperty("Parent Control Setting", "N/A");
				Mixpanel.FEProp.setProperty("User Type", "guest");
				Mixpanel.FEProp.setProperty("Partner Name", "N/A");
				Mixpanel.FEProp.setProperty("HasRental", "false");
				Mixpanel.FEProp.setProperty("hasEduauraa", "false");
		}
		else SubscriptionDetailsFromAPI();
	}
	
	
	public static void SubscriptionDetailsFromAPI() throws ParseException {
		System.out.println("Entered SubscriptionDetailsFromAPI ...");
		String UserType;
		UserType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
		String username = null,password = null;
		if(UserType.equals("NonSubscribedUser")) {
			username = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonSubscribedUserName");
			password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonsubscribedPassword");
		}
		if(UserType.equals("SubscribedUser")) {
			username = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedUserName");
			password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedPassword");
		}
		Response subscriptionResp=ResponseInstance.getSubscriptionDetails(username, password);
		//System.out.println(subscriptionResp.prettyPrint());
		int subscriptionItems=0;
		String id="",subscription_plan_type="",title="",packExpiry="",HasRental="",hasEduauraa="",key="",parentalControl="";
		String Latest_Subscription_Pack="N/A",Latest_Subscription_Pack_Expiry="N/A",Next_Expiring_Pack="N/A",Next_Pack_Expiry_Date="N/A",billing_frequency="N/A";
		try{subscriptionItems=subscriptionResp.jsonPath().get("subscription_plan.size()");}catch(Exception e) {}
		if(subscriptionItems!=0) {
			id=subscriptionResp.jsonPath().get("subscription_plan["+(subscriptionItems-1)+"].id").toString();
			System.out.println("sub plan id"+id);
			subscription_plan_type=subscriptionResp.jsonPath().get("subscription_plan["+(subscriptionItems-1)+"].subscription_plan_type").toString();
			System.out.println("sub plan type"+subscription_plan_type);
			title=subscriptionResp.jsonPath().getString("subscription_plan["+(subscriptionItems-1)+"].original_title");
			System.out.println("sub plan title"+title);
			Latest_Subscription_Pack=id+"_"+title+"_"+subscription_plan_type;
			System.out.println("Latest_Subscription_Pack"+Latest_Subscription_Pack);
			packExpiry=subscriptionResp.jsonPath().get("subscription_end["+(subscriptionItems-1)+"]").toString();
			System.out.println("packExpiry"+packExpiry);
			SimpleDateFormat requiredFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			java.text.DateFormat actualFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			actualFormat.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
			java.util.Date packExpiryDate = actualFormat.parse(packExpiry);
			Latest_Subscription_Pack_Expiry=requiredFormat.format(packExpiryDate).toString();
			System.out.println("Latest_Subscription_Pack_Expiry"+Latest_Subscription_Pack_Expiry);
			Next_Expiring_Pack="["+Latest_Subscription_Pack+"]";
			System.out.println("Next_Expiring_Pack"+Next_Expiring_Pack);
			Next_Pack_Expiry_Date=Latest_Subscription_Pack_Expiry;
			System.out.println("Next_Pack_Expiry_Date"+Next_Pack_Expiry_Date);
			billing_frequency=subscriptionResp.jsonPath().get("subscription_plan["+(subscriptionItems-1)+"].billing_frequency").toString();	
			System.out.println("billing_frequency"+billing_frequency);
		}	
		Response tvodResp=ResponseInstance.getTVODDetails(username, password);
		int tvodItems=tvodResp.jsonPath().get("playback_state.size()");
		try {			
			if(tvodResp.jsonPath().get("playback_state["+(tvodItems-1)+"]").toString().equalsIgnoreCase("purchased")) HasRental="true";
			else HasRental="false";
		}catch(Exception e) {HasRental="false";}
		System.out.println("HasRental"+HasRental);
		Response settingsResp=ResponseInstance.getSettingsDetails(username, password);
		int pairs=settingsResp.jsonPath().get("key.size()");
		for (int i=0;i<pairs;i++) {
			key=settingsResp.jsonPath().get("key["+i+"]").toString();
			if(key.equals("eduauraaClaimed")) { 			
				hasEduauraa=settingsResp.jsonPath().get("value["+i+"]").toString();
				if(hasEduauraa.equals("")) hasEduauraa="false";
				break;
			}
		}
		System.out.println("hasEduauraa"+hasEduauraa);
		for (int i=0;i<pairs;i++) {
			key=settingsResp.jsonPath().get("key["+i+"]").toString();
			if(key.equals("parental_control")) { 			
				parentalControl=settingsResp.jsonPath().get("value["+i+"]").toString();
				if(parentalControl.equals("") || parentalControl.equals("{}")) parentalControl="N/A";
				else parentalControl="true";
				break;
			}
		}
		System.out.println("parentalControl"+parentalControl);
		
		Mixpanel.FEProp.setProperty("Free Trial Expiry Date", "N/A");
		Mixpanel.FEProp.setProperty("Free Trial Package", "N/A");		
		Mixpanel.FEProp.setProperty("Latest Subscription Pack", Latest_Subscription_Pack);
		Mixpanel.FEProp.setProperty("Latest Subscription Pack Expiry", Latest_Subscription_Pack_Expiry);
		Mixpanel.FEProp.setProperty("Next Expiring Pack", Next_Expiring_Pack);
		Mixpanel.FEProp.setProperty("Next Pack Expiry Date", Next_Pack_Expiry_Date);
		Mixpanel.FEProp.setProperty("Pack Duration", billing_frequency);
		Mixpanel.FEProp.setProperty("HasRental", HasRental);
		Mixpanel.FEProp.setProperty("hasEduauraa", hasEduauraa);
		Mixpanel.FEProp.setProperty("Parent Control Setting", parentalControl);
	}
	
	
	public static void DisplayMPValue(String property) {
		int NumberOfRows = getRowCount();
		System.out.println(NumberOfRows);
		if(NumberOfRows != 0) {
			for (rownumber = 1; rownumber < NumberOfRows; rownumber++) {
				try {
					XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
					XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
					value = myExcelSheet.getRow(rownumber).getCell(1).toString();
					key = myExcelSheet.getRow(rownumber).getCell(0).toString();
					if(key.equals(property)) {
						System.out.println("MP Property Key: <b>"+key+"</b> has value: <b>"+value+"</b>");
						if(!value.equals("")) {							
							extent.extentLogger("", "MP Property Key:<b>"+key+"</b> has value: <b>"+value+"</b>");					
						}
						else {
							extent.extentLoggerFail("", "MP Property Key:<b>"+key+"</b> has value: <b>"+value+"</b>");
						}
						break;
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}
	}	
	
	public static void DisplayPartialValues(ArrayList<HashMap<String,String>> valuesToCompare) {
		String expectedProperty="",expectedValue="";
		boolean foundKey=false,foundAdKey=false;
		try {
			int NumberOfRows = getRowCount();
			if(NumberOfRows != 0) {
				System.out.println(NumberOfRows);
				for(int i=0;i<valuesToCompare.size();i++) {
					HashMap<String,String> valuesToCompareMap=valuesToCompare.get(i);
					Set keys = valuesToCompareMap.keySet();
					Iterator it = keys.iterator();
					while (it.hasNext()) {
						foundKey=false;
						expectedProperty=it.next().toString();
						expectedValue=valuesToCompareMap.get(expectedProperty);
						System.out.println("expectedProperty==="+expectedProperty);
						System.out.println("expectedValue==="+expectedValue);
						for (rownumber = 1; rownumber < NumberOfRows; rownumber++) {
							try {
								XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
								XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
								value = myExcelSheet.getRow(rownumber).getCell(1).toString();
								key = myExcelSheet.getRow(rownumber).getCell(0).toString();
								if(key.equals(expectedProperty) && key.equals("Ad Destination URL")) {	
									foundAdKey=true;
									break;
								}
								if(key.equals(expectedProperty)) {	
									foundKey=true;
									break;
								}
							} catch (Exception e) {
								System.out.println(e);
							}
						}
						if(foundKey==true) {
							int intValue=0,intcalculatedValue=0;
							intValue=Integer.valueOf(value);
							intcalculatedValue=Integer.valueOf(expectedValue);
							if(intValue-intcalculatedValue>=-5 && intValue-intcalculatedValue<=5) 
								extentReportInfo("Parameter", "Parameter : <b>Key : " + key + " <br/> value : " + value + "<br/>Expected Value : "+intcalculatedValue+"</b>"+"<br/>Difference of +/-5 is maintained");
							else
								extentReportFail("Parameter", "Parameter : <b>Key : " + key + " <br/> value : " + value + "<br/>Expected Value : "+intcalculatedValue+"</b>"+"<br/>Difference of +/-5 is not maintained");
						}	
						if(foundAdKey==true) {
							value=value.split("signatur")[0];
							value=value.replace(",", "-");
							value=value+"signature";
							if(value.equalsIgnoreCase(expectedValue)) {
								extentReportInfo("Parameter", "Parameter : <b>Key : " + key + " <br/> value : " + value + "</b><br/>contains Expected Value : <br/><b>"+expectedValue+"</b>");
							}else {								
								extentReportFail("Parameter", "Parameter : <b>Key : " + key + " <br/> value : " + value + "</b><br/>does not contains Expected Value : <br/><b>"+expectedValue+"</b>");
							}
						}
					}					
				}
			}
		}catch(Exception e) {}
	}
	
	public static void DisplayPlayHeadPositionValue(String calculatedValue) {
		try {
			int NumberOfRows = getRowCount();
			System.out.println(NumberOfRows);
			if(NumberOfRows != 0) {
				for (rownumber = 1; rownumber < NumberOfRows; rownumber++) {
					try {
						XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
						XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
						value = myExcelSheet.getRow(rownumber).getCell(1).toString();
						key = myExcelSheet.getRow(rownumber).getCell(0).toString();
						if(key.equals("Player Head Position")) {							
							break;
						}
					} catch (Exception e) {
						System.out.println(e);
					}
				}
				int intValue=0,intcalculatedValue=0;
				intValue=Integer.valueOf(value);
				intcalculatedValue=Integer.valueOf(calculatedValue);
				if(intValue-intcalculatedValue>=-5 && intValue-intcalculatedValue<=5) 
					extentReportInfo("Parameter", "Parameter : <b>Key : " + key + " <br/> value : " + value + "<br/>Expected Value : "+calculatedValue+"</b>"+"<br/>Difference of +/-5 is maintained");
				else
					extentReportFail("Parameter", "Parameter : <b>Key : " + key + " <br/> value : " + value + "<br/>Expected Value : "+calculatedValue+"</b>"+"<br/>Difference of +/-5 is not maintained");
			}
		}catch(Exception e) {}
	}
	
	public static void DisplayWatchDurationValue(String calculatedValue) {
		int NumberOfRows = getRowCount();
		System.out.println(NumberOfRows);
		if(NumberOfRows != 0) {
			for (rownumber = 1; rownumber < NumberOfRows; rownumber++) {
				try {
					XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
					XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
					value = myExcelSheet.getRow(rownumber).getCell(1).toString();
					key = myExcelSheet.getRow(rownumber).getCell(0).toString();
					if(key.equals("Watch Duration")) {							
						break;
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			int intValue=0,intcalculatedValue=0;
			intValue=Integer.valueOf(value);
			intcalculatedValue=Integer.valueOf(calculatedValue);
			if(intValue-intcalculatedValue>=-5 && intValue-intcalculatedValue<=5) 
				extentReportInfo("Parameter", "Parameter : <b>Key : " + key + " <br/> value : " + value + "<br/>Expected Value : "+calculatedValue+"</b>"+"<br/>Difference of +/-5 is maintained");
			else
				extentReportFail("Parameter", "Parameter : <b>Key : " + key + " <br/> value : " + value + "<br/>Expected Value : "+calculatedValue+"</b>"+"<br/>Difference of +/-5 is not maintained");
		}
	}
	
	
	public static void deleteAndCreatExcel() {
		try {
			File file = new File(xlpath);
			if (file.exists()) {
				
				file.delete();
				System.out.println("deleted file");
			}
			if (!file.exists()) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				workbook.createSheet(sheet); // Create sheet
				FileOutputStream fos = new FileOutputStream(new File(xlpath));
				workbook.write(fos);
				workbook.close();
				System.out.println("created file");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	public static String[] fetchMPResponse(String distinct_id, String[] eventNames) throws Exception {
		String[] mpRespString= {};
		String eventNamesString="";
		for(int i=0;i<eventNames.length;i++) {
			eventNamesString=eventNamesString.concat("\""+eventNames[i]+"\"");
			if(i!=(eventNames.length-1)) eventNamesString=eventNamesString.concat(",");			
		}	
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now); // Get current date in formate yyyy-MM-dd
		System.out.println("Current Date : " + currentDate);		
		if (platform.equalsIgnoreCase("Web") || platform.equalsIgnoreCase("MPWA")){
			APIKey = "58baafb02e6e8ce03d9e8adb9d3534a6";
			if (distinct_id.contains("-")) {
				UserID = "Unique ID";
				UserType = "Login";
			}
		}
		
		Response mpresponse=null;
		for(int trial=0;trial<5;trial++) {
			System.out.println("MP trial : "+trial);
			try {
				mpresponse = RestAssured.given().auth().preemptive().basic(APIKey, "")
					.config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()))
					.contentType("application/x-www-form-urlencoded; charset=UTF-8").formParam("from_date", currentDate)
					.formParam("to_date", currentDate).formParam("event", "["+eventNamesString+"]")
					.formParam("limit", 10)
					.formParam("where", "properties[\"" + UserID + "\"]==\"" + distinct_id + "\"")
					.post("https://data.mixpanel.com/api/2.0/export/");
				if(mpresponse.toString() != null && !mpresponse.toString().equals("")) {
					System.out.println("MP For loop out in trial : "+trial);
					break;
				}
			}
			catch(Exception e) {}
			Thread.sleep(10000);
		}	
		if(mpresponse.toString() == null || mpresponse.asString().equals("")) extent.extentLoggerFail("", "MP Response not recieved");
		else {
			mpresponse.prettyPrint();
			String response = mpresponse.asString();
			mpRespString = response.split("\n");
		}
		return mpRespString;
	}
	


}


