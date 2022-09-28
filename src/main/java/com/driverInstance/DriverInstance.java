package com.driverInstance;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.simple.parser.ParseException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;
import org.testng.SkipException;
import com.propertyfilereader.PropertyFileReader;
import com.utility.Utilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class DriverInstance extends Drivertools {

	@SuppressWarnings("static-access")
	public DriverInstance(String Application) {
		super(Application);
		try {
			switch (getPlatform()) {
//			case "Android":
//				if (getTestName().equals("Android_ChromeCast")) {
//				} else {
//					tlDriver.set((AppiumDriver<WebElement>) new AndroidDriver<WebElement>(new URL(getremoteUrl()),this.generateAndroidCapabilities(Application)));
//					util.waitForElementDisplayed(AMDOnboardingScreen.objWaitForSplashScreenDisapear, 240);
//					Instant endTime = Instant.now();
//					timeElapsed = Duration.between(startTime, endTime);
//					logger.info("Time taken to launch the App (millisec)" + timeElapsed.toMillis());
//				}
//				extent.extentLogger("Timer","to the App (millisec): " + timeElapsed.toMillis());
//				File file = new File(dir+apkName);
//				file.delete();
//				break;

			case "MPWA":
				tlDriver.set(((AppiumDriver<WebElement>) new AndroidDriver<WebElement>(new URL(getremoteUrl()),
						this.generateAndroidCapabilities(Application))));
				tlDriver.get().get(getURL());
//				tlDriver.set(EventFiringWebDriverFactory.getEventFiringWebDriver(tlDriver.get(), new AppiumEventListener()));
				break;

			case "Web":
				LaunchBrowser(getBrowserType());
				break;
				
			case "Android":
				tlDriver.set((AppiumDriver<WebElement>) new AndroidDriver<WebElement>(new URL(getremoteUrl()),
						this.generateAndroidCapabilities(Application)));
				break;

			default:
				throw new SkipException("Incorrect Platform...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SkipException("Device not connected OR Appium Studio service is down...");
		}
		
		Utilities util = new Utilities();
		util.initDriver();
	}

	/**
	 * @param application
	 * @return Android capabilities
	 * @throws Exception
	 */
	protected DesiredCapabilities generateAndroidCapabilities(String application) {
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);
//		capabilities.setCapability("compressXml", "true");
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
//		4f9e8c63
		capabilities.setCapability(MobileCapabilityType.NO_RESET, false);
		capabilities.setCapability("fullReset", false);
		capabilities.setCapability("autoAcceptAlerts", true);
		if (getPlatform().equals("MPWA")) {
			capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
			return capabilities;
		}
//		logger.info("APK INSTALLED..");
//		String buildChoice = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("Build");
//		installAPK(buildChoice);
		capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, getAppPackage());
		capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, getappActivity());
		if (Utilities.relaunch) {
			removePermisson(getAppPackage());
		}
		startTime = Instant.now();
		return capabilities;
	}
	
	@SuppressWarnings("unused")
	private void installAPK(String build) {
		if(build.equals("Latest") || build.equals("BuildVersion")) {
//		DownloadApp(build);
		System.out.println("Finished download");
		System.out.println(dir);
		File file = new File(dir);
		file.mkdir();
		File filesList[] = file.listFiles();
		 for(File fileName : filesList) {
			 apkName = fileName.getName();
		 }
		 capabilities.setCapability(MobileCapabilityType.APP, dir+apkName);
		 System.out.println("Install APK");
		switch(getApk()) {
		case "CleverTap":
			capabilities.setCapability(MobileCapabilityType.APP, dir+apkName);
			break;
		case "AppsFlyer":
//			capabilities.setCapability(MobileCapabilityType.APP, dir+"");
			break;
		case "Conviva":
//			capabilities.setCapability(MobileCapabilityType.APP, dir+"");
			break;
		case "DFP":
			capabilities.setCapability(MobileCapabilityType.APP, dir+"DFP.apk");
			break;
		case "Mixpanel":
			capabilities.setCapability(MobileCapabilityType.APP, dir+"mixpanel.apk");
			break;
		}
	  }
	}

	/**
	 * Function to Launch Web Browsers
	 * 	
	 * @param browserName
	 */
	public void LaunchBrowser(String browserName) {
		setHandler(new PropertyFileReader("properties/AppPackageActivity.properties"));
		if (browserName.equalsIgnoreCase("Firefox")) {
			WebDriverManager.firefoxdriver().version("0.27.0").setup();
			tlWebDriver.set(new FirefoxDriver());
		} else if (browserName.equalsIgnoreCase("Chrome")) {
			WebDriverManager.chromedriver().version(getDriverVersion()).setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("start-maximized");
			options.addArguments("enable-automation");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-infobars");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--disable-browser-side-navigation");
			options.addArguments("--disable-gpu");
//			options.addArguments("--headless");
//			options.addArguments("--start-maximized");
//			options.addArguments("--window-size=1616, 876");
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);
			tlWebDriver.set(new ChromeDriver(options));
		}else if (browserName.equalsIgnoreCase("ChromeNormal")) {
			WebDriverManager.chromedriver().version(getDriverVersion()).setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("start-maximized");
			options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			tlWebDriver.set(new ChromeDriver(options));
		} 
		else if (browserName.equalsIgnoreCase("IE")) {
			tlWebDriver.set(new InternetExplorerDriver());
		}
		else if (browserName.equalsIgnoreCase("MSEdge")) {
			tlWebDriver.set(new EdgeDriver());
		}
		tlWebDriver.get().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		tlWebDriver.get().get(getURL());
		tlWebDriver.get().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	/**
	 * To Remove the permission of an application
	 * 
	 * @param packagename
	 */
	public static void removePermisson(String packagename)
	{
		logger.info("****Clearing the App Data****");
		String cmd2 = "adb -s "+getDeviceList()+" shell pm clear " + packagename;
		try {
			Runtime.getRuntime().exec(cmd2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public static void DownloadApp(String build) {
//		DriverInstance.setPlatfrom("Web");
//		File file = new File(System.getProperty("user.dir") + File.separator + "Apk");
//		file.mkdir();
//		WebDriverManager.chromedriver().version(getDriverVersion()).setup();
//	    Map<String, Object> prefs = new HashMap<String, Object>();
//	    prefs.put("download.default_directory",System.getProperty("user.dir") + File.separator + "Apk");
//	    ChromeOptions options = new ChromeOptions();
//	    options.setExperimentalOption("prefs", prefs);
//	    tlWebDriver.set(new ChromeDriver(options));
//	    tlWebDriver.get().get("https://install.appcenter.ms/sign-in?original_url=install:/%2Forgs%2FZee5-Mobile%2Fapps%2FZee5-Android");
//	    Utilities util = new Utilities();
//		util.initDriver();
//		try {
//		DownloadAPPFromAPPCenter DAFAC = new DownloadAPPFromAPPCenter();
//		String buildversion = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("BuildVersion");
//		DAFAC.AppCenter(build,buildversion);
//		tlWebDriver.get().quit();
//		DriverInstance.setPlatfrom("Android");
//		}catch(Exception e) {
//			
//		}
//	}
	
	public void chromeCastInitDriver() throws MalformedURLException, ParseException {
		 tlDriver.set((AppiumDriver<WebElement>) new AndroidDriver<WebElement>(new URL(getremoteUrl()),
				 generateAndroidChromeCastCapabilities("Zee")));
		
		driverTV.set((AppiumDriver<WebElement>) new AndroidDriver<WebElement>(new URL(getremoteUrl()),
				 generateAndroidTvChromeCastCapabilities("zeeTV")));
	}
	
	public DesiredCapabilities generateAndroidChromeCastCapabilities(String application) {
		System.out.println("Capability");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
		capabilities.setCapability("udid", getDeviceList());
		capabilities.setCapability("platformVersion", "10");
		capabilities.setCapability("fullReset", false);
		capabilities.setCapability("autoAcceptAlerts", true);
		capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, getAppPackage());
		capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, getappActivity());
		return capabilities;
	}
	
	public DesiredCapabilities generateAndroidTvChromeCastCapabilities(String application) {
		System.out.println("Capability");
	
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);
//		capabilities.setCapability("compressXml", "true");
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
		capabilities.setCapability("udid", getTVDeviceList());
		capabilities.setCapability("platformVersion", "10");
		capabilities.setCapability("fullReset", false);
		capabilities.setCapability("autoAcceptAlerts", true);
		capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.graymatrix.did");
		capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,"com.zee5.player.activities.SplashActivity");
		return capabilities;
	}
}