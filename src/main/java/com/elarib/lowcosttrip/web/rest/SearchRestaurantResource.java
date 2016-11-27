package com.elarib.lowcosttrip.web.rest;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.PriceLevel;

@RestController
@RequestMapping("/api")
public class SearchRestaurantResource {
	
	public static String GOOGLE_MAPS_API_KEY = "AIzaSyAVyvH42WZzRKQK1Jr7Am_9S5vHyF42OaQ";
	
	public static String SEARCH_RESTO_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=[COORD]&radius=500&type=restaurant&key="+GOOGLE_MAPS_API_KEY;
	
	@RequestMapping("searchResto")
	public String searchHotel(HttpServletRequest req) throws JsonProcessingException, IOException{
		
		String response = new RestTemplate().getForObject(SEARCH_RESTO_URL.replace("[COORD]", req.getParameter("coord")),
				String.class);
		
		
		ObjectMapper mapper = new ObjectMapper();
		
		
		Iterator<JsonNode> listRestoIterator = mapper.readTree(response).get("results").elements();
		
		
		ArrayNode list = mapper.createArrayNode();
		
		ObjectNode object = mapper.createObjectNode();
		
		object.put("results", list);
		
		while(listRestoIterator.hasNext()){
			JsonNode ele = listRestoIterator.next();
			
			
			
			list.add(mapper.valueToTree(new RestaurantPlaces(
					ele.get("place_id").asText(),
					ele.get("name").asText(),
					ele.get("vicinity").asText(),
					ele.get("geometry").get("location").get("lat").asText(),
					ele.get("geometry").get("location").get("lng").asText()
					)));
		}

		return object.toString();
		
	
		
	}
	
	

}
