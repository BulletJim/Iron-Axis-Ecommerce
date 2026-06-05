package it.unisa.backend.model.bean;

import java.io.Serializable;
import it.unisa.backend.model.bean.util.PhoneType;

public class PhoneBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String phoneNumber;
	private PhoneType type;
	
	public PhoneBean() {}

	public PhoneBean(String phoneNumber, PhoneType type) {
		this.phoneNumber = phoneNumber;
		this.type = type;
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

