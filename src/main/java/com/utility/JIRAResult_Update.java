package com.utility;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class JIRAResult_Update {

	public static void updateTaskStatus(String JiraID, String status) throws UnirestException {
		JsonNodeFactory jnf = JsonNodeFactory.instance;
		ObjectNode payload = jnf.objectNode();
		{
		  ObjectNode transition = payload.putObject("transition");
		  {
			 transition.put("id", status);	    
		  }
		// Connect Jackson ObjectMapper to Unirest
		Unirest.setObjectMapper(new ObjectMapper() {
		   private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
		           = new com.fasterxml.jackson.databind.ObjectMapper();

		   public <T> T readValue(String value, Class<T> valueType) {
		       try {
		           return jacksonObjectMapper.readValue(value, valueType);
		       } catch (IOException e) {
		           throw new RuntimeException(e);
		       }
		   }

		   public String writeValue(Object value) {
		       try {
		           return jacksonObjectMapper.writeValueAsString(value);
		       } catch (JsonProcessingException e) {
		           throw new RuntimeException(e);
		       }
		   }
		});

		// This code sample uses the  'Unirest' library:
		// http://unirest.io/java.html
		String postURL = "https://testviz-igsindia.atlassian.net/rest/api/3/issue/" + JiraID + "/transitions";
		HttpResponse<JsonNode> response = Unirest.post(postURL)
		  .basicAuth("kaushik.mr@igsindia.net", "vnaEiWpQYiY6xLwnLyLPE649")
		  .header("Accept", "application/json")
		  .header("Content-Type", "application/json")
		  .body(payload)
		  .asJson();
		
		System.out.println(response.getStatus());
		}
	}
	
	public static void updateTaskComment(String JiraID, String TaskComment) throws UnirestException {
		JsonNodeFactory jnf = JsonNodeFactory.instance;
		ObjectNode payload = jnf.objectNode();
		{
		  ObjectNode body = payload.putObject("body");
		  {
		    body.put("type", "doc");
		    body.put("version", 1);
		    ArrayNode content = body.putArray("content");
		    ObjectNode content0 = content.addObject();
		    {
		      content0.put("type", "paragraph");
		      content = content0.putArray("content");
		      content0 = content.addObject();
		      {
		        content0.put("text", TaskComment);
		        content0.put("type", "text");
		      }
		    }
		  }

		// Connect Jackson ObjectMapper to Unirest
		Unirest.setObjectMapper(new ObjectMapper() {
		   private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
		           = new com.fasterxml.jackson.databind.ObjectMapper();

		   public <T> T readValue(String value, Class<T> valueType) {
		       try {
		           return jacksonObjectMapper.readValue(value, valueType);
		       } catch (IOException e) {
		           throw new RuntimeException(e);
		       }
		   }

		   public String writeValue(Object value) {
		       try {
		           return jacksonObjectMapper.writeValueAsString(value);
		       } catch (JsonProcessingException e) {
		           throw new RuntimeException(e);
		       }
		   }
		});

		// This code sample uses the  'Unirest' library:
		// http://unirest.io/java.html
		String postURL = "https://testviz-igsindia.atlassian.net/rest/api/3/issue/" + JiraID + "/comment";
		HttpResponse<JsonNode> response = Unirest.post(postURL)
				  .basicAuth("kaushik.mr@igsindia.net", "vnaEiWpQYiY6xLwnLyLPE649")
				  .header("Accept", "application/json")
				  .header("Content-Type", "application/json")
				  .body(payload)
				  .asJson();
		
		System.out.println(response.getStatus());
		}
	}
}
