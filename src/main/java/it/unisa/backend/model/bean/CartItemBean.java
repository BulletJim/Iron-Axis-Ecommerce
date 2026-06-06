package it.unisa.backend.model.bean;

import java.io.Serializable;

public class CartItemBean implements Serializable {
    
	private static final long serialVersionUID = 1L;
 
    private VariantBean variant; 
    private int selectedQuantity; 

    public CartItemBean() {}

    public CartItemBean(VariantBean variant, int selectedQuantity) {
        this.variant = variant;
        this.selectedQuantity = selectedQuantity;
    }

    public VariantBean getVariant() { return variant; }
    public void setVariant(VariantBean variant) { this.variant = variant; }

    public int getSelectedQuantity() { return selectedQuantity; }
    public void setSelectedQuantity(int selectedQuantity) { this.selectedQuantity = selectedQuantity; }
}
