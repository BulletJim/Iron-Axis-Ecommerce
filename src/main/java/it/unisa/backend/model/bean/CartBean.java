package it.unisa.backend.model.bean;

import java.io.Serializable;
import java.time.*;

public class CartBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private double total_price;
	private LocalDateTime creationDate;
	
	public CartBean() {}

	public CartBean(long id, double total_price, LocalDateTime creationDate) {
		this.id = id;
		this.total_price = total_price;
		this.creationDate = creationDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

}
