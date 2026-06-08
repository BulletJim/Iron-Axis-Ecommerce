package it.unisa.backend.model.dao.impl;

import it.unisa.backend.model.dao.ReviewDaoInterface;
import it.unisa.backend.model.bean.ReviewBean;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDaoImpl implements ReviewDaoInterface{

    private final DataSource dataSource;

    public ReviewDaoImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public boolean save(ReviewBean review){
    	
        String query = "INSERT INTO reviews (user_email, product_id, score, title, comment) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            
            ps.setString(1, review.getUserEmail());
            ps.setLong(2, review.getProductId());
            ps.setInt(3, review.getScore());
            ps.setString(4, review.getTitle());
            ps.setString(5, review.getComment());
            
            return ps.executeUpdate() > 0;
            
        } 
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ReviewBean findByUserAndProduct(String email, long productId){
    	
        String query = "SELECT * FROM reviews WHERE user_email = ? AND product_id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            
            ps.setString(1, email);
            ps.setLong(2, productId);
            
            try (ResultSet rs = ps.executeQuery()){
            	
                if (rs.next()) {
                    return extractReviewFromResultSet(rs);
                }
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ReviewBean> findByProductId(long productId){
    	
        String query = "SELECT * FROM reviews WHERE product_id = ? ORDER BY review_date DESC";
        
        List<ReviewBean> reviews = new ArrayList<>();
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)){
            
            ps.setLong(1, productId);
            
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                	
                    reviews.add(extractReviewFromResultSet(rs));
                }
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public List<ReviewBean> findByUserEmail(String email){
    	
        String query = "SELECT * FROM reviews WHERE user_email = ? ORDER BY review_date DESC";
        
        List<ReviewBean> reviews = new ArrayList<>();
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)){
            
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()){
            	
                while (rs.next()) {
                    reviews.add(extractReviewFromResultSet(rs));
                }
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public boolean update(ReviewBean review) {
        String query = "UPDATE reviews SET score = ?, title = ?, comment = ? WHERE user_email = ? AND product_id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            
            ps.setInt(1, review.getScore());
            ps.setString(2, review.getTitle());
            ps.setString(3, review.getComment());
            ps.setString(4, review.getUserEmail());
            ps.setLong(5, review.getProductId());
            
            return ps.executeUpdate() > 0;
            
        } 
        catch (SQLException e) {
            e.printStackTrace();
            
            return false;
        }
    }

    @Override
    public boolean delete(String id){
    
        throw new UnsupportedOperationException("Usa delete(String email, long productId) per le recensioni.");
    }
    
    public boolean delete(String email, long productId){
    	
        String query = "DELETE FROM reviews WHERE user_email = ? AND product_id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)){
        	
            ps.setString(1, email);
            ps.setLong(2, productId);
            
            return ps.executeUpdate() > 0;
            
        } 
        catch (SQLException e) {
            e.printStackTrace();
            
            return false;
        }
    }

    @Override
    public ReviewBean findById(String id){ 
    	return null;
    	}

    @Override
    public List<ReviewBean> findAll(){ 
    	return new ArrayList<>(); 
    	}

    private ReviewBean extractReviewFromResultSet(ResultSet rs) throws SQLException {
        return new ReviewBean(
        		
            rs.getString("user_email"),
            rs.getLong("product_id"),
            rs.getString("title"),
            rs.getInt("score"),
            rs.getString("comment"),
            rs.getTimestamp("review_date") != null ? rs.getTimestamp("review_date").toLocalDateTime() : null
        );
    }
}