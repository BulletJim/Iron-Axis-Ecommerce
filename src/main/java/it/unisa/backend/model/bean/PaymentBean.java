package it.unisa.backend.model.bean;

import java.io.Serializable;
import java.time.*;
import it.unisa.backend.model.bean.util.PaymentStatus;

public class PaymentBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private long orderId;
	private LocalDateTime paymentDate;
	private String paymentMethod;
	private String transactionId;
	private PaymentStatus status;
	private double totalPrice;
	
	public PaymentBean() {}

	public PaymentBean(long id, long orderId, LocalDateTime paymentDate, 
			String paymentMethod, String transactionId, PaymentStatus status, double totalPrice) {
		this.id = id;
		this.orderId = orderId;
		this.paymentDate = paymentDate;
		this.paymentMethod = paymentMethod;
		this.transactionId = transactionId;
		this.status = status;
		this.totalPrice = totalPrice;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
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

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

}
