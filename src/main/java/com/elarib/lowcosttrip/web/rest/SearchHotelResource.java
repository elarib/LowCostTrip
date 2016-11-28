package com.elarib.lowcosttrip.web.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class SearchHotelResource {
	
	public static String GOOGLE_MAPS_API_KEY = "AIzaSyAVyvH42WZzRKQK1Jr7Am_9S5vHyF42OaQ";
	public static String AUTO_SUGGEST_URL = "http://secure.rezserver.com/api/autosuggest/?format=json&product=hotel&refid=2050&query=[CITY]&cities=true";
	public static String SEARCH_RESULT_API = "http://secure.rezserver.com/hotels/results/list?refid=7723&varid=1a2b3a4b73b74a75b&city_id=[CITYID]&type=city&rooms=1&adults=1&children=0&check_in=12%2F13%2F2016&check_out=12%2F16%2F2016&date_search=1&currency=EUR&radius=0&distance_unit=mile&search_type=city";
	public static String NEW_PREFIX_IMG= "max1024x768";
	public static String OLD_PREFIX_IMG= "square150";
	public static String WEATHER_API = "2We4IyHCHClzdcxGhA4Gthw37LZv64Tl";
	public static String LOCATION_WEATHER_URL = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?q=[COORD]&apikey="+WEATHER_API;
	public static String WEATHER_5_DAYS_URL = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/[IDPLACE]?language=fr-FR&metric=true&apikey="+WEATHER_API;
	
	
	@RequestMapping("/searchHotel")
	public String searchHotel(HttpServletRequest req) throws Exception {
		
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
	
	@RequestMapping(value = "/getWeather", method = RequestMethod.GET)
	public String getWeatherByCity(HttpServletRequest req) throws JsonProcessingException, IOException {

		String infoPlace = new RestTemplate().getForObject(getLocationUrl(req.getParameter("coord")),
				String.class);

		ObjectMapper mapperInfoPlace = new ObjectMapper();
		String idPlace = mapperInfoPlace.readTree(infoPlace).get("Key").asText();

		String weather5Days = new RestTemplate().getForObject(getWeather5Days(idPlace), String.class);

		ObjectMapper mapper3 = new ObjectMapper();
		
		String text = mapper3.readTree(weather5Days).get("Headline").get("Text").asText();
		
		JsonNode list = mapper3.readTree(weather5Days).get("DailyForecasts");

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();

		JsonNode firstDay = list.get(0);

		node.put("date", "Aujourd'hui");
		node.put("text", text);
		node.put("temperature", (firstDay.get("Temperature").get("Minimum").get("Value").asDouble()
				+ firstDay.get("Temperature").get("Maximum").get("Value").asDouble()) / 2);
		node.put("day", firstDay.get("Day").get("IconPhrase").asText());
		node.put("night", firstDay.get("Night").get("IconPhrase").asText());
		//node.put("icon", getIconUrl(firstDay.get("Day").get("Icon").asText()));
		node.put("location", "loca");

		ArrayNode arrayNode = mapper.createArrayNode();

		int i = 0;
		for (JsonNode dayNode : list) {
			// System.out.println(dayNode);
			if (i != 0) {

				ObjectNode objectNode2 = mapper.createObjectNode();

				String dayName = getDaYName(dayNode.get("EpochDate").asLong());

				objectNode2.put("day", dayName);
//				objectNode2.put("icon", Global.getIconUrl(dayNode.get("Day").get("Icon").asText()));
				objectNode2.put("temperature", (dayNode.get("Temperature").get("Minimum").get("Value").asDouble()
						+ dayNode.get("Temperature").get("Maximum").get("Value").asDouble()) / 2);
				objectNode2.put("event", dayNode.get("Day").get("IconPhrase").asText());

				arrayNode.add(objectNode2);
			}
			i++;
			if(i==4)
				break;
		}

		node.putPOJO("details", arrayNode);

		 return node.toString();

//		return weather5Days;

	}
	
	public static String getLocationUrl(String coord){
		
		
		return LOCATION_WEATHER_URL.replace("[COORD]", coord);
	}
	
	
	public static String getWeather5Days(String idPlace){
		
		
		return WEATHER_5_DAYS_URL.replace("[IDPLACE]", idPlace);
	}
	
	public static String getDaYName(long timestamp) {

		java.util.Date time = new java.util.Date((long) timestamp * 1000);
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE", Locale.FRANCE);
		return formatter.format(cal.getTime());

	}
	
	
	
}
