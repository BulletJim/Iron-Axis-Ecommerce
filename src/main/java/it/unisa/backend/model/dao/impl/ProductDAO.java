package it.unisa.backend.model.dao.impl;

import it.unisa.backend.model.dao.*;
import it.unisa.backend.model.bean.ProductBean;
import it.unisa.backend.model.bean.CategoryBean;
import it.unisa.backend.model.bean.VariantBean;
import it.unisa.backend.model.bean.ReviewBean;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements ProductDaoInterface {

    private final DataSource dataSource;

    public ProductDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean save(ProductBean product) {

        String query = "INSERT INTO products (category_id, name, description) VALUES (?, ?, ?)";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setLong(1, product.getCategory().getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setString(3, product.getDescription());
            
            return preparedStatement.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ProductBean findById(Long id) {
       
        String query = "SELECT * FROM products WHERE id = ? AND is_deleted = false";
        
        ProductBean product = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setLong(1, id);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    product = extractProductFromResultSet(resultSet, connection);
                }
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public List<ProductBean> findAll() {
        String query = "SELECT * FROM products WHERE is_deleted = false";
        
        List<ProductBean> products = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                products.add(extractProductFromResultSet(resultSet, connection));
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public boolean update(ProductBean product) {
    	
        String query = "UPDATE products SET category_id = ?, name = ?, description = ? WHERE id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setLong(1, product.getCategory().getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setLong(4, product.getId());
            
            return preparedStatement.executeUpdate() > 0;
            
        } 
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
      
        String query = "UPDATE products SET is_deleted = true WHERE id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setLong(1, id);
            
            return preparedStatement.executeUpdate() > 0;
            
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ProductBean> findByCategory(long categoryId){
    	
        String query = "SELECT * FROM products WHERE category_id = ? AND is_deleted = false";
        
        List<ProductBean> products = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setLong(1, categoryId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(extractProductFromResultSet(resultSet, connection));
                }
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<ProductBean> searchProducts(String searchQuery) {
      
        String query = "SELECT * FROM products WHERE (name LIKE ? OR description LIKE ?) AND is_deleted = false";
        
        List<ProductBean> products = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            String searchPattern = "%" + searchQuery + "%";
            
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(extractProductFromResultSet(resultSet, connection));
                }
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    
    // Utility method 
    private ProductBean extractProductFromResultSet(ResultSet resultSet, Connection connection) throws SQLException {
        ProductBean product = new ProductBean();
        product.setId(resultSet.getLong("id"));
        product.setName(resultSet.getString("name"));
        product.setDescription(resultSet.getString("description"));
        product.setCategory(findCategoryById(connection, resultSet.getLong("category_id")));
        product.setVariants(findVariantsByProductId(connection, product.getId()));
        product.setReviews(findReviewsByProductId(connection, product.getId()));
        return product;
    }

    private CategoryBean findCategoryById(Connection connection, long categoryId) throws SQLException{
    	
        String query = "SELECT * FROM categories WHERE id = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, categoryId);
            
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()){
                	
                    return new CategoryBean(rs.getLong("id"), rs.getString("name"), rs.getString("description"));
                }
            }
        }
        return null;
    }

    private List<VariantBean> findVariantsByProductId(Connection connection, long productId) throws SQLException{
    	
        String query = "SELECT * FROM variants WHERE product_id = ?";
        
        List<VariantBean> variants = new ArrayList<>();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1, productId);
            
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                	
                    variants.add(new VariantBean(
                        rs.getLong("id"),
                        rs.getLong("product_id"), 
                        rs.getString("sku"),
                        rs.getString("size"),
                        rs.getDouble("vat"), 
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("flavour"), 
                        rs.getString("url_image"),
                        rs.getString("nutr_tabl_url")
                    ));
                }
            }
        }
        return variants;
    }

    private List<ReviewBean> findReviewsByProductId(Connection connection, long productId) throws SQLException{
    	
        String query = "SELECT * FROM reviews WHERE product_id = ?";
        
        List<ReviewBean> reviews = new ArrayList<>();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1, productId);
            
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                	
                    reviews.add(new ReviewBean(
                        rs.getString("user_email"), 
                        rs.getLong("product_id"),
                        rs.getString("title"),
                        rs.getInt("score"),
                        rs.getString("comment"), 
                        rs.getTimestamp("review_date") != null ? rs.getTimestamp("review_date").toLocalDateTime() : null
                    ));
                }
            }
        }
        return reviews;
    }
}