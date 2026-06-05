package it.unisa.backend.model.bean;

import java.io.Serializable;

public class AddressBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String zipCode;
	private String city;
	private String street;
	private String streetNumber; 
	private String country;
	
	public AddressBean() {}

	public AddressBean(String zipCode, String city, String street, String streetNumber, String country) {
		this.zipCode = zipCode;
		this.city = city;
		this.street = street;
		this.streetNumber = streetNumber;
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
