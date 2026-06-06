package it.unisa.backend.model.bean;

import java.io.Serializable;
import java.time.*;

public class ReviewBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String userEmail;
	private long productId;
	private String title;
	private int score;
	private String comment;
	private LocalDateTime reviewDate;
	
	public ReviewBean() {}

	public ReviewBean(String userEmail, long productId, String title, 
			int score, String comment, LocalDateTime date) {
		this.userEmail = userEmail;
		this.productId = productId;
		this.title = title;
		this.score = score;
		this.comment = comment;
		this.reviewDate = date;
	}
	
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(LocalDateTime reviewDate) {
		this.reviewDate = reviewDate;
	}

}
