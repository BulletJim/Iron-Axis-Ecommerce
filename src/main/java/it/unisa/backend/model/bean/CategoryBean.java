package it.unisa.backend.model.bean;

import java.io.Serializable;

public class CategoryBean implements Serializable {
	
private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private String macroCategory;
	private String description;
	
	public CategoryBean() {}

	public CategoryBean(long id, String name, String macroCategory, String description) {
		this.id = id;
		this.name = name;
		this.macroCategory = macroCategory;
		this.description = description;
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
	
	public String getMacroCategory() {
	    return macroCategory;
	}

	public void setMacroCategory(String macroCategory) {
	    this.macroCategory = macroCategory;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
