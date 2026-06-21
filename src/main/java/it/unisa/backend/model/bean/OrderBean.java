package it.unisa.backend.model.bean;

import java.io.Serializable;
import it.unisa.backend.model.bean.util.OrderStatus;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class OrderBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private OrderStatus status;
	private LocalDateTime createdAt;
	private double totalAmount;
	private double shippingCosts;
	private UserBean user;            
    private AddressBean shippingAddress;
    private InvoiceBean invoice;
    private PaymentBean payment;
    private List<OrderItemBean> items;
    
    public OrderBean() {
    	this.items = new ArrayList<>();
    }

	

	public OrderBean(long id, OrderStatus status, LocalDateTime createdAt, double totalAmount, double shippingCosts,
			UserBean user, AddressBean shippingAddress, InvoiceBean invoice, PaymentBean payment,
			List<OrderItemBean> items) {
		this.id = id;
		this.status = status;
		this.createdAt = createdAt;
		this.totalAmount = totalAmount;
		this.shippingCosts = shippingCosts;
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

	public List<OrderItemBean> getItems() {
		return items;
	}

	public void setItems(List<OrderItemBean> items) {
		this.items = items;
	}

	public double getShippingCosts() {
		return shippingCosts;
	}

	public void setShippingCosts(double shippingCosts) {
		this.shippingCosts = shippingCosts;
	}
	
}
