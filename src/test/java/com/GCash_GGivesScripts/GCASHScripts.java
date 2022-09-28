package com.GCash_GGivesScripts;

import java.io.IOException;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.extent.ExtentReporter;
import com.utility.JIRAResult_Update;
import com.utility.Utilities;

public class GCASHScripts {

	private com.business.gCASH.GCASHBusinessLogic GCASHBusiness;
//Hello
	@BeforeTest
	public void Before() throws InterruptedException {
		GCASHBusiness = new com.business.gCASH.GCASHBusinessLogic("gcash");
	}

	@Test(priority = 0)
	@Parameters({ "userType" })
	public void Allowpopup(String userType) throws Exception {
		GCASHBusiness.GGivesAppLaunch(userType);
		ExtentReporter.jiraID = "PP-9";
	}

	@Test(priority = 1)
	@Parameters({ "InvalidphoneNumber", "validphonenumber", "GGivesLoginValidOTP" , "GGivesLoginInValidOTP"})
	public void Login(String InvalidphoneNumber, String validphonenumber, String GGivesLoginValidOTP , String GGivesLoginInValidOTP) throws Exception {
		GCASHBusiness.loginToGCash(InvalidphoneNumber, validphonenumber, GGivesLoginValidOTP, GGivesLoginInValidOTP);
		ExtentReporter.jiraID = "PP-17";
	}

	@Test(priority = 2)
	@Parameters({ "userType" })
	public void gGivesHomePage(String userType) throws Exception {
		GCASHBusiness.homePage(userType);
		ExtentReporter.jiraID = "PP-18";
	}

	@Test(priority = 3)
	public void gGivesViewPage() throws Exception {
		GCASHBusiness.ggivesViewAll();
		ExtentReporter.jiraID = "PP-19";
	}

	@Test(priority = 4)
	@Parameters({ "repaymentAMT" })
	public void gGivesDashboardPage(String repaymentAMT) throws Exception {
		GCASHBusiness.ggivesDashboard(repaymentAMT);
		ExtentReporter.jiraID = "PP-20";
	}

	@Test(priority = 5)
	public void gGivesDuesPage() throws Exception {
		GCASHBusiness.gGivesDues();
		ExtentReporter.jiraID = "PP-21";
	}

	@Test(priority = 6)
	public void gGivesPaymentPage() throws Exception {
		GCASHBusiness.gGivesPaymentSuccess();
		ExtentReporter.jiraID = "PP-22";
	}

	@Test(priority = 7)
	public void gCashLogoutPage() throws Exception {
		GCASHBusiness.gCashLogout();
		ExtentReporter.jiraID = "PP-23";
	}

	@Test(priority = 8)
	@Parameters({ "GSaveValidPhoneNumber" , "GSaveValidOTP" })
	public void gSaveLoginPage(String GSaveValidPhoneNumber , String GSaveValidOTP) throws Exception {
		GCASHBusiness.GsaveLogin(GSaveValidPhoneNumber , GSaveValidOTP);
		ExtentReporter.jiraID = "PP-24";
	}

	@Test(priority = 9)
	@Parameters({"amtPay"})
	public void gSaveTransactionPage(String amtPay) throws Exception {
		GCASHBusiness.GsaveTransaction(amtPay);
		ExtentReporter.jiraID = "PP-25";
	}
	
	@Test(priority = 10)
	public void gSaveConfirmPage() throws Exception {
		GCASHBusiness.gSaveConfirmTranscation();
		ExtentReporter.jiraID = "PP-60";
	}

	@AfterTest
	public void After() {
		GCASHBusiness.tearDown();
	}
}
