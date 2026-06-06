package it.unisa.backend.model.bean;

import java.io.Serializable;
import java.time.*;
import java.util.List;
import java.util.ArrayList;

public class UserBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String passwordHash;
	private String passwordSalt;
	private String firstName;
	private String lastName;
	private String role;
	private LocalDate birthDate;
	private LocalDateTime registrationDate;
	private List<AddressBean> addresses;
	private List<PhoneBean> phones;
	
	public UserBean() {
		this.addresses = new ArrayList<>();
	}

	public UserBean(String email, String passwordHash, String passwordSalt, 
			String firstName, String lastName, String role, LocalDate birthDate,
			LocalDateTime registrationDate, List<AddressBean> addresses, List<PhoneBean> phones) {
		this.email = email;
		this.passwordHash = passwordHash;
		this.passwordSalt = passwordSalt;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.birthDate = birthDate;
		this.registrationDate = registrationDate;
		this.addresses = addresses;
		this.phones = phones;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	public List<AddressBean> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressBean> addresses) {
		this.addresses = addresses;
	}

	public List<PhoneBean> getPhones() {
		return phones;
	}

	public void setPhones(List<PhoneBean> phones) {
		this.phones = phones;
	}
	
}
