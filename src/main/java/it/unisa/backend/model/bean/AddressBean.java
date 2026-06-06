package it.unisa.backend.model.bean;

import java.io.Serializable;

public class AddressBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String userEmail;
	private String zipCode;
	private String city;
	private String street;
	private int streetNumber;
	private String province;
	private String country;

	
	public AddressBean() {}

	public AddressBean(long id, String userEmail, String zipCode, String city, 
			String street, int streetNumber, String province, String country) {
		this.id = id;
		this.userEmail = userEmail;
		this.zipCode = zipCode;
		this.city = city;
		this.street = street;
		this.streetNumber = streetNumber;
		this.province = province;
		this.country = country;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
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

	public int getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(int streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
