package com.metadata;

//import static com.jayway.restassured.RestAssured.given;
import java.util.LinkedList;
import java.util.List;
//import com.jayway.restassured.response.Response;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

	public class getResponseUpNextRail {
		
		protected  static Response resp = null;
		public static Response getResponse() {
			String uri ="https://gwapi.zee5.com/content/season/next_previous/0-2-1095?episode_id=0-1-manual_1qskonmm7sk8&type=next&limit=25&translation=en&country=IN&page=1";
			resp = RestAssured.given().headers("X-ACCESS-TOKEN", getXAccessToken()).when().get(uri);
			System.out.println(resp.getBody());
			return resp;
	}
	
		
		public static Response getResponse1() {
			String uri ="https://gwapi.zee5.com/content/season/next_previous/0-2-1864?episode_id=0-1-manual_4vqosf28clu0&type=next&limit=25&translation=en&country=IN&page=1";
			resp = RestAssured.given().headers("X-ACCESS-TOKEN", getXAccessToken()).when().get(uri);
			System.out.println(resp.getBody());
			return resp;
	}
		
		public static String getMediaContentName(){
			String mediaContentName = null;
			int itemsSize = resp.jsonPath().get("items.size()");
			System.out.println("-------------");
			System.out.println("TOTAL ITEMS IN UPNEXT RAIL : " +itemsSize);
			System.out.println("-------------");
			for (int i = 0 ;i < itemsSize ;) {		
				 mediaContentName = (resp.jsonPath().getString("items["+ i +"].original_title"));
				 System.out.println("Media Content Name in Upnext Rail : " +mediaContentName);
				 break;	 
			}
			return mediaContentName;
		}
	
		/**
	 * Function to return X-ACCESS TOKEN
	 * 
	 * @param page
	 * @return
	 */
	public static String getXAccessToken() {
		Response respToken = null, respForKey = null;
		
		//get APi-KEY
		String Uri = "https://gwapi.zee5.com/user/getKey?=aaa";
		respForKey = RestAssured.given().urlEncodingEnabled(false).when().get(Uri);

		String rawApiKey = respForKey.getBody().asString();
		String apiKeyInResponse = rawApiKey.substring(0, rawApiKey.indexOf("<br>airtel "));
		String finalApiKey = apiKeyInResponse.replaceAll("<br>rel - API-KEY : ", "");
		
		String UriForToken = "http://gwapi.zee5.com/user/getToken";
		respToken = RestAssured.given().headers("API-KEY", finalApiKey).when().get(UriForToken);
		String xAccessToken = respToken.jsonPath().getString("X-ACCESS-TOKEN");
		
		return xAccessToken;
	}
	
	/**
	 * Function to return response for different pages
	 * 
	 * @param page
	 * @return
	 */
	public static Response getResponseForPages(String page, String contLang) {
		Response respCarousel = null;

		if (page.equals("news")) {
			page = "626";
		} else if (page.equals("music")) {
			page = "2707";
		} else if (page.equals("home")) {
			page = "homepage";
		}
		
		String Uri = "https://gwapi.zee5.com/content/collection/0-8-" + page+ "?page=1&limit=5&item_limit=20&country=IN&translation=en&languages="+contLang+"&version=6";
		respCarousel = RestAssured.given().headers("X-ACCESS-TOKEN", getXAccessToken()).when().get(Uri);
		System.out.println("Response Body"+respCarousel.getBody().asString());
		return respCarousel;
	}
	
	/**
	 * Function to tray list on carousel for different pages
	 * 
	 * @param page
	 * @return
	 */
	public static List<String> traysTitleCarousel(String page, String contLang) {
		Response responseCarouselTitle = getResponseForPages(page, contLang);
		List<String> titleOnCarouselList = new LinkedList<String>();
		int numberOfCarouselSlides = responseCarouselTitle.jsonPath().getList("buckets[0].items").size();
		System.out.println("api size : " + numberOfCarouselSlides);
		for (int i = 0; i < 7; i++) {  //Only 7 tray visible on UI
			String title = responseCarouselTitle.jsonPath().getString("buckets[0].items[" + i + "].title");
			titleOnCarouselList.add(title);
		}
		return titleOnCarouselList;
	}
	
	/**
	 * Method to get content details passing content ID
	 * 
	 */
	public static Response getTVShowDetails(String tvShowID) {
		Response respContentDetails = null;
		String Uri = "https://gwapi.zee5.com/content/tvshow/"+tvShowID+"?translation=en&country=IN";
		respContentDetails = RestAssured.given().headers("X-ACCESS-TOKEN", getXAccessToken()).when().get(Uri);
		System.out.println("TV Show Response API"+respContentDetails.getBody().asString());
		return respContentDetails;
	}
	
	public static Response getUpNextResponse(String seasonID,String contentID) {
		String uri ="https://gwapi.zee5.com/content/season/next_previous/"+seasonID+"?episode_id="+contentID+"&type=next&limit=25&translation=en&country=IN&page=1";
		resp = RestAssured.given().headers("X-ACCESS-TOKEN", getXAccessToken()).when().get(uri);
		System.out.println(resp.getBody().print());
		return resp;
	}
}
