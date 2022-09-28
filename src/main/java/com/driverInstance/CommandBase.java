package com.driverInstance;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import com.extent.ExtentReporter;
import com.propertyfilereader.PropertyFileReader;
import com.utility.LoggingUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;

public class CommandBase extends DriverInstance {
	
	/** Time out */
	private int timeout;

	/** Retry Count */
	private int retryCount;

	ExtentReporter extent = new ExtentReporter();

	/** The Constant logger. */
//	final static Logger logger = Logger.getLogger("rootLogger");
	static LoggingUtils logger = new LoggingUtils();

	/** The Android driver. */
	public AndroidDriver<AndroidElement> androidDriver;
	
	/** The Android driver. */
	public IOSDriver<WebElement> iOSDriver;
	

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public CommandBase(String Application)  {
		super(Application);
		init();
	}
	

	public void init() {

		PropertyFileReader handler = new PropertyFileReader("properties/Execution.properties");
		setTimeout(Integer.parseInt(handler.getproperty("TIMEOUT")));
		setRetryCount(Integer.parseInt(handler.getproperty("RETRY_COUNT")));
	}
	
	public static void clearAppData(String packagename) {
        logger.info("****Clearing the App Data****");
        String cmd2 = "adb shell pm clear " + packagename;
        try {
            Runtime.getRuntime().exec(cmd2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
