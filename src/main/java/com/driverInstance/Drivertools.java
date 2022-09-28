package com.driverInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;
import org.testng.SkipException;
import com.applitools.eyes.images.Eyes;
import com.extent.ExtentReporter;
import com.propertyfilereader.PropertyFileReader;
import com.utility.LoggingUtils;
import com.utility.Utilities;
import io.appium.java_client.AppiumDriver;

public class Drivertools {

	private String host;
	private int port;
	private String deviceName;
	private static String platform;
	private int appTimeOut;
	private String remoteUrl;
	private String appPackage;
	private String appActivity;
	private String appVersion;
	private String APkFileName;
	private PropertyFileReader handler;
	private static String testName;
	private String browserType;
	private String url = "";
	public static String runModule;
	private URLConnection connection;
	private URL connectURL;
	public static boolean startTest = false;
	private static String runMode = "null";
	private static String driverVersion = "";
	public static boolean click = true;
	public static String methodName = "";
	Date date = new Date();
	long StartTime;
	public static Instant startTime ;
	public static Duration timeElapsed ;
	private static String DeviceList;
	private static String TVDeviceList;
	private static String apk;
	public static String apkName = null;
	public static String dir = System.getProperty("user.dir") + "\\APK\\";

	public static String getApk() {
		return apk;
	}

	public static void setApk(String apk) {
		Drivertools.apk = apk;
	}

	public static String getDeviceList() {
		return DeviceList;
	}

	public static void setDeviceList(String deviceList) {
		DeviceList = deviceList;
	}

	public static String getTestName() {
		return testName;
	}

	@SuppressWarnings("static-access")
	public void setTestName(String testName) {
		this.testName = testName;
	}

	public static ThreadLocal<AppiumDriver<WebElement>> tlDriver = new ThreadLocal<>();

	public static ThreadLocal<WebDriver> tlWebDriver = new ThreadLocal<>();

	public static  ThreadLocal<AppiumDriver<WebElement>> driverTV = new ThreadLocal<>();
	
	public static Eyes eyes = new Eyes();

	public static ExtentReporter extent = new ExtentReporter();

	protected DesiredCapabilities capabilities = new DesiredCapabilities();

	protected Utilities util = new Utilities();

	private static String ENV = "";

	/** The Constant logger. */
//	final static Logger logger = Logger.getLogger("rootLogger");
	static LoggingUtils logger = new LoggingUtils();

	public static AppiumDriver<WebElement> getDriver() {
		return tlDriver.get();
	}

	public static WebDriver getWebDriver() {
		return tlWebDriver.get();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public static String getPlatform() {
		return platform;
	}

	public static void setPlatfrom(String Platform) {
		platform = Platform;
	}

	protected int getappTimeOut() {
		return appTimeOut;
	}

	protected void setappTimeOut(int timeOut) {
		this.appTimeOut = timeOut;
	}

	protected String getremoteUrl() {
		return this.remoteUrl;
	}

	protected void setremoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	protected void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	protected String getAppPackage() {
		return this.appPackage;
	}

	protected void setAppActivity(String appActivity) {
		this.appActivity = appActivity;
	}

	protected String getappActivity() {
		return this.appActivity;
	}

	protected void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	protected String getAppVersion() {
		return this.appVersion;
	}

	protected String getAPKName() {
		return this.APkFileName;
	}

	protected void setAPKName(String apkName) {
		this.APkFileName = apkName;
	}

	protected PropertyFileReader getHandler() {
		return handler;
	}

	protected void setHandler(PropertyFileReader handler) {
		this.handler = handler;
	}

	protected void setBrowserType(String BrowserType) {
		this.browserType = BrowserType;
	}

	protected String getBrowserType() {
		return this.browserType;
	}

	protected void setURL(String url) {
		this.url = url;
	}

	protected String getURL() {
		return this.url;
	}

	protected String runMode() {
		return this.runMode();
	}

	@SuppressWarnings("static-access")
	protected void setRunModule(String runModule) {
		this.runModule = runModule;
	}

	public static String getRunModule() {
		return runModule;
	}

	@SuppressWarnings("static-access")
	public void setRunMode(String runMode) {
		this.runMode = runMode;
	}

	@SuppressWarnings("static-access")
	public String getRunMode() {
		return this.runMode;
	}

	@SuppressWarnings("static-access")
	public void setENV(String env) {
		this.ENV = env;
	}

	public static String getENV() {
		return ENV;
	}

	public static String getDriverVersion() {
		return driverVersion;
	}

	@SuppressWarnings("static-access")
	public void setDriverVersion(String driverVersion) {
		this.driverVersion = driverVersion;
	}
	
	public static String getTVDeviceList() {
		return TVDeviceList;
	}
	
	@SuppressWarnings("static-access")
	public void setTVDeviceList(String TVDeviceList) {
		this.TVDeviceList = TVDeviceList;
	}
	
	public static String getENvironment() {
		return "<h5> ENV : <a href=\""+getENV()+"\" onclick='return "+click+";'\">"+getENV()+"</a></h5>";
	}

	public Drivertools(String application) {
		setHandler(new PropertyFileReader("properties/Execution.properties"));
		setHost(getHandler().getproperty("HOST_IP"));
		setPort(Integer.parseInt(getHandler().getproperty("HOST_PORT")));
		setappTimeOut(Integer.parseInt(getHandler().getproperty("APP_TIMEOUT")));
		setremoteUrl("http://" + getHost() + ":" + getPort() + "/wd/hub");

		setHandler(new PropertyFileReader("properties/AppPackageActivity.properties"));
		setAppPackage(getHandler().getproperty(application + "Package"));
		setAppActivity(getHandler().getproperty(application + "Activity"));
		setAppVersion(getHandler().getproperty(application + "Version"));
		setAPKName(getHandler().getproperty(application + "apkfile"));
		setDriverVersion(getHandler().getproperty("DriverVersion"));
	}

	{
		setPlatfrom(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getSuite().getName());
		setTestName(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName());
		setBrowserType(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browserType"));
		setURL(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("url"));
		setRunModule(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("runModule"));
		setRunMode(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("runMode"));
		if (getPlatform().equals("Android")) {
			setDeviceList(getListOfDevicesConnected().get(0).toString());
			if (getTestName().equals("Android_ChromeCast")) {
				setTVDeviceList(getListOfDevicesConnected().get(1).toString());
			}
		}
		
		if(getTestName().equals("Android_UserSessionManagement")) {
			setPlatfrom(Utilities.setPlatform);
		}else if(methodName.equals("TvLogin") || methodName.equals("setting") || methodName.equals("subscription") 
				|| methodName.equals("continueWatching") || methodName.equals("deviceAuthentication") || methodName.equals("club")
				|| methodName.equals("onboarding_MP") || methodName.equals("zeelogoVerificationInPlayer") 
				|| methodName.equals("authenticateInprint") || methodName.equals("premiumLiveChannel") 
				|| methodName.equals("premiumcontentFromInfo") || methodName.equals("rsvodplayback") 
				|| methodName.equals("loginPerformance") || methodName.equals("cleverTapValidation")||methodName.equals("profileselection")) {
			setPlatfrom("Web");
		}
		
		try {
			connectURL = new URL("https://www.google.com");
			connection = connectURL.openConnection();
			connection.connect();
			connection.getInputStream().close();
		} catch (IOException e1) {
			System.out.println("<<<<<<---- Network is Down  ---->>>>>>>");
			System.exit(0);
		}
		
		if (getPlatform().equals("Web")) {
			if (getURL().equals("https://newpwa.zee5.com/")) {
				setENV(getURL());
			} else if (getURL().equals("https://www.zee5.com/")) {
				setENV(getURL());
			}else if(getURL().equals("https://pwa-preprod2.zee5.com/")) {
				setENV(getURL());
			}
		} else if (getPlatform().equals("Android")) {
			setENV("Native App");
			setApk(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("Platform"));
			click = false;
		} else if (getPlatform().equals("MPWA")) {
			setENV("Chrome Application");
			click = false;
		}
		
		logger.info("PlatForm :: " + getPlatform());
		if (Stream.of("Android", "ios", "Web", "MPWA", "Android").anyMatch(getPlatform()::equals)) {
			setHandler(new PropertyFileReader("properties/ExecutionControl.properties"));
			if (getHandler().getproperty(getTestName()).equals("Y") && (getRunMode().contentEquals(getTestName()))
					|| (getRunMode().contentEquals("Suites"))) {
				logger.info("Running Test :: " + getTestName());
				logger.info("Run Mode :: YES");
				startTest = true;
			} else {
				logger.info(getTestName() + " : Test Skipped");
				logger.info("RunMode is :: No");
				startTest = false;
				throw new SkipException(getTestName() + " : Test Skipped ");
			}
		} else {
			throw new SkipException("PlatForm not matched...");
		}
	}
	
	
	public static ArrayList<String> getListOfDevicesConnected() {
		ArrayList<String> deviceID = new ArrayList<>();
		try {
			String cmd2 = "adb devices";
			Process p1 = Runtime.getRuntime().exec(cmd2);
			BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream()));
			String s = "";
			Thread.sleep(1000);
			while (!(s = br.readLine()).isEmpty()) {
				if (!s.equals("List of devices attached")) {
						deviceID.add(s.replaceAll("device", "").trim());
				}
			}
			return deviceID;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return deviceID;
	}
}
