package it.unisa.backend.model.bean;

import java.io.Serializable;

public class VariantBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private long productId;
	private String sku;
	private String size;
	private double vat;
	private double price;
	private int quantity;
	private String flavour;
	private String imageUrl;
	private String nutrTablUrl;
	
	public VariantBean() {}

	public VariantBean(long id, long productId, String sku, String size, double vat,
			double price, int quantity, String flavour, String imageUrl, String nutrTablUrl) {
		this.id = id;
		this.productId = productId;
		this.sku = sku;
		this.size = size;
		this.vat = vat;
		this.price = price;
		this.quantity = quantity;
		this.flavour = flavour;
		this.imageUrl = imageUrl;
		this.nutrTablUrl = nutrTablUrl;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
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

	public double getVat() {
		return vat;
	}

	public void setVat(double vat) {
		this.vat = vat;
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

	public String getNutrTablUrl() {
		return nutrTablUrl;
	}

	public void setNutrTablUrl(String nutrTablUrl) {
		this.nutrTablUrl = nutrTablUrl;
	}
	
}
