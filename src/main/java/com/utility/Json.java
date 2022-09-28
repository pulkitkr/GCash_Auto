package com.utility;

import java.io.IOException;
import org.json.simple.JSONObject;
import com.github.jsontemplate.JsonTemplate;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Json {

	private static final String BASE_URL= "https://xray.cloud.getxray.app";
	
	@SuppressWarnings("unchecked")
	public static void XrayJsonImport(String testKey, String result) throws InterruptedException, IOException {
		
		ConfigurationGetter config = new ConfigurationGetter();
		String xrayEnable = config.getPropValues().getProperty("xray.enabled");
		if(xrayEnable.contentEquals("true")) {
			System.out.println("test Key : "+testKey);
			JsonTemplate json = JsonXRayTemplate.json(testKey, result);
			
			RestAssured.baseURI = BASE_URL;
			RequestSpecification request = RestAssured.given();
			request.header("Content-Type", "application/json");
			JSONObject requestParams = new JSONObject();
			requestParams.put("client_id", "7BDD4513945D4FFFB1699C8386F7B33E"); // Cast
			requestParams.put("client_secret", "c38f50b4ec4c8311d63404008fea98de290df6a62c25375c246c2ae85ac88111");

			request.body(requestParams.toJSONString());
			
			Response response = request.post("/api/v2/authenticate");
			int statusCode = response.getStatusCode();
			System.out.println(statusCode);
			String token = response.getBody().asString();
			String tokenNo = token.replace("\"", "");
			
			System.out.println(tokenNo);
			RestAssured.baseURI = BASE_URL;	
			RequestSpecification request1 = RestAssured.given();
			request1.queryParam("projectKey", "PP");
			request1.header("Content-Type", "application/json");

			request1.header("Authorization", "Bearer " + tokenNo);
			request1.body(json.getTemplate());
			request1.contentType(ContentType.JSON);
			Response response1 = request1.post("/api/v2/import/execution");
			
			int statusCode1 = response1.getStatusCode();
			System.out.println(statusCode1);

			System.out.println(response1.getBody().asString());	
		}else {
			System.out.println("Xray is disabled");
		}
	}
	

}
