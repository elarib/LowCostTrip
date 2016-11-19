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
