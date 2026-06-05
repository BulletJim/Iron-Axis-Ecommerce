package it.unisa.backend.model.bean;

import java.io.Serializable;
import java.time.*;

public class ReviewBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String title;
	private int score;
	private String comment;
	private LocalDateTime date;
	
	public ReviewBean() {}

	public ReviewBean(String title, int score, String comment, LocalDateTime date) {
		this.title = title;
		this.score = score;
		this.comment = comment;
		this.date = date;
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

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

}
