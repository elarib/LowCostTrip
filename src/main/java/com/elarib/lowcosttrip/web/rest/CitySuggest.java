package com.elarib.lowcosttrip.web.rest;

public class CitySuggest {

	private String coord;
	private String city;
	private String country;
	private String id;
	private String cityCountry;
	
	
	public CitySuggest(String coord, String city, String country, String id, String cityCountry) {
		super();
		this.coord = coord;
		this.city = city;
		this.country = country;
		this.id = id;
		this.setCityCountry(cityCountry);
	}
	public String getCoord() {
		return coord;
	}
	public void setCoord(String coord) {
		this.coord = coord;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getCityCountry() {
		return cityCountry;
	}
	public void setCityCountry(String cityCountry) {
		this.cityCountry = cityCountry;
	}
	@Override
	public String toString() {
		return "CitySuggest [coord=" + coord + ", city=" + city + ", country=" + country + ", id=" + id
				+ ", cityCountry=" + cityCountry + "]";
	}
	
}
