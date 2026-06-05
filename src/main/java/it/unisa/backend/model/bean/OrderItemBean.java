package it.unisa.backend.model.bean;

import java.io.Serializable;

public class OrderItemBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long orderId;
    private int quantity;              //Quanti pezzi di questo prodotto ha comprato
    private double priceAtPurchase;
    private VariantBean variant;

    public OrderItemBean() {}

	public OrderItemBean(long orderId, int quantity, double priceAtPurchase, VariantBean variant) {
		this.orderId = orderId;
		this.quantity = quantity;
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
