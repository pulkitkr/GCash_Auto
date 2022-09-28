package com.android.GCASHPages;

import org.openqa.selenium.By;

public class GGivesHomePage {

	// HomePage header Text verify
	public static By objHomePageHeader = By.id("com.globe.gcash.android.uat.tokyo:id/lbl_hello");
	
	//Available balance currency
	public static By objAvailableCurrency = By.id("com.globe.gcash.android.uat.tokyo:id/tv_balance");
	
	//Discover popup
	public static By objDiscoverPopup = By.xpath("//*[@text='Something new for you!']");
	
	//Discover Button
	public static By objDiscoverBtn = By.xpath("//*[@text='Discover']");
	
	//Suggestion
	public static By objSuggestion = By.id("com.globe.gcash.android.uat.tokyo:id/btn_dismiss");
	
	//tour msg
	public static By objTourMsg = By.id("com.globe.gcash.android.uat.tokyo:id/guide_message_title");
	
	//View All
	public static By objViewAllIcon = By.xpath("//*[@text='View All']");
	
	//View All Discover
	public static By objViewAllDiscoverPopup = By.id("com.globe.gcash.android.uat.tokyo:id/tv_dialog_title");
}
