package com.android.GCASHPages;

import org.openqa.selenium.By;

public class GCash_GSave {
	
	public static By objGsaveTab = By.xpath("//android.widget.TextView[@text='GSave']");
	public static By objNextBtn = By.xpath("//*[@text='NEXT']");
	public static By objWelcomePage=By.xpath("//android.widget.TextView[@text='Welcome to GSave Marketplace!']");
	public static By objContinuePopUp = By.xpath("//android.widget.TextView[@text='Tap to continue']");
	public static By objGsave_CIMB = By.xpath("//android.widget.TextView[@text='GSave by CIMB']");
	public static By objGsavemarketPlace=By.xpath("//android.widget.TextView[@text='GSave Marketplace']");
	public static By objContinue = By.id("com.globe.gcash.android.uat.tokyo:id/next_message");
	public static By objMySavingAcc=By.xpath("//android.widget.TextView[@text='My Savings Account']");
	public static By objDepositBtn=By.id("com.globe.gcash.android.uat.tokyo:id/btn_deposit");
	public static By objDepositePage=By.xpath("//android.widget.TextView[@text='Deposit']");
	public static By objAmountTextField=By.id("com.globe.gcash.android.uat.tokyo:id/txt_amount");
	public static By objConfirmationPage=By.xpath("//android.widget.TextView[@text='Confirmation']");
	public static By objConfirmBtn=By.xpath("//android.widget.TextView[@text='CONFIRM']");
	public static By objDepositeSuccess=By.xpath("//android.widget.TextView[@text='GSave Deposit Success!']");
	public static By objOKBtn=By.xpath("//android.widget.TextView[@text='OK']");
	
	
	
	public static By objAmtPhp = By.id("com.globe.gcash.android.uat.tokyo:id/tvAmount");
	public static By objOurPartnerBankTour = By.xpath("//*[@text='Open a savings account with our partners.']");
	public static By objMyPiggyBankTour = By.xpath("//*[@text='We have a new Piggy Bank feature!']");
	public static By objTotalSavingBalance = By.id("com.globe.gcash.android.uat.tokyo:id/txtBalance");
	public static By objAccountNo = By.id("com.globe.gcash.android.uat.tokyo:id/txtAccNumber");
	public static By objInterestRate = By.id("com.globe.gcash.android.uat.tokyo:id/txtInterestRate");
	public static By objCIMBBankLogo = By.id("com.globe.gcash.android.uat.tokyo:id/ivHolder");
	public static By objGCashAmt = By.id("com.globe.gcash.android.uat.tokyo:id/tvGcashAmt");
	public static By objErrorMsg = By.id("com.globe.gcash.android.uat.tokyo:id/tvCustomHeader");
	public static By objGSaveAccountBalance = By.id("com.globe.gcash.android.uat.tokyo:id/tvGSaveValue");
	public static By objOOps = By.id("com.globe.gcash.android.uat.tokyo:id/alertTitle");
	public static By objOkBtns = By.id("android:id/button1");
	
	
	//Bottom Profile Icon
	public static By objProfileIcon = By.xpath("//android.widget.FrameLayout[@content-desc='Profile']/android.widget.ImageView");
	
	//Phone Number
	public static By objPhoneNo = By.id("com.globe.gcash.android.uat.tokyo:id/txt_number");
	
	//Logout 
	public static By objLogout = By.xpath("//*[@text='Log Out']");
	
	//logout popup
	public static By objLogoutPopup = By.xpath("//*[@text='Are you sure you want to log out?']");
	
	//back Button
	public static By objBackBtn = By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']");
	public static By objBackNav = By.xpath("//*[@text='My Savings Account']/preceding-sibling::android.widget.ImageButton");
	public static By objGSaveMarketPlaceBackBtn = By.id("com.globe.gcash.android.uat.tokyo:id/h5_tv_nav_back");
	public static By bjMarketBack = By.id("com.globe.gcash.android.uat.tokyo:id/h5_nav_back");
}




