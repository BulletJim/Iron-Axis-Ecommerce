package it.unisa.backend.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductBean implements Serializable{

    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String description;
    private CategoryBean category;
    private ArrayList<VariantBean> variants;
    private ArrayList<ReviewBean> reviews;

    public ProductBean() {
    	this.variants = new ArrayList<>();
    	this.reviews = new ArrayList<>();
    }

    public ProductBean(long id, String name, String description, CategoryBean category, ArrayList<VariantBean> variants, ArrayList<ReviewBean> reviews){
        this.id = id;
        this.description = description;
        this.category = category;
        this.variants = variants;
        this.reviews = reviews;
        
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CategoryBean getCategory() {
		return category;
	}

	public void setCategory(CategoryBean category) {
		this.category = category;
	}

	public ArrayList<VariantBean> getVariants() {
		return variants;
	}

	public void setVariants(ArrayList<VariantBean> variants) {
		this.variants = variants;
	}

	public ArrayList<ReviewBean> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<ReviewBean> reviews) {
		this.reviews = reviews;
	}
    
}




