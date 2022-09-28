package com.android.GCASHPages;

import org.openqa.selenium.By;

public class GGivesLoginPage {

	// Request Permission
	public static By objRequestPermissionPopup = By.xpath("//*[@text='Request Permission']");

	// Request Permission Ok Button
	public static By objRequestPermissionOkBtn = By.xpath(
			"//*[@text='Request Permission']/parent::android.widget.LinearLayout/parent::android.widget.LinearLayout/following-sibling::android.widget.ScrollView/descendant::android.widget.Button[@text='Ok']");

	// Allow popup Picture
	public static By objPicturePopup = By.xpath("//*[@text='Allow GCash UAT to take pictures and record video?']");

	// Allow Picture popup Don't Allow
	public static By objPictureDontAllowBtn = By.id("com.android.permissioncontroller:id/permission_deny_button");

	// Location allow popup
	public static By objLocationPopup = By.xpath("//*[@text='Allow GCash UAT to access this device’s location?']");

	// Allow location popup Don't Allow
	public static By objLocationDontAllowBtn = By.id("com.android.permissioncontroller:id/permission_deny_button");
	
	// Allow location popup Don't Allow
	public static By objRequestLocationDontAllowBtn = By.id("com.android.permissioncontroller:id/permission_deny_and_dont_ask_again_button");

	// contact allow popup
	public static By objContactPopup = By.xpath("//*[@text='Allow GCash UAT to access your contacts?']");

	// Allow contact popup Don't Allow
	public static By objContactDontAllowBtn = By.id("com.android.permissioncontroller:id/permission_deny_button");

	// photo media allow popup
	public static By objPhotoPopup = By
			.xpath("//*[@text='Allow GCash UAT to access photos and media on your device?']");

	// Allow photo media popup Don't Allow
	public static By objPhotoDontAllowBtn = By.id("com.android.permissioncontroller:id/permission_deny_button");

	// Welcome page verify
	public static By objWelcomePageVerify = By.xpath("//*[@text='Welcome to GCash']");
//	------------------------------------------------------------------------------------------------------------------

	// Login Button
	public static By objLoginBtn = By.xpath("//*[@text='LOG IN']");

	// GCASH mobile registration Logo
	public static By objGCashLogo = By.id("com.globe.gcash.android.uat.tokyo:id/gcash");

	// mobile number field
	public static By objMobileNumberField = By.id("com.globe.gcash.android.uat.tokyo:id/txt_msisdn");

	// Click Next button
	public static By objNextBtn = By.xpath("//*[@text='NEXT']");
	
	//Something Went wrong
	public static By objWrongPopup = By.id("com.globe.gcash.android.uat.tokyo:id/alertTitle");
	
	//Later button
	public static By objLaterBtns = By.id("android:id/button2");
//	-------------------------------------------------------------------------------------------------------------------

	// Authentication page verify
	public static By objAuthenticationPageVerify = By.xpath("//*[@text='Authentication']");

	// OTP tex field
	public static By objOTPField = By.id("com.globe.gcash.android.uat.tokyo:id/txt_code");

	// Submit page
	public static By objSubmitBtn = By.id("com.globe.gcash.android.uat.tokyo:id/btn_submit");
//	----------------------------------------------------------------------------------------------------------------------

	// Alert popup
	public static By objRequestAlertPermissionPopup = By.xpath("//*[@text='Request Permission']");

	// alert ok button
	public static By objAlertOkBtn = By.xpath("//*[@text='Ok']");
//	---------------------------------------------------------------------------------------------------------------------

	// Mpin page verify
	public static By objMpinPageVerify = By.xpath("//*[@text='Enter your MPIN']");

	// Click 1
	public static By objOneBtn = By.xpath("//*[@text='1']");

	// Click 2
	public static By objTwoBtn = By.xpath("//*[@text='2']");
	
	//click
	public static By objThreeBtn = By.xpath("//*[@text='3']");

	// change number
	public static By objChangeNumber = By.xpath("//*[@text='Change Number']");
//	------------------------------------------------------------------------------------------------------------------------

	// Wrong MPIN Popup
	public static By objMPINPopup = By.xpath("//*[@text='Retries have been exceeded.']");

	// Later Button
	public static By objLaterBtn = By.xpath("//*[@text='Later']");
//	----------------------------------------------------------------------------------------------------------------------
	// Oops popup
	public static By objOopsPopup = By.xpath("//*[@text='Oops!']");

	// Yes Button
	public static By objYesBtn = By.xpath("//*[@text='Yes']");
//	------------------------------------------------------------------------------------------------------------------------
	
	//Invalid OTP popup
	public static By objInvalidPopupVerify = By.xpath("//*[@text='Invalid authentication code.']");
	
	//OK btn
	public static By objOKBtn = By.xpath("//*[@text='OK']");
//	-----------------------------------------------------------------------------------------------------------------------
	
	//Mpin popup
	public static By objMpinpopupVerify = By.xpath("//*[@text='The MPIN entered is incorrect.']");
	
	//OK btn
	public static By objMpinpopupOKBtn = By.xpath("//*[@text='OK']");
	
	
}
