package it.unisa.backend.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductBean implements Serializable{

    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String description;
    private CategoryBean category;
    private List<VariantBean> variants;
    private List<ReviewBean> reviews;

    public ProductBean() {
    	this.variants = new ArrayList<>();
    	this.reviews = new ArrayList<>();
    }

    public ProductBean(long id, String name, String description, 
    		CategoryBean category, List<VariantBean> variants, List<ReviewBean> reviews){
        this.id = id;
        this.name = name;
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

	public List<VariantBean> getVariants() {
		return variants;
	}

	public void setVariants(List<VariantBean> variants) {
		this.variants = variants;
	}

	public List<ReviewBean> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewBean> reviews) {
		this.reviews = reviews;
	}
    
}




