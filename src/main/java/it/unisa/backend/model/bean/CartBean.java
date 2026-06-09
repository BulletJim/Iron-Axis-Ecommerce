package it.unisa.backend.model.bean;

import java.io.Serializable;
import java.time.*;
import java.util.HashMap;
import java.util.Map;

public class CartBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String userEmail;
	private double totalPrice;
	private LocalDateTime creationDate;
	
	private Map<Long, CartItemBean> variants;
	
	public CartBean() {this.variants = new HashMap<>();}

	public CartBean(long id, String userEmail, double totalPrice, 
			LocalDateTime creationDate, Map<Long, CartItemBean> variants) {
		this.id = id;
		this.userEmail = userEmail;
		this.totalPrice = totalPrice;
		this.creationDate = creationDate;
		this.variants = variants;
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

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public Map<Long, CartItemBean> getVariants() {
		return variants;
	}

	public void setVariants(Map<Long, CartItemBean> variants) {
		this.variants = variants;
	}
	
}
