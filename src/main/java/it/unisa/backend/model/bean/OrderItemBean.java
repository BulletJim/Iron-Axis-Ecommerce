package it.unisa.backend.model.bean;

import java.io.Serializable;

public class OrderItemBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long orderId;
    private int quantity;
    private double vat;
    private double priceAtPurchase;
    private VariantBean variant;

    public OrderItemBean() {}

	public OrderItemBean(long orderId, int quantity, double vat, 
			double priceAtPurchase, VariantBean variant) {
		this.orderId = orderId;
		this.quantity = quantity;
		this.vat = vat;
		this.priceAtPurchase = priceAtPurchase;
		this.variant = variant;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getVat() {
		return vat;
	}

	public void setVat(double vat) {
		this.vat = vat;
	}

	public double getPriceAtPurchase() {
		return priceAtPurchase;
	}

	public void setPriceAtPurchase(double priceAtPurchase) {
		this.priceAtPurchase = priceAtPurchase;
	}

	public VariantBean getVariant() {
		return variant;
	}

	public void setVariant(VariantBean variant) {
		this.variant = variant;
	}  
}
