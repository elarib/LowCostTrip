package com.elarib.lowcosttrip.web.rest;

public class RestaurantPlaces {
	
	private String placeId;
	private String name;
	private String vicinity;
	private String lat;
	private String log;
	public RestaurantPlaces(String placeId, String name, String vicinity, String lat, String log) {
		super();
		this.placeId = placeId;
		this.name = name;
		this.vicinity = vicinity;
		this.lat = lat;
		this.log = log;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVicinity() {
		return vicinity;
	}
	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	@Override
	public String toString() {
		return "RestaurantPlaces [placeId=" + placeId + ", name=" + name + ", vicinity=" + vicinity + ", lat=" + lat
				+ ", log=" + log + "]";
	}
	
	
	
	
}
