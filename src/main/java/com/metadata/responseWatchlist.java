package com.metadata;

import org.json.JSONObject;

import io.restassured.RestAssured;

//import static com.jayway.restassured.RestAssured.given;
import java.util.ArrayList;
//import com.jayway.restassured.RestAssured;
//import com.jayway.restassured.response.Response;
//import com.jayway.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class responseWatchlist {

	protected static Response respp = null;

	public static Response getRECOResponse(String URL, String username, String password) {

		String Uri = URL;
		respp = RestAssured.given()
				.headers("X-ACCESS-TOKEN", getXAccessToken(), "Authorization", getBearerToken(username, password))
				.when().get(Uri);
		// System.out.println(respp.print());
		return respp;

	}

	public static ArrayList<String> WatchlistValidationEpisodesTab() {

		ArrayList<String> EpisodeAssetSubtype = new ArrayList<String>();
		String emptyShow = respp.jsonPath().getString("show");
		// System.out.println(emptyShow);
		if (emptyShow == null) {
			return null;
		}
		int totalShows = respp.jsonPath().getList("show").size();
		// System.out.println("Size of the shows tab " +totalShows);

		for (int i = 0; i < totalShows; i++) {
			int totalEpisodes = respp.jsonPath().getList("show[" + i + "].episodes").size();
			// System.out.println("Total episodes tab " +totalEpisodes);
			for (int j = 0; j < totalEpisodes; j++) {
				EpisodeAssetSubtype
						.add(respp.jsonPath().getString("show[" + i + "].episodes[" + j + "].asset_subtype"));
				// System.out.println(EpisodeAssetSubtype);
			}
		}
		return EpisodeAssetSubtype;
	}

	public static ArrayList<String> WatchlistValidationMoviesTab() {

		ArrayList<String> MovieAssetSubtype = new ArrayList<String>();
		int totalMovies = respp.jsonPath().getList("movie").size();
		// System.out.println("Size of the shows tab " +totalMovies);
		for (int i = 0; i < totalMovies; i++) {
			MovieAssetSubtype.add(respp.jsonPath().getString("movie[" + i + "].asset_subtype"));
			// System.out.println(MovieAssetSubtype);
		}

		return MovieAssetSubtype;
	}

	public static ArrayList<String> WatchlistValidationVideoTab() {

		ArrayList<String> VideoAssetSubtype = new ArrayList<String>();
		if (respp.jsonPath().getList("video") == null) {
			return null;
		}
		int totalVideos = respp.jsonPath().getList("video").size();
		// System.out.println("Size of the shows tab " +totalMovies);
		for (int i = 0; i < totalVideos; i++) {
			VideoAssetSubtype.add(respp.jsonPath().getString("video[" + i + "].asset_subtype"));
			// System.out.println(VideoAssetSubtype);
		}

		return VideoAssetSubtype;
	}

	public static void main(String args[]) {
//		String URL = "https://gwapi.zee5.com/user/v2/watchhistory?country=IN&translation=en";
//
//		String username = "raghucpatel@gmail.com";
//		String pwd = "raghu@123";
//		getRECOResponse(URL, username, pwd);
//		ArrayList<String> episode = WatchlistValidationEpisodesTab();
//		System.out.println(episode);
//		WatchlistValidationVideoTab();
//		WatchlistValidationMoviesTab();
//		Response resp = RestAssured.given().urlEncodingEnabled(false).when().post(
//						"https://useraction.zee5.com/device/v3/getdeviceuser.php");
//		resp.print();
	}

	public static String getXAccessToken() {
		Response respToken = null, respForKey = null;

		// get APi-KEY
		String Uri = "https://gwapi.zee5.com/user/getKey?=aaa";
		respForKey = RestAssured.given().urlEncodingEnabled(false).when().post(Uri);

		String rawApiKey = respForKey.getBody().asString();
		String apiKeyInResponse = rawApiKey.substring(0, rawApiKey.indexOf("<br>airtel "));
		String finalApiKey = apiKeyInResponse.replaceAll("<br>rel - API-KEY : ", "");

		String UriForToken = "http://gwapi.zee5.com/user/getToken";
		respToken = RestAssured.given().headers("API-KEY", finalApiKey).when().get(UriForToken);
		String xAccessToken = respToken.jsonPath().getString("X-ACCESS-TOKEN");

		// System.out.println(xAccessToken);

		return xAccessToken;

	}

	public static String getBearerToken(String email, String password) {
		JSONObject requestParams = new JSONObject();
		requestParams.put("email", email);
		requestParams.put("password", password);

		RequestSpecification req = RestAssured.given();
		req.header("Content-Type", "application/json");
		req.config(io.restassured.RestAssured.config().encoderConfig(io.restassured.config.EncoderConfig
				.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
		req.body(requestParams.toString());
		Response resp = req.post("https://userapi.zee5.com/v2/user/loginemail");
		String accesstoken = resp.jsonPath().getString("access_token");
		String tokentype = resp.jsonPath().getString("token_type");
		String bearerToken = tokentype + " " + accesstoken;
		return bearerToken;
	}

	public static Response getResponseForPages(String page, String contLang) {
		Response respCarousel = null;

		if (page.equals("news")) {
			page = "626";
		} else if (page.equals("music")) {
			page = "2707";
		} else if (page.equals("home")) {
			page = "homepage";
		} else if (page.equals("movies")) {
			page = "movies";
		} else if (page.equals("shows")) {
			page = "tvshows";
		} else if (page.equals("premium")) {
			page = "premiumcontents";
		} else if (page.equals("zeeoriginals")) {
			page = "zeeoriginals";
		} else if (page.equals("livetv")) {
			page = "4098";
		} else if (page.equals("videos")) {
			page = "5011";
		}

		String Uri = "https://gwapi.zee5.com/content/collection/0-8" + page
				+ "?page=2&limit=5&item_limit=20&country=IN&translation=en&languages=" + contLang + "&version=6";
//		String Uri = page;
		respCarousel = RestAssured.given().headers("X-ACCESS-TOKEN", getXAccessToken()).when().get(Uri);
		System.out.println(respCarousel.print());
		return respCarousel;
	}

	public static Response getSpecificViewAllResponse(String id, String lang) {
		Response viewallResp = null;
		String Uri = "https://gwapi.zee5.com/content/collection/0-8" + id
				+ "?page=1&limit=24&item_limit=20&country=IN&translation=en&languages=en,kn&version=6";
		viewallResp = RestAssured.given().headers("X-ACCESS-TOKEN", getXAccessToken()).when().get(Uri);
		return viewallResp;
	}

	public static Response getResponseFromURL(String url) {
		Response respCarousel = null;
		String Uri = url;
		respCarousel = RestAssured.given().headers("X-ACCESS-TOKEN", getXAccessToken()).when().get(Uri);
		System.out.println(respCarousel.print());
		return respCarousel;
	}
	
	
	

}
