package it.unisa.backend.model.bean.dto;

public class GuestCartItemDTO {
	
	private String sku;
	private int quantity;
	
	public GuestCartItemDTO() {}
	
	public GuestCartItemDTO(String sku, int quantity) {
		this.sku = sku;
		this.quantity = quantity;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	
}
