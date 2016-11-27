package com.elarib.lowcosttrip.web.rest;

public class Hotel {
	
	private long id;
	private String name;
	private int starRating;
	private String image;
	private String longtitue;
	private String latitude;
	private String priceSymbole;
	private long price;
	private String address;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStarRating() {
		return starRating;
	}
	public void setStarRating(int starRating) {
		this.starRating = starRating;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getlongtitue() {
		return longtitue;
	}
	public void setlongtitue(String longtitue) {
		this.longtitue = longtitue;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getPriceSymbole() {
		return priceSymbole;
	}
	public void setPriceSymbole(String priceSymbole) {
		this.priceSymbole = priceSymbole;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Hotel(long id, String name, int starRating, String image, String longtitue, String latitude,
			String priceSymbole, long price, String address) {
		super();
		this.id = id;
		this.name = name;
		this.starRating = starRating;
		this.image = image;
		this.longtitue = longtitue;
		this.latitude = latitude;
		this.priceSymbole = priceSymbole;
		this.price = price;
		this.address = address;
	}
	@Override
	public String toString() {
		return "Hotel [id=" + id + ", name=" + name + ", starRating=" + starRating + ", image=" + image + ", longtitue="
				+ longtitue + ", latitude=" + latitude + ", priceSymbole=" + priceSymbole + ", price=" + price
				+ ", address=" + address + "]";
	}
	
	
	

}
