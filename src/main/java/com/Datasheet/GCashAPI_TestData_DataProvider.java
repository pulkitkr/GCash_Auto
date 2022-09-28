package com.Datasheet;

import java.io.IOException;

import org.testng.annotations.DataProvider;

import com.utils.Excel;

//import commonFunctions.TestUtil;


public class GCashAPI_TestData_DataProvider {
	public static String excelPath()
	{
		String os = System.getProperty("os.name");
		String path = System.getProperty("user.dir");
		String filePath;
		//String filePath=System.getProperty("user.dir")+ "/src/main/java/com/GCashAPI_TestData.xlsx";
		if(os.equalsIgnoreCase("windows 10")){
			filePath = path + "\\src\\main\\java\\com\\Datasheet\\GCashAPI_TestData_" + "stage" + ".xlsx";

		}
		else{
			filePath = path + "//src//main//java//resources//GCashAPI_TestData_" + "env" + ".xlsx";

			}
		return filePath;
	}

	@DataProvider(name = "GCashapi")
    public static Object[][] GCashapi(String testCaseName) throws IOException{
		return Excel.getTestData(excelPath(), "GCash", testCaseName);
	}



}
