package com.elarib.lowcosttrip.web.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.PriceLevel;

@RestController
@RequestMapping("/api")
public class SearchHotelResource {
	
	public static String GOOGLE_MAPS_API_KEY = "AIzaSyAVyvH42WZzRKQK1Jr7Am_9S5vHyF42OaQ";
	public static String AUTO_SUGGEST_URL = "http://secure.rezserver.com/api/autosuggest/?format=jsonp&product=hotel&refid=2050&query=[CITY]&cities=true&airports=true&hotels=false&regions=true&pois=false&plugin=&pet_friendly=false&path=hotel&jsoncallback=jQuery20309141397151075861_1480183644575&_=1480183644583";
	public static String SEARCH_RESULT_API = "http://secure.rezserver.com/hotels/results/list?refid=7723&varid=1a2b3a4b73b74a75b&city_id=800029889&type=city&rooms=1&adults=1&children=0&check_in=12%2F13%2F2016&check_out=12%2F16%2F2016&date_search=1&currency=EUR&radius=0&distance_unit=mile&search_type=city";
	public static String NEW_PREFIX_IMG= "max1024x768";
	public static String OLD_PREFIX_IMG= "square150";
	
	@RequestMapping("/searchHotel")
	public String test(HttpServletRequest req) throws Exception {
		GeoApiContext context = new GeoApiContext().setApiKey(GOOGLE_MAPS_API_KEY);
		PlacesSearchResult[] results =  PlacesApi.textSearchQuery(context, "hotels in Marseille, France").await().results;
		
		
		
		for (int i = 0; i < results.length/2; i++) {
			System.out.println(results[i].name);
			
			PlaceDetails place = new PlaceDetailsRequest(context).placeId(results[i].placeId).await();
			PriceLevel price = place.priceLevel;
			String address = place.formattedAddress;
			

			
			System.out.println("---> Price : "+ price);
			
			System.out.println("---> Adress : "+ address);
			
//			for (int j = 0; j < results[i].types.length; j++) {
//				
//				
//				
//				System.out.println("---> "+ results[i].types[j]);
//				
//			}
			System.out.println("#####");
			System.out.println();System.out.println();System.out.println();
		}
		
	
	    
	    return "Ok";
	}


}
