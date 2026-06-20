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
    public boolean saveVariant(VariantBean variant) {
    	
    	String query = "INSERT INTO variants (sku, product_id, size, price, vat, quantity, image_url, flavour, nutr_tabl_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    	
    	try(Connection conn = dataSource.getConnection();
    		PreparedStatement prepStat = conn.prepareStatement(query)){
    		
    		prepStat.setString(1, variant.getSku());
    		prepStat.setLong(2, variant.getProductId());
    		prepStat.setString(3, variant.getSize());
    		prepStat.setDouble(4, variant.getPrice());
    		prepStat.setDouble(5, variant.getVat());
    		prepStat.setInt(6, variant.getQuantity());
    		prepStat.setString(7, variant.getImageUrl());
    		prepStat.setString(8, variant.getFlavour());
    		prepStat.setString(9, variant.getNutrTablUrl());
    		
    		return prepStat.executeUpdate() > 0;
    		
    	}catch(SQLException e) {
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
    public List<CategoryBean> findAllCategories(){
    	
    	String query = "SELECT * FROM categories";
        
        List<CategoryBean> categories = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                categories.add( new CategoryBean(
                		resultSet.getLong("id"),
                		resultSet.getString("name"),
                		resultSet.getString("macro_category"),
                		resultSet.getString("description")
                		));
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
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

    public List<ProductBean> searchProducts(String searchQuery) {
        List<ProductBean> products = new ArrayList<>();
        
        String query = "SELECT * FROM products WHERE name LIKE ? AND is_deleted = false LIMIT 6";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, "%" + searchQuery + "%");
            
            try (ResultSet rs = preparedStatement.executeQuery()) {
            	
                while (rs.next()) {
                    ProductBean product = new ProductBean();
                    product.setId(rs.getLong("id"));
                    product.setName(rs.getString("name"));
                    
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return products;
    }
    
    @Override
    public VariantBean findVariantBySku(String sku) {
    	
    	String query = "SELECT * FROM variants WHERE sku = ?;";
    	VariantBean variant = null;
    	
    	try(Connection connection = dataSource.getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(query)){
    		
    		preparedStatement.setString(1, sku);
    		
    		try(ResultSet resultSet = preparedStatement.executeQuery()){
    			if(resultSet.next()) {
    				variant = new VariantBean(resultSet.getLong("id"),
    											resultSet.getLong("product_id"),
    											resultSet.getString("sku"),
    											resultSet.getString("size"),
    											resultSet.getDouble("vat"),
    											resultSet.getDouble("price"),
    											resultSet.getInt("quantity"),
    											resultSet.getString("flavour"),
    											resultSet.getString("image_url"),
    											resultSet.getString("nutr_tabl_url")
    										 );
    				
    			}
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	return variant;
    }
    
    @Override
    public List<ProductBean> getProductsByFilters(Long categoryId, Double maxPrice, String sortBy, boolean onlyAvailable) {
        List<ProductBean> products = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(
            "SELECT p.* " +
            "FROM products p " +
            "JOIN categories c ON p.category_id = c.id " +
            "JOIN variants v ON p.id = v.product_id " +
            "WHERE p.is_deleted = false"
        );

        if (categoryId != null && categoryId > 0) {
            sql.append(" AND c.id = ?");
        }
        
        if (maxPrice != null) {
            sql.append(" AND v.price <= ?");
        }
        
        if (onlyAvailable) {
            sql.append(" AND v.quantity > 0");
        }
        
        sql.append(" GROUP BY p.id");

        if (sortBy != null) {
            switch (sortBy) {
                case "price_asc":
                    sql.append(" ORDER BY MIN(v.price) ASC");
                    break;
                case "price_desc":
                    sql.append(" ORDER BY MIN(v.price) DESC");
                    break;
                case "rating":
                    this.doRetrieveTopRated(5);
                    break;
                case "default":
                default:
                    sql.append(" ORDER BY p.id DESC");
                    break;
            }
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            
            if (categoryId != null && categoryId > 0) {
                preparedStatement.setLong(paramIndex++, categoryId);
            }
            
            if (maxPrice != null) {
                preparedStatement.setDouble(paramIndex++, maxPrice);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(extractProductFromResultSet(resultSet, connection));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return products;
    }
    
    
    @Override
    public List<ProductBean> doRetrieveTopRated(int limit) {
        List<ProductBean> products = new ArrayList<>();
        String query = "SELECT p.*, AVG(r.score) as media_voti " +
                       "FROM products p " +
                       "JOIN reviews r ON p.id = r.product_id " +
                       "WHERE p.is_deleted = false " +
                       "GROUP BY p.id " +
                       "ORDER BY media_voti DESC " +
                       "LIMIT ?";

        try (Connection con = dataSource.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
             
        	ps.setInt(1, limit);
        	try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    products.add(extractProductFromResultSet(resultSet, con));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return products;
    }
    
    @Override
    public List<ProductBean> doRetrieveSuggested(int limit) {
        List<ProductBean> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE is_deleted = false ORDER BY RAND() LIMIT ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
             
            ps.setInt(1, limit);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    products.add(extractProductFromResultSet(resultSet, con));
                }
            }

        } catch (SQLException e) {
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
                	
                    return new CategoryBean(rs.getLong("id"), rs.getString("macro_category"), rs.getString("name"), rs.getString("description"));
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
                        rs.getString("image_url"),
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