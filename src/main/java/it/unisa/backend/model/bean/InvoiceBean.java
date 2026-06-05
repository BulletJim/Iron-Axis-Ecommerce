package it.unisa.backend.model.bean;

import java.io.Serializable;
import java.time.*;

public class InvoiceBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private int number;
	private LocalDateTime issueDate;    //Data emissione fattura 
	private String holderName;          //Nome intestatario
	private String vatNumber;           //Partita IVA dell'intestatario
	private double netAmount;           //Totale netto (senza IVA)
	private double taxAmount;           //Totale IVA 
	private double totalAmount;         //Importo totale 
	
	public InvoiceBean() {}

	public InvoiceBean(long id, int number, LocalDateTime issueDate, String holderName, String vatNumber,
			double netAmount, double taxAmount, double totalAmount) {
		this.id = id;
		this.number = number;
		this.issueDate = issueDate;
		this.holderName = holderName;
		this.vatNumber = vatNumber;
		this.netAmount = netAmount;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public LocalDateTime getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDateTime issueDate) {
		this.issueDate = issueDate;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
}
