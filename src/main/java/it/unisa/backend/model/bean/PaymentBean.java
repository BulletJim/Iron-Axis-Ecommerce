package it.unisa.backend.model.bean;

import java.io.Serializable;
import java.time.*;
import it.unisa.backend.model.bean.util.PaymentStatus;

public class PaymentBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private LocalDateTime paymentDate;
	private String paymentMethod;
	private String transactionCode;  // Il codice di transazione della banca/PayPal
	private PaymentStatus status;
	private double amount;
	
	public PaymentBean() {}

	public PaymentBean(long id, LocalDateTime paymentDate, String paymentMethod, String transactionCode,
			PaymentStatus status, double amount) {
		this.id = id;
		this.paymentDate = paymentDate;
		this.paymentMethod = paymentMethod;
		this.transactionCode = transactionCode;
		this.status = status;
		this.amount = amount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
