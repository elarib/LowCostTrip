package com.elarib.lowcosttrip.web.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/api")
public class SearchHotelResource {
	
	public static String GOOGLE_MAPS_API_KEY = "AIzaSyAVyvH42WZzRKQK1Jr7Am_9S5vHyF42OaQ";
	public static String AUTO_SUGGEST_URL = "http://secure.rezserver.com/api/autosuggest/?format=json&product=hotel&refid=2050&query=[CITY]&cities=true";
	public static String SEARCH_RESULT_API = "http://secure.rezserver.com/hotels/results/list?refid=7723&varid=1a2b3a4b73b74a75b&city_id=[CITYID]&type=city&rooms=1&adults=1&children=0&check_in=12%2F13%2F2016&check_out=12%2F16%2F2016&date_search=1&currency=EUR&radius=0&distance_unit=mile&search_type=city";
	public static String NEW_PREFIX_IMG= "max1024x768";
	public static String OLD_PREFIX_IMG= "square150";
	
	@RequestMapping("/searchHotel")
	public String test(HttpServletRequest req) throws Exception {
		
//		String response = new RestTemplate().getForObject("http://secure.rezserver.com/hotels/results/list?refid=7723&varid=1a2b3a4b73b74a75b&city_id=800029889&type=city&rooms=1&adults=1&children=0&check_in=12%2F13%2F2016&check_out=12%2F16%2F2016&date_search=1&currency=EUR&radius=0&distance_unit=mile&search_type=city",
//				String.class);
		
		URL url = new URL(
				"http://secure.rezserver.com/hotels/results/list?refid=7723&varid=1a2b3a4b73b74a75b&city_id="+req.getParameter("cityID")+"&type=city&rooms=1&adults=1&children=0&check_in=12%2F13%2F2016&check_out=12%2F16%2F2016&date_search=1&currency=EUR&radius=0&distance_unit=mile&search_type=city");
		HttpURLConnection conn =null;
		List<Hotel> listHotel = new ArrayList<Hotel>();
		try {
			 conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();

			ObjectMapper mapper = new ObjectMapper();
			
			Iterator<JsonNode> listHotelIterator = mapper.readTree(sb.toString()).get("hotels").elements();
			
			
			
			

			
			
			while(listHotelIterator.hasNext()){
				JsonNode ele = listHotelIterator.next();
				listHotel.add(new Hotel(
						ele.get("id").asLong(),
						ele.get("name").asText(),
						ele.get("star_rating").asInt(),
						ele.get("thumbnail").asText(),
						ele.get("geo").get("latitude").asText(),
						ele.get("geo").get("longitude").asText(),
						ele.get("price_symbol").asText(),
						ele.get("price_display").asInt(),
						ele.get("address_display").asText()
						));
			}
			
			
			

			

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		finally {
			conn.disconnect();
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(listHotel);
			
			return json;
		}
	}

	
	@RequestMapping("/suggest")
	public String suggest(HttpServletRequest req) throws JsonProcessingException, IOException {
		
		String response = new RestTemplate().getForObject(AUTO_SUGGEST_URL.replace("[CITY]", req.getParameter("cityName")),
				String.class);
		
		
		ObjectMapper mapper = new ObjectMapper();
		
		
		Iterator<JsonNode> listCitiesIterator = mapper.readTree(response).get("getSolr").get("results").get("result").get("city").elements();
		
		List<CitySuggest> listCities = new ArrayList<CitySuggest>();
		
		ArrayNode list = mapper.createArrayNode();
		
		ObjectNode object = mapper.createObjectNode();
		
		object.put("results", list);
		
		while(listCitiesIterator.hasNext()){
			JsonNode ele = listCitiesIterator.next();
			
			
			
			list.add(mapper.valueToTree(new CitySuggest(
					ele.get("coordinate").asText(),
					ele.get("city").asText(),
					ele.get("country").asText(),
					ele.get("cityid_ppn").asText(),
					ele.get("city").asText()+", "+ele.get("country").asText()
					)));
		}

		return object.toString();
		
		
		
		
	}
	
	
	
}
