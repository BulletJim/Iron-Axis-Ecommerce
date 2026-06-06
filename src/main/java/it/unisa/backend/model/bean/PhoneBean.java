package it.unisa.backend.model.bean;

import java.io.Serializable;
import it.unisa.backend.model.bean.util.PhoneType;

public class PhoneBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String userEmail;
	private String phoneNumber;
	private PhoneType type;
	
	public PhoneBean() {}

	public PhoneBean(long id, String userEmail, String phoneNumber, PhoneType type) {
		this.id = id;
		this.userEmail = userEmail;
		this.phoneNumber = phoneNumber;
		this.type = type;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public PhoneType getType() {
		return type;
	}

	public void setType(PhoneType type) {
		this.type = type;
	}

	

}

