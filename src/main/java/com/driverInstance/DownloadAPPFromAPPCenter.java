package com.driverInstance;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.asserts.SoftAssert;
import com.utility.LoggingUtils;
import com.utility.Utilities;


public class DownloadAPPFromAPPCenter extends Utilities{
	
	static LoggingUtils logger = new LoggingUtils();
	private SoftAssert softAssert = new SoftAssert();

//	public void AppCenter(String build,String buildVersion) throws Exception {
//		HeaderChildNode("App Center");
//		verifyElementPresentAndClick(AppCenterPage.objMicrosoft, "Microsoft");
//		type(AppCenterPage.objEmail, "ABC", "Email Field");
//		verifyElementPresentAndClick(AppCenterPage.objSignIn, "Next Icon");
//		waitTime(4000);
//		type(AppCenterPage.objPassword, "ABC", "Password Field");
//		verifyElementPresentAndClick(AppCenterPage.objSignIn, "Next Icon");
//		if (verifyElementPresent(AppCenterPage.objSignIn, "Next Icon")) {
//			verifyElementPresentAndClick(AppCenterPage.objSignIn, "Next Icon");
//		}
//		waitTime(4000);
//		if (verifyElementPresent(AppCenterPage.objMoreInformationRequiredPopUp, "More Information")) {
//			click(AppCenterPage.objCancelBtn, "Cancel");
//		}
//		waitTime(4000);
//		if (verifyElementPresent(AppCenterPage.objNoBtn, "No Icon")) {
//			click(AppCenterPage.objNoBtn, "No Icon");
//		}
//		
//		if(verifyElementPresent(AppCenterPage.objZee5AndroidHeader, "ZEE5 Android Header")) {
//			if(build.equals("latest")) {
//			click(AppCenterPage.objDownloadLatestReleaseBtn, "Dwonload button");
//			}else if(build.equals("BuildVersion")){
//				List<WebElement> version = findElements(AppCenterPage.objVersion);
//				List<WebElement> expend = findElements(AppCenterPage.objExpandMore);
//				int buildsVersion = version.size();
//				for (int i = 1; i < buildsVersion; i++) {
//					System.out.println(version.get(i).getText());
//					if(version.get(i).getText().contains(buildVersion)) {
//						expend.get(i).click();
//						verifyElementPresentAndClick(AppCenterPage.objDwonload,"Download button");
//						break;
//					}
//				}
//			}
//		}
//		waitTime(120000);
//	}
	
	public boolean verifyElementPresentAndClick(By byLocator, String validationtext) throws Exception {
		try {
			WebElement element = findElement(byLocator);
			softAssert.assertEquals(element.isDisplayed(), true, "" + validationtext + " " + " is displayed");
			logger.info("" + validationtext + " " + "is displayed");
			findElement(byLocator).click();
			logger.info("Clicked on " + validationtext);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			softAssert.assertEquals(false, true, "Element" + validationtext + " " + " is not visible");
			logger.error("Element " + validationtext + " " + " is not visible");
			screencapture();
//			softAssert.assertAll();
			return false;
		}
	}
	
	public void type(By byLocator, String input, String FieldName) {
		try {
			waitTime(1000);
			if (!getPlatform().equals("Web")) {
				Actions a = new Actions(getDriver());
				a.sendKeys(input);
				a.perform();
			} else {
				WebElement element = findElement(byLocator);
				element.sendKeys(input);
			}
			input = input.split("\n")[0];
			logger.info("Typed the value " + input + " into " + FieldName);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public void click(By byLocator, String validationtext) throws Exception {
		try {
			WebElement element = findElement(byLocator);
			element.click();
			logger.info("Clicked on " + validationtext);
		} catch (Exception e) {
			screencapture();
		}
	}
	
	public boolean verifyElementPresent(By byLocator, String validationtext) throws Exception {

		try {
			WebElement element = findElement(byLocator);
			softAssert.assertEquals(element.isDisplayed(), true, "" + validationtext + " " + " is displayed");
			logger.info(validationtext + " is displayed");
			return true;
		} catch (Exception e) {
			softAssert.assertEquals(false, true, validationtext + " " + " is not displayed");
//			softAssert.assertAll();
			logger.error(validationtext + " is not displayed");
			return false;
		}
	}
	
	
}
