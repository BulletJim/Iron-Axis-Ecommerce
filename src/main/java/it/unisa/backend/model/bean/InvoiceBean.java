package it.unisa.backend.model.bean;

import java.io.Serializable;
import java.time.*;

public class InvoiceBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String number;
	private LocalDateTime issueDate;
	private String holderFirstName;
	private String holderLastName;
	private AddressBean billingAddress;         
	private double taxableAmount;
	private double totalAmount;
	
	public InvoiceBean() {}

	public InvoiceBean(long id, String number, LocalDateTime issueDate, 
			String holderFirstName, String holderLastName, AddressBean billingAddress, double taxableAmount, double totalAmount) {
		this.id = id;
		this.number = number;
		this.issueDate = issueDate;
		this.holderFirstName = holderFirstName;
		this.holderLastName = holderLastName;
		this.billingAddress = billingAddress;
		this.taxableAmount = taxableAmount;
		this.totalAmount = totalAmount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public LocalDateTime getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDateTime issueDate) {
		this.issueDate = issueDate;
	}

	public String getHolderFirstName() {
		return holderFirstName;
	}

	public void setHolderFirstName(String holderFirstName) {
		this.holderFirstName = holderFirstName;
	}
	
	public String getHolderLastName() {
		return holderLastName;
	}

	public void setHolderLastName(String holderLastName) {
		this.holderLastName = holderLastName;
	}
	
	public AddressBean getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(AddressBean billingAddress) {
		this.billingAddress = billingAddress;
	}

	public double getTaxableAmount() {
		return taxableAmount;
	}

	public void setTaxableAmount(double taxableAmount) {
		this.taxableAmount = taxableAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
}
