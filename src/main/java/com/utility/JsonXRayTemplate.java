package com.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.testng.Reporter;
import com.github.jsontemplate.JsonTemplate;

public class JsonXRayTemplate  {

	public static JsonTemplate json(String testKey, String result) {
		String testExecutionKey = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("testExecutionKey");
		System.out.println("Execution Key:" + testExecutionKey);
//		String testExecutionKey = "TES-125";
		//String testPlanKey = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("testPlanKey");
//		String env = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("Env");
//		String env = "prod";
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date());
//		System.out.println(testExecutionKey);
		
//		if(env.contentEquals("prod")) {
//			env = "Production";
//		}else if(env.contentEquals("stage")) {
//			env = "Staging";
//		}
		String temp1 = "{\r\n" + 
				"    \"testExecutionKey\" : \""+testExecutionKey+"\", \r\n" + 
				"    \"info\" : {\r\n" + 
				"        \"startDate\" : \""+timeStamp+"\", \r\n" +	
			    "        \"finishDate\" : \""+timeStamp+"\" \r\n" +					 
				"    },\r\n" + 
				"    \"tests\" : [\r\n" + 
				"        {\r\n" + 
				"            \"testKey\" : \""+testKey+"\",\r\n" + 
				"        	 \"start\" : \""+timeStamp+"\", \r\n" +	
				"            \"finish\" : \""+timeStamp+"\", \r\n" +	
				"            \"comment\" : \"Successful execution\",\r\n" + 
				"        \"status\" : \""+result+"\"\r\n" + 
				"        }\r\n" + 
				"    ]\r\n" + 
				"}";
		
		JsonTemplate json1 = new JsonTemplate(temp1);
		return json1;
	
	}
}