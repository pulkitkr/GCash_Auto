package com.extent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.stream.Stream;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import com.CleverTap.QOEMatrix;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.deviceDetails.DeviceDetails;
import com.driverInstance.DriverInstance;
import com.emailReport.SendPerformanceMail;
import com.excel.ExcelUpdate;
import com.propertyfilereader.PropertyFileReader;
import com.utility.Json;
import com.utility.LoggingUtils;
import io.appium.java_client.AppiumDriver;

public class ExtentReporter implements ITestListener {

	private static String report;
	protected static String platform;
	private static ThreadLocal<ExtentReports> extent = new ThreadLocal<>();
	private static ThreadLocal<ExtentHtmlReporter> htmlReport = new ThreadLocal<>();
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	protected static ThreadLocal<ExtentTest> childTest = new ThreadLocal<>();
	private static File src;
	private static String currentDate;
	private boolean runmode = true;
	private static String BrowserType;
	public static String filePath;
	public static String fileName;
	private static String AppVersion;
	public static String ReportName;
	public static String userType;
	public static ArrayList<String> mailBodyPart = new ArrayList<String>();
	public static int totalTests = 0;
	private static int totalPassedTest = 0;
	private static int totalFailedTest = 0;
	private static ArrayList<String> moduleFail = new ArrayList<String>();
	private static int moduleFailCount = 0;
	private static int logfail = 0;
	public static String version;
	public static String jiraID = "TC";
	static String buildVersion;
	public static String CTCurrentTime;
	public static ArrayList<String> performaceDetails = new ArrayList<String>();
	public static Dictionary<String, String> performaceMatrics = new Hashtable<String, String>();
	static int passed = 0;
	static int failed = 0;

	/** The Constant logger. */
	static LoggingUtils logger = new LoggingUtils();

	@SuppressWarnings("static-access")
	public void setReport(String report) {
		this.report = report;
	}

	@SuppressWarnings("static-access")
	public String getReport() {
		return this.report;
	}

	@SuppressWarnings("static-access")
	public String getPlatform() {
		return this.platform;
	}

	@SuppressWarnings("static-access")
	public void setPlatform(String platform) {
		this.platform = platform; 
	}

	public String getPlatformFromtools() {
		return DriverInstance.getPlatform();
	}
	@SuppressWarnings("static-access")
	public String getAppVersion() {
		return this.AppVersion;
	}

	@SuppressWarnings("static-access")
	public void setAppVersion(String versionName) {
		this.AppVersion = versionName;
	}

	public static AppiumDriver<WebElement> getDriver() {
		return DriverInstance.tlDriver.get();
	}

	private WebDriver getWebDriver() {
		return DriverInstance.tlWebDriver.get();
	}

	public void initExtentDriver() {
		if (getPlatformFromtools().equals("Web")) {
			src = ((TakesScreenshot) getWebDriver()).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
		} else if (getPlatformFromtools().equals("Android") || getPlatformFromtools().equals("PWA") || getPlatformFromtools().equals("TV")) {
			src = ((TakesScreenshot) getDriver()).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
		} else if (getPlatformFromtools().equals("MPWA")) {
			src = ((TakesScreenshot) getDriver()).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
		}
	}

	@Override
	public void onStart(ITestContext context) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		currentDate = dateFormat.format(date).toString().replaceFirst(" ", "_").replaceAll("/", "_").replaceAll(":",
				"_");
		setReport(context.getName());
		setPlatform(context.getSuite().getName());
		userType = context.getCurrentXmlTest().getParameter("userType");
		appVersion();
		DeviceDetails.deviceDetails();
		filePath = System.getProperty("user.dir") + "/Reports" + "/" + currentDate + "/" + getPlatform() + "/"
				+ context.getCurrentXmlTest().getParameter("userType") + "/" + context.getName() + "/"
				+ context.getCurrentXmlTest().getParameter("userType") + "_" + context.getName() + "_" + getAppVersion()
				+ "_" + getDate() + ".html";

		fileName = context.getCurrentXmlTest().getParameter("userType") + "_" + context.getName() + "_"
				+ getAppVersion() + "_" + getDate() + ".html";

		htmlReport.set(new ExtentHtmlReporter(filePath));
		htmlReport.get().loadXMLConfig(new File(System.getProperty("user.dir") + "/ReportsConfig.xml"), true);
		extent.set(new ExtentReports());
		extent.get().attachReporter(htmlReport.get());
		BrowserType = context.getCurrentXmlTest().getParameter("browserType");
		ExcelUpdate.UserType = context.getCurrentXmlTest().getParameter("userType");
		if (!getPlatform().equals("Web")) {
			DeviceDetails.getTheDeviceManufacturer();
			DeviceDetails.getTheDeviceOSVersion();
		}
		CleverTapTime();
//		QOEMatrix.creatExcelPerformance();
	}

	@Override
	public void onTestStart(ITestResult result) {
		if(DriverInstance.startTest) {
		if ((Stream.of(result.getName(), "Suite").anyMatch(DriverInstance.getRunModule()::equals)
				&& DriverInstance.startTest)|| result.getName().equals("Login")
				|| result.getName().equals("PWAWEBLogin") || result.getName().equals("tvLogin")) {
			ReportName = result.getName();
			DriverInstance.methodName = result.getName();
			ExcelUpdate.ModuleName = result.getName();
			logger.info(":::::::::Test " + result.getName() + " Started::::::::");
			test.set(extent.get().createTest(result.getName(),DriverInstance.getENvironment()));
			totalTests++;
			ExcelUpdate.passCounter = ExcelUpdate.failCounter = ExcelUpdate.warningCounter = moduleFailCount = 0;
			ExcelUpdate.creatExcel();
			} else {
				runmode = false;
				throw new SkipException("");
			}
		}else {
			runmode = false;
		}
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		if (DriverInstance.startTest) {
			screencapture();
			childTest.get().log(Status.PASS, result.getName() + " is PASSED");
			logger.info("::::::::::Test " + result.getName() + " PASSED::::::::::");
			if(moduleFailCount == 0) {
			moduleFail.add(result.getName()+","+"Pass");
			}else {
			moduleFail.add(result.getName()+","+"Fail");
			}
			if(logfail != 0) {
				totalFailedTest++;
			}else {
				totalPassedTest++;
			}
			System.out.println("JIRA ID : "+jiraID);
			try {
				if(logfail != 0) {
					Json.XrayJsonImport(jiraID, "FAILED");
				}else {
					Json.XrayJsonImport(jiraID, "PASSED");
				}
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
//		mailBodyPart.add(result.getName()+","+ExcelUpdate.passCounter+","+ExcelUpdate.failCounter);
		}
	}

	@Override
	public void onTestFailure(ITestResult result) {
		if (DriverInstance.startTest) {
			if ((getDriver() != null) || (getWebDriver() != null)) {
				childTest.get().log(Status.FAIL, result.getName() + " is FAILED");
				logger.info("::::::::::Test " + result.getName() + " FAILED::::::::::");
				moduleFail.add(result.getName()+","+"Fail");
				totalFailedTest++;
				try {
			         if(logfail != 0) {
				                Json.XrayJsonImport(jiraID, "FAILED");
							}else {
								Json.XrayJsonImport(jiraID, "PASSED");
							}
						} catch (InterruptedException | IOException e) {
							e.printStackTrace();
						}
//		mailBodyPart.add(result.getName()+","+ExcelUpdate.passCounter+","+ExcelUpdate.failCounter);
			}
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		if (DriverInstance.startTest) {
			if (runmode) {
				HeaderChildNode(result.getTestName());
				childTest.get().log(Status.SKIP, result.getName() + " is SKIPPED");
				logger.info("::::::::::Test " + result.getName() + " SKIPPED::::::::::");
//			mailBodyPart.add(result.getName()+","+ExcelUpdate.passCounter+","+ExcelUpdate.failCounter);
			}
		}
	}

	public void HeaderChildNode(String header) {
		if (test.get() != null)
			childTest.set(test.get().createNode(header));
			ExcelUpdate.Node(header);
	}

	public void extentLogger(String stepName, String details) {
		childTest.get().log(Status.INFO, details);
//		ExcelUpdate.writeData(details, "Pass", "");
	}
	
	public void extentLoggerPass(String stepName, String details) {
		childTest.get().log(Status.PASS, details);
		ExcelUpdate.writeData(details, "Pass", "");
	}

	public void extentLoggerFail(String stepName, String details) {
		childTest.get().log(Status.FAIL, details);
		screencapture();
		moduleFailCount = 1;
		logfail++;
		ExcelUpdate.writeData("", "Fail", details);
	}

	public void extentLoggerWarning(String stepName, String details) {
		childTest.get().log(Status.WARNING, details);
//		ExcelUpdate.writeData("", "Warning", details);
	}

	@Override
	public void onFinish(ITestContext context) {
		if (!getPlatformFromtools().equals("Web")) {
			extent.get().setSystemInfo("Device Info ", DeviceDetails.DeviceInfo());
			buildVersion = getAppVersion().replace("Build", "");
			extent.get().setSystemInfo("App version : ", buildVersion);
		} else if (getPlatformFromtools().equals("Web")) {
			extent.get().setSystemInfo("Browser Name ", BrowserType);
		}
		ExcelUpdate.updateResult();
		extent.get().flush();
//		totalPassedTest = context.getPassedTests().size();
//		totalFailedTest = context.getFailedTests().size();
//		totalSkipedTest = context.getSkippedTests().size();
//		SendEmail.EmailReport();
//		insertToExcel();
//		SendPerformanceMail.EmailReport();
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult context) {
	}

	public static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String name = dateFormat.format(date).toString().replaceFirst(" ", "_").replaceAll("/", "_").replaceAll(":","_");
		return name;
	}

	public void screencapture() {
		try {
			initExtentDriver();
			org.apache.commons.io.FileUtils.copyFile(src,
					new File(System.getProperty("user.dir") + "/Reports" + "/" + currentDate + "/" + getPlatform() + "/"
							+ Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
									.getParameter("userType")
							+ "/" + getReport() + "/Screenshots/" + getReport() + "_" + getDate() + ".jpg"));
			childTest.get().addScreenCaptureFromBase64String(base64Encode(src));
			logger.log(src, "Attachment");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void screencapture(WebDriver webdriver) {
		try {
			src = ((TakesScreenshot) webdriver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
			org.apache.commons.io.FileUtils.copyFile(src,
					new File(System.getProperty("user.dir") + "/Reports" + "/" + currentDate + "/" + getPlatform() + "/"
							+ Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
									.getParameter("userType")
							+ "/" + getReport() + "/Screenshots/" + getReport() + "_" + getDate() + ".jpg"));
			childTest.get().addScreenCaptureFromBase64String(base64Encode(src));
			logger.log(src, "Attachment");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String base64Encode(File file) {
		if (file == null || !file.isFile()) {
			return null;
		}
		try {
			@SuppressWarnings("resource")
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			if (fileInputStreamReader.read(bytes) != -1) {
				return "data:image/png;base64," + new String(Base64.getEncoder().encode(bytes), "UTF-8");
			}
			return null;
		} catch (Throwable e) {
			return null;
		}
	}

	public void appVersion() {
		if (getPlatform().equals("Android") || getPlatform().equals("TV")) {
			PropertyFileReader handler = new PropertyFileReader("properties/AppPackageActivity.properties");
			setAppVersion("Build " + DeviceDetails.getAppVersion(handler.getproperty("zeePackage")).trim()
					.replace("versionName=", ""));
			logger.info(getAppVersion());
		} else {
			setAppVersion("");
		}
	}

	public static StringBuilder updateTVResult() {
		StringBuilder builder = new StringBuilder();
		if (mailBodyPart.size() > 0) {
			for (int i = 0; i < mailBodyPart.size(); i++) {
				String result[] = mailBodyPart.get(i).toString().split(",");
//				System.out.println(result[0]+result[1]+result[2]);
				builder.append("        <tr>\r\n" + "          <td> " + result[0] + " </td>\r\n" + "          <td> "
						+ result[1] + " </td>\r\n" + "          <td> " + result[2] + " </td>\r\n"
						+ "        </tr>\r\n");
			}
			return builder;
		}else {
			return null;
		}
	}
	
	public static StringBuilder updateResult() {
		int totalTest = moduleFail.size();
		passedCount();
		StringBuilder builder = new StringBuilder();
				builder.append("        <tr>\r\n" + "          <td> " + (totalTest) + " </td>\r\n" + "          <td> "
						+ passed + " </td>\r\n" + "          <td> " + failed + " </td>\r\n"
						+ "        </tr>\r\n");
			return builder;
	}
	
	public static void passedCount(){
		for(int i=0;i<moduleFail.size();i++){
			String result[] = moduleFail.get(i).toString().split(",");
			if(result[1].equals("Pass")){
				passed ++;
			}else {
				failed++;
			}
		}
	}
	
	static double pass = 0;
	static double fail = 0;
	public static StringBuilder updateModuleResult() {
		StringBuilder builder = new StringBuilder();
		if (moduleFail.size() > 0) {
			for (int i = 0; i < moduleFail.size(); i++) {
				String result[] = moduleFail.get(i).toString().split(",");
				if(moduleFail.get(i).toString().contains("Pass")) {
					builder.append("<tr>\r\n" + "<td> " + result[0] + " </td>\r\n" + "<td> <span style=\"font-weight:bold;color:green\">"+ result[1] + " </td>\r\n"+ "</tr>\r\n");
					pass++;
				}else {
					builder.append("<tr>\r\n" + "<td> " + result[0] + " </td>\r\n" + "<td> <span style=\"font-weight:bold;color:red\">"+ result[1] + " </td>\r\n"+ "</tr>\r\n");
					fail++;
				}
			}
			return builder;
		}else {
			return null;
		}
	}
	
	public static StringBuilder performanceDetails() {
		StringBuilder builder = new StringBuilder();
		System.out.println(performaceDetails);
		if (performaceDetails.size() > 0) {
			for (int i = 0; i < performaceDetails.size(); i++) {
				String result[] = performaceDetails.get(i).toString().split(",");
					builder.append("<tr>\r\n" + "<td> " + result[0] + " </td>\r\n" + "<td>"+ result[1] + " </td>\r\n" + "<td>"+ result[2] + " </td>\r\n"
							+"<td>"+ result[3] + " </td>\r\n"+"<td>"+ result[4] + " </td>\r\n"+"<td>"+ result[5] + " </td>\r\n"
							+"<td>"+ result[6] + " </td>\r\n"+"<td>"+ result[7] + " </td>\r\n"+"</tr>\r\n");
			}
			return builder;
		}else {
			return null;
		}
	}
	
	public static void insertToExcel() {
		System.out.println("Size : "+performaceDetails.size());
		QOEMatrix.Count = true;
		if (performaceDetails.size() > 0) {
			for (int i = 0; i < performaceDetails.size(); i++) {
				int row = QOEMatrix.getRowCount();
				System.out.println(performaceDetails.get(i)+" == "+row);
				String result[] = performaceDetails.get(i).toString().split(",");
				QOEMatrix.InsertEventProperties((row+1), result[0], Integer.valueOf(result[1]), Integer.valueOf(result[2]), 
						Double.parseDouble(result[3]), Double.parseDouble(result[4]),Double.parseDouble(result[5]),Double.parseDouble(result[6]),
								Integer.valueOf(result[7]));
			}
			performaceDetails.clear();
		} else {
		}
	}
	
	public static StringBuilder DeviceDetails() {
		String deviceDetails = "Device Name - MarQ 2K Android TV Version - 9";
		StringBuilder builder = new StringBuilder();
				builder.append("        <tr>\r\n" + "          <td> " + deviceDetails.split("Version")[0]+ " </td>\r\n" + "          <td> "
						+  deviceDetails.split("Version - ")[1] + " </td>\r\n" + "          <td> " + buildVersion + " </td>\r\n"
						+ "        </tr>\r\n");
			return builder;
	}
	
	public void CleverTapTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		CTCurrentTime = dtf.format(now);
		System.out.println(CTCurrentTime);
	}
	
	public static StringBuilder updatePercentageOffailure() {
		StringBuilder builder = new StringBuilder();
		double total = (pass+fail);
	     double percentage;
	     percentage = (fail * 100/ total);
	     String percent = String.format("%.2f", percentage);
	     builder.append("<tr>\r\n" + "<td>"+total+"</td>\r\n" + "<td>"+pass+"</td>\r\n"+ "<td>"+fail+"</td>\r\n"+"<td>"+percent+"%</td>\r\n"+"</tr>\r\n");
	     return builder;
	}
}
