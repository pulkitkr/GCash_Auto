package com.android.GCASHPages;

import org.openqa.selenium.By;

public class GGivesPaymentPage {

	//GGives Payment Header
	public static By objGGivesPaymentHeader = By.xpath("//*[@text='GGives Payment']");
	
	//GGives Reference No text
	public static By objGGivesReferenceText= By.xpath("//*[@text='REF. NO.']");
	
	//gGives Reference No
	public static By objGGivesReferenceNo = By.id("com.globe.gcash.android.uat.tokyo:id/reference_number");
	
	//GGives successful Paid text
	public static By objGGivesSuccessfulPaidText= By.id("com.globe.gcash.android.uat.tokyo:id/successful_label");
	
	//GGives title
	public static By objPaidForGGivesTitle = By.id("com.globe.gcash.android.uat.tokyo:id/ggives_title");
	
	//GGives Loan Account No
	public static By objLoanAccNo = By.id("com.globe.gcash.android.uat.tokyo:id/tv_loan_acct_no_val");
	
	//GGives Amount paid
	public static By objAmountPaid = By.id("com.globe.gcash.android.uat.tokyo:id/paid_amount");
	
	//GGives Paid date time
	public static By objAmountPaidDateTime = By.id("com.globe.gcash.android.uat.tokyo:id/payment_date");
	
	//GGives Paid time taken msg
	public static By objAmountPaidTimeTakenMsg = By.id("com.globe.gcash.android.uat.tokyo:id/textview_info");
	
	//GGives Cross BUtton
	public static By objCrossBtn = By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']");
	
}
