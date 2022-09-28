package com.android.GCASHPages;

import org.openqa.selenium.By;

public class GGivesViewAll {

	//Grow Sub header
	public static By objGrowSubHeader = By.id("com.globe.gcash.android.uat.tokyo:id/lnr_grow");
	
	//View All header
	public static By objGgivesViewAll = By.id("com.globe.gcash.android.uat.tokyo:id/tv_toolbar_title");
	
	//Grow Sub header
	public static By objGrowGGivesModule = By.xpath("//*[@text='GGives']");
	
	//loan account no
	public static By objLoanAccountNumber = By.id("com.globe.gcash.android.uat.tokyo:id/account_number_value");
	
	//interest rate
	public static By objInterestRate = By.id("com.globe.gcash.android.uat.tokyo:id/interest_rate_value");
//	-------------------------------------------------------------------------------------------
	//GGives Welcome dashboard popup
	public static By objGGivesDashboardPopup = By.xpath("//*[@text='Welcome sa GGives dashboard!']");
	
	//GGives Dashboard popup ok button
	public static By objGGivesDashboardpopupOkBtn = By.xpath("//*[@text='OK']");
	
	//Suggestion
	public static By objGuideSuggestion = By.id("com.globe.gcash.android.uat.tokyo:id/guide_message_title");
	
	//Pay for gives Button
	public static By objPayForGivesBtn = By.xpath("//*[@text='PAY FOR GGIVES']");
	
	//Enter Amount Page Popup
	public static By objEnterAmtPopup = By.xpath("//*[@text='Paano bayaran ang GGives?']");
	
	//Enter Amt popup ok button
	public static By objEnterAmtPopupOkBtn = By.xpath("//*[@text='OK']");
//	------------------------------------------------------------------------------------------
	//GGives Header
	public static By objGGivesHeaderPayment = By.id("com.globe.gcash.android.uat.tokyo:id/toolbar_title");
	
	//Ggives Repayment amt
	public static By objEnterAmt = By.id("com.globe.gcash.android.uat.tokyo:id/amount_input");
	
	//loan balance amt
	public static By objLoanAmt = By.id("com.globe.gcash.android.uat.tokyo:id/tv_loan_balance_val");
	
	//Loan account No
	public static By objLoanAccountNo = By.id("com.globe.gcash.android.uat.tokyo:id/tv_loan_acct_val");
	
	//Next Button
	public static By objNextBtn = By.id("com.globe.gcash.android.uat.tokyo:id/next_button");
	
	//Pay amount due
	public static By objPayAmtDue = By.xpath("//*[@text='Pay amount due']");
	
	//Gcash Balance
	public static By objGcashBalance = By.id("com.globe.gcash.android.uat.tokyo:id/toolbar_title");
	
	//Gcash error msg
	public static By objGcashErrorMsg = By.id("com.globe.gcash.android.uat.tokyo:id/gcash_balance_error");
	
	//Payment due option
	public static By objPaymentDueOptionBtn = By.id("com.globe.gcash.android.uat.tokyo:id/full_amount_due_container");
	
	//Due Amount
	public static By objDueAmount = By.id("com.globe.gcash.android.uat.tokyo:id/amount_to_pay");
	
	//Half of due label
	public static By objPayInGives = By.id("com.globe.gcash.android.uat.tokyo:id/half_amount_due_container");
	
	//Pay in Gives popup
	public static By objPayInGivesPopup = By.xpath("//*[@text='Pay in Gives.']");
	
	//Pay in Gives popup ok button
	public static By objPayInGivesPopupOkBtn = By.xpath("//*[@text='OK']");
	
	//Loan Account Next button
	public static By objLoanACNextBtn = By.id("com.globe.gcash.android.uat.tokyo:id/next_button");
	
	//GCash Balance
	public static By objGcashAvailableBalance = By.id("com.globe.gcash.android.uat.tokyo:id/gcash_balance");
	
	//GGives Balance
	public static By objGCashBal = By.id("com.globe.gcash.android.uat.tokyo:id/gcash_balance");
	
}
