package it.unisa.backend.model.bean;

import java.io.Serializable;
import it.unisa.backend.model.bean.util.OrderStatus;
import java.time.*;
import java.util.ArrayList;

public class OrderBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private OrderStatus status;
	private LocalDateTime createdAt;   //Data creazione ordine
	private double totalAmount;
	private UserBean user;            // L'utente che ha effettuato l'acquisto
    private AddressBean shippingAddress; // L'indirizzo dove spedire la merce
    private InvoiceBean invoice;      //Fattura associata
    private PaymentBean payment;
    private ArrayList<OrderItemBean> items;
    
    public OrderBean() {
    	this.items = new ArrayList<>();
    }

	public OrderBean(long id, OrderStatus status, LocalDateTime createdAt, double totalAmount, UserBean user,
			AddressBean shippingAddress, InvoiceBean invoice, PaymentBean payment, ArrayList<OrderItemBean> items) {
		this.id = id;
		this.status = status;
		this.createdAt = createdAt;
		this.totalAmount = totalAmount;
		this.user = user;
		this.shippingAddress = shippingAddress;
		this.invoice = invoice;
		this.payment = payment;
		this.items = items;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public AddressBean getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(AddressBean shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public InvoiceBean getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoiceBean invoice) {
		this.invoice = invoice;
	}

	public PaymentBean getPayment() {
		return payment;
	}

	public void setPayment(PaymentBean payment) {
		this.payment = payment;
	}

	public ArrayList<OrderItemBean> getItems() {
		return items;
	}

	public void setItems(ArrayList<OrderItemBean> items) {
		this.items = items;
	}
	
}
