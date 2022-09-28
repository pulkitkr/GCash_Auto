package com.business.gCASH;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.Datasheet.GCashAPI_TestData_DataProvider;
import com.android.GCASHPages.GCash_GSave;
import com.android.GCASHPages.GGivesDues;
import com.android.GCASHPages.GGivesHomePage;
import com.android.GCASHPages.GGivesLoginPage;
import com.android.GCASHPages.GGivesPaymentPage;
import com.android.GCASHPages.GGivesViewAll;
import com.driverInstance.CommandBase;
import com.extent.ExtentReporter;
import com.mixpanelValidation.Mixpanel;
import com.propertyfilereader.PropertyFileReader;
import com.utility.LoggingUtils;
import com.utility.Utilities;

import ch.qos.logback.classic.Logger;
import org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class GCASHBusinessLogic extends Utilities {

	public GCASHBusinessLogic(String Application) throws InterruptedException {
		new CommandBase(Application);
		init();
	}

	private String amtText;
	private String loanAccountNo;
	private String availableAmt;
	private String loanAccNo;
	private String youAreAboutToPay;
	private String enterAmount;
	private String totalSavingBalance;
	private String profileNo;

	private int timeout;
	SoftAssert softAssertion = new SoftAssert();
	boolean launch = "" != null;
	/** Retry Count */
	private int retryCount;
	ExtentReporter extent = new ExtentReporter();

	/** The Constant logger. */

	static LoggingUtils logger = new LoggingUtils();

	/** The Android driver. */
	public AndroidDriver<AndroidElement> androidDriver;

	/** The Android driver. */
	public IOSDriver<WebElement> iOSDriver;

	/**
	 * Initiate Property File.
	 *
	 * @param byLocator the by locator
	 */

	public void init() {

		PropertyFileReader handler = new PropertyFileReader("properties/Execution.properties");
		setTimeout(Integer.parseInt(handler.getproperty("TIMEOUT")));
		setRetryCount(Integer.parseInt(handler.getproperty("RETRY_COUNT")));
		logger.info(
				"Loaded the following properties" + " TimeOut :" + getTimeout() + " RetryCount :" + getRetryCount());
	}

	public void tearDown() {
		getDriver().quit();
	}

	/**
	 * @handle Popups method
	 * @param userType
	 * @throws Exception
	 */
	public void GGivesAppLaunch(String userType) throws Exception {
		extent.HeaderChildNode("Request Permission");
		explicitWaitVisibility(GGivesLoginPage.objRequestPermissionPopup, 10);
		if (verifyElementPresent(GGivesLoginPage.objRequestPermissionPopup, "Request Permission Popup")) {
			click(GGivesLoginPage.objRequestPermissionOkBtn, "Ok Button");

			verifyElementPresent(GGivesLoginPage.objPicturePopup, "GCash Picture Popup");
			click(GGivesLoginPage.objPictureDontAllowBtn, "Don't Allow Button");

			verifyElementPresent(GGivesLoginPage.objLocationPopup, "GCash Location access Popup");
			click(GGivesLoginPage.objLocationDontAllowBtn, "Don't Allow Button");

			verifyElementPresent(GGivesLoginPage.objContactPopup, "GCash contact access Popup");
			click(GGivesLoginPage.objContactDontAllowBtn, "Don't Allow Button");

			verifyElementPresent(GGivesLoginPage.objPhotoPopup, "GCash Photo media access Popup");
			click(GGivesLoginPage.objPhotoDontAllowBtn, "Don't Allow Button");

			verifyElementPresent(GGivesLoginPage.objWelcomePageVerify, "Welcome to GCash Page");
			logger.info("Navigated to Welcome to GCash Page");
			extent.extentLoggerPass("Welcome Page", "Navigated to Welcome to GCash Page");

		} else {
			logger.info("Navigated to Welcome to GCash Page");
			extent.extentLogger("Welcome Page", "Navigated to Welcome to GCash Page");
		}
	}

	/**
	 * @login to application method
	 * @param InvalidMobileNo
	 * @param vailidNo
	 * @throws Exception
	 */
	public void loginToGCash(String InvalidMobileNo, String validNo, String validOTP, String invalidOTP)
			throws Exception {
		extent.HeaderChildNode("Login");

		if (verifyElementDisplayed(GGivesLoginPage.objWelcomePageVerify)) {
			String welcometoGCashPage = getText(GGivesLoginPage.objWelcomePageVerify);
			Assert.assertEquals("Welcome to GCash", welcometoGCashPage);
			click(GGivesLoginPage.objLoginBtn, "Login Button");
			logger.info("Navigated to GCash  Login Page");
			extent.extentLoggerPass("Login Page", "Navigated to GCash  Login Page");

			if (verifyElementDisplayed(GGivesLoginPage.objGCashLogo)) {
				type(GGivesLoginPage.objMobileNumberField, InvalidMobileNo, "Mobile Number text Field");
				click(GGivesLoginPage.objNextBtn, "Next Button");

				if (verifyElementDisplayed(GGivesLoginPage.objWrongPopup)) {
					int wrongCount = 1;
					for (int i = 0; i < wrongCount; i++) {
						click(GGivesLoginPage.objLaterBtns, "Later Button");
						click(GGivesLoginPage.objNextBtn, "Next Button");
						if (verifyElementDisplayed(GGivesLoginPage.objWrongPopup)) {
							wrongCount++;
						} else {
							break;
						}
					}
				}

				logger.info("Navigated to OTP Authentication Page");
				extent.extentLoggerPass("OTP Authentication Page", "Navigated to GCash OTP Authentication Page");
			} else {
				logger.info("Failed to Navigate to OTP Authentication Page");
				extent.extentLoggerFail("OTP Authentication Page",
						"Failed to Navigate to GCash OTP Authentication Page");
			}
			explicitWaitVisibility(GGivesLoginPage.objAuthenticationPageVerify, 20);
			String authenticationPage = getText(GGivesLoginPage.objAuthenticationPageVerify);
			verifyElementPresent(GGivesLoginPage.objAuthenticationPageVerify, authenticationPage + " Page");
			Assert.assertEquals("Authentication", authenticationPage);
			type(GGivesLoginPage.objOTPField, validOTP, "OTP Field");
			click(GGivesLoginPage.objSubmitBtn, "Submit Button");

			if (verifyElementPresent(GGivesLoginPage.objRequestAlertPermissionPopup, "Request permission Popup")) {
				click(GGivesLoginPage.objAlertOkBtn, "OK Button");
				verifyElementPresent(GGivesLoginPage.objLocationPopup, "GCash Location access Popup");
				click(GGivesLoginPage.objRequestLocationDontAllowBtn, "Don't Allow Button");

				logger.info("Navigated to MPIN page");
				extent.extentLoggerPass("MPIN Page", "Navigated to MPIN page");
			} else {
				logger.info("No popup is displayed");
				extent.extentLoggerPass("Popup", "No popup is displayed");
			}

			String MPINPage = getText(GGivesLoginPage.objMpinPageVerify);
			verifyElementPresent(GGivesLoginPage.objMpinPageVerify, MPINPage + " Page");
			Assert.assertEquals("Enter your MPIN", MPINPage);
			click(GGivesLoginPage.objOneBtn, "1");
			click(GGivesLoginPage.objTwoBtn, "2");
			click(GGivesLoginPage.objOneBtn, "1");
			click(GGivesLoginPage.objTwoBtn, "2");

			if (verifyElementDisplayed(GGivesLoginPage.objMPINPopup)) {
				click(GGivesLoginPage.objLaterBtn, "Later Button");
				click(GGivesLoginPage.objChangeNumber, "Change Number");

				verifyElementPresent(GGivesLoginPage.objOopsPopup, "Oops! popup");
				click(GGivesLoginPage.objYesBtn, "Yes Button");
				logger.info("Navigated to Enter Mobile Number Page");
				extent.extentLoggerPass("Mobile Number Page", "Navigated to Enter Mobile Number Page");

			} else if (verifyElementPresent(GGivesLoginPage.objMpinpopupVerify, "Mpin Incorrect popup")) {
				click(GGivesLoginPage.objMpinpopupOKBtn, "OK Button");
				click(GGivesLoginPage.objChangeNumber, "Change Number");
				verifyElementPresent(GGivesLoginPage.objOopsPopup, "Oops! popup");
				click(GGivesLoginPage.objYesBtn, "Yes Button");
				logger.info("Navigated to Enter Mobile Number Page");
				extent.extentLoggerPass("Mobile Number Page", "Navigated to Enter Mobile Number Page");
			} else {
				logger.info("Failed to Navigate to Enter Mobile Number Page");
				extent.extentLogger("Mobile Number Page", "Failed to Navigate to Enter Mobile Number Page");
			}

			verifyElementDisplayed(GGivesLoginPage.objGCashLogo);
			type(GGivesLoginPage.objMobileNumberField, validNo, "Mobile Number text Field");

			click(GGivesLoginPage.objNextBtn, "Next Button");
			logger.info("Navigated to OTP Authentication Page");
			extent.extentLoggerPass("OTP Authentication Page", "Navigated to GCash OTP Authentication Page");

			explicitWaitVisibility(GGivesLoginPage.objAuthenticationPageVerify, 20);
			verifyElementPresent(GGivesLoginPage.objAuthenticationPageVerify, "OTP Authentication Page");
			type(GGivesLoginPage.objOTPField, invalidOTP, "OTP Field");
			click(GGivesLoginPage.objSubmitBtn, "Submit Button");

			if (verifyElementPresent(GGivesLoginPage.objInvalidPopupVerify, "Invalid Authentication Code Popup")) {
				click(GGivesLoginPage.objOKBtn, "OK Button");
				androidClearText(GGivesLoginPage.objOTPField, "OTP Field");
				waitTime(3000);
				type(GGivesLoginPage.objOTPField, validOTP, "OTP Field");
				waitTime(3000);
				click(GGivesLoginPage.objSubmitBtn, "Submit Button");
				logger.info("Navigated to MPIN Page");
				extent.extentLoggerPass("MPIN Page", "Navigated to MPIN Page");
			} else {
				logger.info("Failed to Navigate to MPIN Page");
				extent.extentLogger("MPIN Page", "Failed to Navigate to MPIN Page");
			}

			if (verifyElementPresent(GGivesLoginPage.objMpinPageVerify, MPINPage + " Page")) {
				Assert.assertEquals("Enter your MPIN", MPINPage);
				click(GGivesLoginPage.objOneBtn, "1");
				click(GGivesLoginPage.objTwoBtn, "2");
				click(GGivesLoginPage.objOneBtn, "1");
				click(GGivesLoginPage.objThreeBtn, "3");

				explicitWaitVisibility(GGivesLoginPage.objMpinpopupVerify, 20);
				if (verifyElementPresent(GGivesLoginPage.objMpinpopupVerify, "Mpin Incorrect popup")) {
					click(GGivesLoginPage.objMpinpopupOKBtn, "OK Button");
					click(GGivesLoginPage.objOneBtn, "1");
					click(GGivesLoginPage.objTwoBtn, "2");
					click(GGivesLoginPage.objOneBtn, "1");
					click(GGivesLoginPage.objTwoBtn, "2");
					logger.info("Navigated to Home Page");
					extent.extentLoggerPass("HomePage", "Navigated to Home Page");
				} else {
					logger.info("Failed to navigate to welcome page");
					extent.extentLoggerFail("Welcome Page", "Failed to navigate to welcome page");
				}
			}
		}
	}

	/**
	 * @Homepage methods
	 * @throws Exception
	 */
	public void homePage(String userType) throws Exception {
		extent.HeaderChildNode("Home Page");

		if (verifyElementPresent(GGivesHomePage.objDiscoverPopup, "Discover popup")) {
			click(GGivesHomePage.objDiscoverBtn, "Discover Button");

			suggestionTapMidScreen(GGivesHomePage.objTourMsg);

			String homeHeaderText = getText(GGivesHomePage.objHomePageHeader);
			Assert.assertEquals(homeHeaderText, "Hello!");

			verifyElementPresentAndClick(GGivesHomePage.objViewAllIcon, "View All Icon");
			explicitWaitVisibility(GGivesHomePage.objViewAllDiscoverPopup, 10);
			if (verifyElementPresent(GGivesHomePage.objViewAllDiscoverPopup, "Discover Popup")) {
				click(GGivesHomePage.objDiscoverBtn, "Discover Button");
				logger.info("Navigated to View All Page");
				extent.extentLoggerPass("View All", "Navigated to View All Page");
			} else {
				logger.info("Failed to Navigate to Vew All Page");
				extent.extentLogger("View All", "Failed to Navigate to View All Page");
			}
		}
	}

	/**
	 * @GGivesViewAll methods
	 * @throws Exception
	 */
	public void ggivesViewAll() throws Exception {
		extent.HeaderChildNode("View All");

		if (verifyElementDisplayed(GGivesViewAll.objGgivesViewAll)) {
			String viewAllTextHeader = getText(GGivesViewAll.objGgivesViewAll);
			Assert.assertEquals(viewAllTextHeader, "View All");

			Swipe("up", 1);
			verifyElementPresentAndClick(GGivesViewAll.objGrowGGivesModule, "GGives module");
			logger.info("Navigated to GGives Page");
			extent.extentLoggerPass("GGives", "Navigated to GGives Page");
		} else {
			logger.info("Failed to navigate to View All page");
			extent.extentLoggerFail("View All", "Failed to navigate to View All page");
		}
	}

	/**
	 * @GGives Dashboard methods
	 * @param repaymentAmt
	 * @throws Exception
	 */

	public void ggivesDashboard(String repaymentAmt) throws Exception {
		extent.HeaderChildNode("GGives");

		if (verifyElementPresent(GGivesViewAll.objGGivesDashboardPopup, "Welcome to GGives Dashboard Popup")) {
			click(GGivesViewAll.objGGivesDashboardpopupOkBtn, "OK Button");

			suggestionTapMidScreen(GGivesViewAll.objGuideSuggestion);

			String loanAccountNumber = getText(GGivesViewAll.objLoanAccountNumber);
			logger.info("LOAN ACCOUNT NO. " + loanAccountNumber);
			extent.extentLogger("Loan Account no", "LOAN ACCOUNT NO. " + loanAccountNumber);

			String interestRate = getText(GGivesViewAll.objInterestRate);
			logger.info("Interest Rate " + interestRate);
			extent.extentLogger("Interest rate", "INTEREST RATE " + interestRate);

			String dueAmount = getText(GGivesViewAll.objDueAmount);
			String[] splits = dueAmount.split(",");
			String balanceBeforeCommas = splits[0];
			String afterCommas = splits[1];
			
			String dueBalance = balanceBeforeCommas + afterCommas;
			float dueBalanceToFloat = Float.parseFloat(dueBalance);
			String dueBalanceToString = Float.toString(dueBalanceToFloat);
			float dueAmt = Float.parseFloat(dueBalanceToString);
			
			
			click(GGivesViewAll.objPayForGivesBtn, "Pay For GGives Button");

			if (verifyElementPresent(GGivesViewAll.objEnterAmtPopup, "Paano bayaran ang GGives? Popup")) {
				click(GGivesViewAll.objEnterAmtPopupOkBtn, "OK Button");
			}

			suggestionTapMidScreen(GGivesViewAll.objGuideSuggestion);

			String getLoanAmtText = getText(GGivesViewAll.objGCashBal);

			getLoanAmtText = getLoanAmtText.substring(18);
			String[] split = getLoanAmtText.split(",");
			String balanceBeforeComma = split[0];
			String afterComma = split[1];

			String amountBalance = balanceBeforeComma + afterComma;
			float amoutBalanceToFloat = Float.parseFloat(amountBalance);
			amoutBalanceToFloat = amoutBalanceToFloat + 100;
			String amoutBalanceToString = Float.toString(amoutBalanceToFloat);

			click(GGivesViewAll.objEnterAmt, "Enter Amount Text Field");
			type(GGivesViewAll.objEnterAmt, amoutBalanceToString, "Amount Field");

			explicitWaitVisibility(GGivesViewAll.objGcashErrorMsg, 10);
			String outstatndingBalanceErrorMsg = getText(GGivesViewAll.objGcashErrorMsg);
			if (verifyElementPresent(GGivesViewAll.objGcashErrorMsg, outstatndingBalanceErrorMsg)) {
				click(GGivesViewAll.objEnterAmt, "Enter Amount Text Field");
				androidClearText(GGivesViewAll.objEnterAmt, "Enter Amount Text Field");
				waitTime(2000);
				type(GGivesViewAll.objEnterAmt, repaymentAmt, "Amount Field");
				amtText = getText(GGivesViewAll.objEnterAmt);
			}

			availableAmt = getText(GGivesViewAll.objGcashAvailableBalance);
			availableAmt = availableAmt.substring(availableAmt.lastIndexOf(":") + 2);

			if (dueAmt > 0) {
				click(GGivesViewAll.objPaymentDueOptionBtn, "Pay amount due level");
				androidClearText(GGivesViewAll.objEnterAmt, "Enter Amount Text Field");
				type(GGivesViewAll.objEnterAmt, repaymentAmt, "Amount Field");
			} else {
				click(GGivesViewAll.objPayInGives, "Pay in Gives(half of due) level");
				verifyElementPresentAndClick(GGivesViewAll.objPayInGivesPopup, "Pay in Gives. Popup");
			}

			hideKeyboard();
			explicitWaitVisibility(GGivesViewAll.objLoanACNextBtn, 10);
			click(GGivesViewAll.objLoanACNextBtn, "NEXT Button");
		}
	}

	/**
	 * @GGives Dues method
	 * @throws Exception
	 */
	public void gGivesDues() throws Exception {
		extent.HeaderChildNode("GGives Dues");

		if (verifyElementDisplayed(GGivesDues.objgGivesDuesBanner)) {
			logger.info("Navigated to GGives Dues payment Page");
			extent.extentLoggerPass("GGives Dues", "Navigated to GGives Dues Payment Page");

			String availableGcashBalance = getText(GGivesDues.objAvailableGcashBal);
			Assert.assertEquals(availableAmt, availableGcashBalance);
			logger.info("GCash Avaiable Balance " + availableGcashBalance);
			extent.extentLoggerPass("GCash Balance", "GCash Avaiable Balance " + availableGcashBalance);

			youAreAboutToPay = getText(GGivesDues.objTotalAmt);
			youAreAboutToPay = youAreAboutToPay.substring(4);
			Assert.assertEquals(amtText, youAreAboutToPay);
			logger.info("Total Amount " + youAreAboutToPay);
			extent.extentLoggerPass("Total Amount", "Total Amount " + youAreAboutToPay);

			click(GGivesDues.objPayPhpBtn, "PAY " + youAreAboutToPay);
		} else {
			logger.info("Failed to navigate to GGives Due Page");
			extent.extentLoggerFail("Due Page", "Failed to navigate to GGives Due Page");
		}
	}

	/**
	 * @GGives Payment Success method
	 * @throws Exception
	 */
	public void gGivesPaymentSuccess() throws Exception {
		extent.HeaderChildNode("GGives Payment Message");

		if (verifyElementPresent(GGivesPaymentPage.objGGivesPaymentHeader, "GGives Payment")) {
			String gGivesheaderText = getText(GGivesPaymentPage.objGGivesPaymentHeader);
			Assert.assertEquals(gGivesheaderText, "GGives Payment");

			String refText = getText(GGivesPaymentPage.objGGivesReferenceText);
			String refNo = getText(GGivesPaymentPage.objGGivesReferenceNo);
			logger.info(refText + " is " + refNo);
			extent.extentLogger(refNo, refText + " is " + refNo);

			String successfullPaidTet = getText(GGivesPaymentPage.objGGivesSuccessfulPaidText);
			String successfullPaidForGGivesTitle = getText(GGivesPaymentPage.objPaidForGGivesTitle);

			logger.info(successfullPaidTet + " " + successfullPaidForGGivesTitle);
			extent.extentLogger("Success Msg", successfullPaidTet + " " + successfullPaidForGGivesTitle);

			String amountPaid = getText(GGivesPaymentPage.objAmountPaid);
			Assert.assertEquals(amountPaid, youAreAboutToPay);
			logger.info("Total Amount Paid is " + amountPaid);
			extent.extentLogger("Amount Paid", "Total Amount Paid is " + "PHP " + amountPaid);

			String paidDateTime = getText(GGivesPaymentPage.objAmountPaidDateTime);
			logger.info("Payment Date and Time is " + paidDateTime);
			extent.extentLogger("Date Time", "Payment Date and Time is " + paidDateTime);

			String amountPAidTimeTaken = getText(GGivesPaymentPage.objAmountPaidTimeTakenMsg);
			logger.info(amountPAidTimeTaken);
			extent.extentLogger("Time Taken For Payment", amountPAidTimeTaken);

			click(GGivesPaymentPage.objCrossBtn, "Cross Button");

		} else {
			logger.info("Failed to navigate to GGives Payment Page");
			extent.extentLoggerFail("GGives Payment", "Failed to navigate to GGives Payment Page");
		}
	}

	/**
	 * @GGives Logout method
	 * @throws Exception
	 */
	public void gCashLogout() throws Exception {
		extent.HeaderChildNode("logout");

		if (verifyElementPresent(GGivesHomePage.objHomePageHeader, "Hello! HomePage")) {
			String homeHeaderText = getText(GGivesHomePage.objHomePageHeader);
			Assert.assertEquals(homeHeaderText, "Hello!");
			logger.info("Navigated to HomePage");
			extent.extentLoggerPass("HomePage", "Navigated to HomePage");
			click(GCash_GSave.objProfileIcon, "Profile Icon");

			String profileNum = getText(GCash_GSave.objPhoneNo);
			profileNum = profileNum.substring(1);
			logger.info("Registered Mobile No " + profileNum);
			extent.extentLoggerPass("Register No", "Registered Mobile No " + profileNum);
			Swipe("up", 1);
			click(GCash_GSave.objLogout, "Log Out");

			if (verifyElementPresent(GCash_GSave.objLogoutPopup, "Log out Popup Message")) {
				click(GCash_GSave.objOkBtns, "OK Button");
			}
		} else {
			logger.info("Failed to navigate to HomePage");
			extent.extentLoggerFail("HomePage", "Failed to navigate to HomePage");
		}
	}

	/**
	 * GSave Login method
	 * 
	 * @param validNo
	 * @throws Exception
	 */
	public void GsaveLogin(String validNo, String GSaveValidOTP) throws Exception {
		extent.HeaderChildNode("GSave");

		if (verifyElementPresent(GGivesLoginPage.objMpinPageVerify, "MPIN Page")) {
			String MPINPage = getText(GGivesLoginPage.objMpinPageVerify);
			Assert.assertEquals("Enter your MPIN", MPINPage);
			click(GGivesLoginPage.objChangeNumber, "Change Number");
			verifyElementPresent(GGivesLoginPage.objOopsPopup, "Oops! popup");
			click(GGivesLoginPage.objYesBtn, "Yes Button");
			logger.info("Navigated to Enter Mobile Number Page");
			extent.extentLoggerPass("Mobile Number Page", "Navigated to Enter Mobile Number Page");

			verifyElementDisplayed(GGivesLoginPage.objGCashLogo);
			type(GGivesLoginPage.objMobileNumberField, validNo, "Mobile Number text Field");
			profileNo = getText(GGivesLoginPage.objMobileNumberField);

			click(GGivesLoginPage.objNextBtn, "Next Button");
			logger.info("Navigated to OTP Authentication Page");
			extent.extentLoggerPass("OTP Authentication Page", "Navigated to GCash OTP Authentication Page");

			explicitWaitVisibility(GGivesLoginPage.objAuthenticationPageVerify, 20);
			if (verifyElementPresent(GGivesLoginPage.objAuthenticationPageVerify, "OTP Authentication Page")) {
				type(GGivesLoginPage.objOTPField, GSaveValidOTP, "OTP Field");
				click(GGivesLoginPage.objSubmitBtn, "Submit Button");
				logger.info("Navigated to MPIN Page");
				extent.extentLoggerPass("MPIN Page", "Navigated to MPIN Page");
			} else {
				logger.info("Failed to Navigate to MPIN Page");
				extent.extentLogger("MPIN Page", "Failed to Navigate to MPIN Page");
			}

			if (verifyElementPresent(GGivesLoginPage.objMpinPageVerify, "MPIN Page")) {
				String MPINPages = getText(GGivesLoginPage.objMpinPageVerify);
				Assert.assertEquals("Enter your MPIN", MPINPages);
				click(GGivesLoginPage.objOneBtn, "1");
				click(GGivesLoginPage.objTwoBtn, "2");
				click(GGivesLoginPage.objOneBtn, "1");
				click(GGivesLoginPage.objTwoBtn, "2");
			}
			if (verifyElementPresent(GGivesHomePage.objDiscoverPopup, "Discover popup")) {
				logger.info("Navigated to Home Page");
				extent.extentLoggerPass("HomePage", "Navigated to Home Page");
				click(GGivesHomePage.objDiscoverBtn, "Discover Button");

				suggestionTapMidScreen(GGivesHomePage.objTourMsg);

				String homeHeaderText = getText(GGivesHomePage.objHomePageHeader);
				Assert.assertEquals(homeHeaderText, "Hello!");
				click(GCash_GSave.objGsaveTab, "GSave Tab");
			} else {
				logger.info("Failed to Navigate to Home Page");
				extent.extentLoggerFail("HomePage", "Failed to Navigate to Home Page");
			}
		} else {
			logger.info("Failed to Navigate to Enter Mobile Number Page");
			extent.extentLoggerFail("Mobile Number Page", "Failed to Navigate to Enter Mobile Number Page");
		}
	}

	/**
	 * @GSave Transaction method
	 * @throws Exception
	 */
	public void GsaveTransaction(String amtPay) throws Exception {
		extent.HeaderChildNode("GSave Transaction");

		explicitWaitVisibility(GCash_GSave.objWelcomePage, 20);
		if (verifyIsElementDisplayed(GCash_GSave.objWelcomePage, "Welcome to Gsave Marketplace!")) {

			for (int i = 0; i < 3; i++) {
				explicitWaitVisibility(GCash_GSave.objNextBtn, 10);
				verifyElementPresentAndClick(GCash_GSave.objNextBtn, "Next Button");
			}
			suggestionTapMidScreen(GCash_GSave.objOurPartnerBankTour);
			logger.info("Navigated to Gsave MarketPlace Page");
			extent.extentLoggerPass("Gsave MarketPlace", "Navigated to Gsave MarketPlace Page");

			verifyIsElementDisplayed(GCash_GSave.objGsavemarketPlace, "Gsave MarketPlace Page");
			verifyElementPresentAndClick(GCash_GSave.objGsave_CIMB, "CIMB Button");
			logger.info("Navigated to My Savings Account Page");
			extent.extentLoggerPass("Account Page", "Navigated to My Savings Account Page");

			explicitWaitVisibility(GCash_GSave.objMySavingAcc, 10);
			if (verifyIsElementDisplayed(GCash_GSave.objMySavingAcc, "My Savings Account Page")) {
				suggestionTapMidScreen(GCash_GSave.objMyPiggyBankTour);

				totalSavingBalance = getText(GCash_GSave.objTotalSavingBalance);
				logger.info("Total Saving Balance is " + totalSavingBalance);
				extent.extentLogger("Balance", "Total Saving Balance is " + totalSavingBalance);

				String accountNo = getText(GCash_GSave.objAccountNo);
				logger.info("Account No. " + accountNo);
				extent.extentLogger("Account No.", "Account No. " + accountNo);

				String interestRate = getText(GCash_GSave.objInterestRate);
				logger.info("Interest Rate " + interestRate);
				extent.extentLogger("Interest Rate", "Interest Rate " + interestRate);

				click(GCash_GSave.objDepositBtn, "Deposit Button");
				logger.info("Navigated to Deposit Page");
				extent.extentLoggerPass("Deposit", "Navigated to Deposit Page");
			}
			explicitWaitVisibility(GCash_GSave.objDepositePage, 10);
			if (verifyIsElementDisplayed(GCash_GSave.objDepositePage, "Deposit Page")) {
				verifyElementDisplayed(GCash_GSave.objCIMBBankLogo);

				String GSaveAccountBalance = getText(GCash_GSave.objGSaveAccountBalance);
				GSaveAccountBalance = GSaveAccountBalance.substring(4);
				Assert.assertEquals(GSaveAccountBalance, totalSavingBalance);

				String gCashText = getText(GCash_GSave.objGCashAmt);
				gCashText = gCashText.substring(4);
				String[] split = gCashText.split(",");
				String balanceBeforeComma = split[0];
				String afterComma = split[1];

				String amountBalance = balanceBeforeComma + afterComma;
				float amoutBalanceToFloat = Float.parseFloat(amountBalance);
				amoutBalanceToFloat = amoutBalanceToFloat + 100;
				String amoutBalanceToString = Float.toString(amoutBalanceToFloat);

				click(GCash_GSave.objNextBtn, "NEXT Button");
				if (verifyElementPresent(GCash_GSave.objOOps, "OOPS! Popup")) {
					click(GCash_GSave.objOkBtns, "OK Button");
				}

				click(GCash_GSave.objAmountTextField, "Amount Text Field");
				waitTime(2000);
				type(GCash_GSave.objAmountTextField, amoutBalanceToString, "Amount Text Field");
				click(GCash_GSave.objNextBtn, "NEXT Button");

				explicitWaitVisibility(GCash_GSave.objErrorMsg, 20);
				verifyElementPresent(GCash_GSave.objErrorMsg, "Something went wrong. Message");
				click(GCash_GSave.objOKBtn, "OK Button");
				androidClearText(GCash_GSave.objAmountTextField, "Enter Amount Text Field");
				waitTime(2000);
				type(GCash_GSave.objAmountTextField, amtPay, "Amount Text Field");
				enterAmount = getText(GCash_GSave.objAmountTextField);
				click(GCash_GSave.objNextBtn, "NEXT Button");
				logger.info("Navigated to Confirmation Page");
				extent.extentLoggerPass("Deposit", "Navigated to Confirmation Page");
			}
		}
	}

	/**
	 * @param GSave Confirmation
	 * @throws Exception
	 */
	public void gSaveConfirmTranscation() throws Exception {
		extent.HeaderChildNode("Confirm transaction");

		if (verifyElementPresent(GCash_GSave.objConfirmationPage, "Confirmation Page")) {
			String phpAmt = getText(GCash_GSave.objAmtPhp);
			phpAmt = phpAmt.substring(4);
			Assert.assertEquals(phpAmt, enterAmount);
			logger.info("You are about to deposit: php " + phpAmt);
			extent.extentLogger("To deposit Amt", "You are about to deposit: php " + phpAmt);

			click(GCash_GSave.objConfirmBtn, "CONFIRM Button");
		}

		if (verifyElementDisplayed(GCash_GSave.objDepositeSuccess)) {
			click(GCash_GSave.objOkBtns, "OK Button");
			logger.info("Navigated to Home Page");
			extent.extentLoggerPass("HomePage", "Navigated to Home Page");

			String gSaveSuccesstext = getText(GCash_GSave.objDepositeSuccess);
			verifyElementPresent(GCash_GSave.objDepositeSuccess, gSaveSuccesstext);
			verifyElementPresentAndClick(GCash_GSave.objOKBtn, "OK Button");
			logger.info("Navigated to Home Page");
			extent.extentLoggerPass("HomePage", "Navigated to Home Page");
		} else {
			logger.info("Failed to Navigate to Home Page");
			extent.extentLoggerFail("HomePage", "Failed to Navigate to Home Page");
		}

		String homeHeaderText = getText(GGivesHomePage.objHomePageHeader);
		if (verifyElementPresent(GGivesHomePage.objHomePageHeader, homeHeaderText + " Page")) {
			Assert.assertEquals(homeHeaderText, "Hello!");
			logger.info(homeHeaderText + " Page is displayed");
			extent.extentLoggerPass("HomePage", homeHeaderText + " Page is displayed");
		}
		gCashLogout();

		String MPINPage = getText(GGivesLoginPage.objMpinPageVerify);
		if (verifyElementPresent(GGivesLoginPage.objMpinPageVerify, MPINPage + " Page")) {
			Assert.assertEquals("Enter your MPIN", MPINPage);
			logger.info("User Successfully Logged Out");
			extent.extentLoggerPass("Log Out", "User Successfully Logged Out");
		} else {
			logger.info("Failed to Log Out");
			extent.extentLoggerFail("Log Out", "Failed to Log Out");
		}
	}
}