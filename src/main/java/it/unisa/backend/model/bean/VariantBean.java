package it.unisa.backend.model.bean;

import java.io.Serializable;

public class VariantBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String sku;
	private String size;
	private double price;
	private int quantity;
	private String flavour;
	private String imageUrl;
	
	public VariantBean() {}

	public VariantBean(long id, String sku, String size, double price, int quantity, String flavour, String imageUrl) {
		this.id = id;
		this.sku = sku;
		this.size = size;
		this.price = price;
		this.quantity = quantity;
		this.flavour = flavour;
		this.imageUrl = imageUrl;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getFlavour() {
		return flavour;
	}

	public void setFlavour(String flavour) {
		this.flavour = flavour;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
