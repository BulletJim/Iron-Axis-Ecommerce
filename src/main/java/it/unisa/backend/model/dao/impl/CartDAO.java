package it.unisa.backend.model.dao.impl;

import it.unisa.backend.model.dao.CartDaoInterface;
import it.unisa.backend.model.bean.CartBean;
import it.unisa.backend.model.bean.CartItemBean;
import it.unisa.backend.model.bean.VariantBean;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartDAO implements CartDaoInterface{

    private final DataSource dataSource;

    public CartDAO(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public boolean save(CartBean cart){
    	
    	String queryCart = "INSERT INTO carts (user_email, total_price) VALUES (?, ?)";
        String queryItems = "INSERT INTO cart_variants (cart_id, variant_id, quantity) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false); 

            try (PreparedStatement psCart = connection.prepareStatement(queryCart, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psItems = connection.prepareStatement(queryItems)) {
                
                psCart.setString(1, cart.getUserEmail());
                psCart.setDouble(2, cart.getTotalPrice());
                psCart.executeUpdate();
                
                try (ResultSet rs = psCart.getGeneratedKeys()) {
                    if (rs.next()) {
                        cart.setId(rs.getLong(1));
                    }
                }

                if (cart.getVariants() != null) {
                    for (CartItemBean item : cart.getVariants().values()) {
                        psItems.setLong(1, cart.getId());
                        psItems.setLong(2, item.getVariant().getId());
                        psItems.setInt(3, item.getSelectedQuantity());
                        psItems.addBatch();
                    }
                    psItems.executeBatch();
                }

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CartBean findByUserEmail(String email){
    	
        String query = "SELECT * FROM carts WHERE user_email = ?";
        CartBean cart = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)){
            
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()){
            	
                if (rs.next()){
                	
                    cart = new CartBean();
                    cart.setId(rs.getLong("id"));
                    cart.setUserEmail(rs.getString("user_email"));
                    cart.setTotalPrice(rs.getDouble("total_price"));
                    
                    if (rs.getTimestamp("creation_date") != null){
                        cart.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                    }
                    
                    cart.setVariants(findCartItemsByCartId(connection, cart.getId()));
                }
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }

    @Override
    public boolean update(CartBean cart){
    	
    	String queryCart = "UPDATE carts SET total_price = ? WHERE id = ?";
        String queryDeleteOld = "DELETE FROM cart_variants WHERE cart_id = ?";
        String queryItems = "INSERT INTO cart_variants (cart_id, variant_id, quantity) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false); 

            try (PreparedStatement psCart = connection.prepareStatement(queryCart);
                 PreparedStatement psDelete = connection.prepareStatement(queryDeleteOld);
                 PreparedStatement psItems = connection.prepareStatement(queryItems)) {
                
                psCart.setDouble(1, cart.getTotalPrice());
                psCart.setLong(2, cart.getId());
                psCart.executeUpdate();

                psDelete.setLong(1, cart.getId());
                psDelete.executeUpdate();

                if (cart.getVariants() != null && !cart.getVariants().isEmpty()) {
                    for (CartItemBean item : cart.getVariants().values()) {
                        psItems.setLong(1, cart.getId());
                        psItems.setLong(2, item.getVariant().getId());
                        psItems.setInt(3, item.getSelectedQuantity());
                        psItems.addBatch();
                    }
                    psItems.executeBatch();
                }

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
     
        String query = "DELETE FROM carts WHERE id = ?";
        
        try (Connection connection = dataSource.getConnection();
        		
             PreparedStatement ps = connection.prepareStatement(query)){
            ps.setLong(1, id);
            
            return ps.executeUpdate() > 0;
            
        } 
        catch (SQLException e){
            e.printStackTrace();
            
            return false;
        }
    }

    @Override
    public boolean clearCart(long cartId){
    	
    	String queryDelete = "DELETE FROM cart_variants WHERE cart_id = ?";
        String queryUpdate = "UPDATE carts SET total_price = 0.0 WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement psDelete = connection.prepareStatement(queryDelete);
                 PreparedStatement psUpdate = connection.prepareStatement(queryUpdate)) {
                
                psDelete.setLong(1, cartId);
                psDelete.executeUpdate();
                
                psUpdate.setLong(1, cartId);
                psUpdate.executeUpdate();

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CartBean findById(Long id) { 
    	return null; 
    	}

    @Override
    public List<CartBean> findAll() { 
    	return new ArrayList<>(); 
    	}

    private Map<Long, CartItemBean> findCartItemsByCartId(Connection connection, long cartId) throws SQLException{

        String query = "SELECT cv.quantity as selected_qty, v.* FROM cart_variants cv JOIN variants v ON cv.variant_id = v.id WHERE cv.cart_id = ?";
        
        Map<Long, CartItemBean> itemsMap = new HashMap<>();
        
        try (PreparedStatement ps = connection.prepareStatement(query)){
            ps.setLong(1, cartId);
            
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    VariantBean variant = new VariantBean(
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
                    );
                    
                    CartItemBean item = new CartItemBean(variant, rs.getInt("selected_qty"));
                    
                    itemsMap.put(variant.getId(), item);
                }
            }
        }
        return itemsMap;
    }
}