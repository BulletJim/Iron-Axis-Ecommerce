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

public class CartDaoImpl implements CartDaoInterface{

    private final DataSource dataSource;

    public CartDaoImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public boolean save(CartBean cart){
    	
        Connection connection = null;
        
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false); 

            String queryCart = "INSERT INTO carts (user_email, total_price) VALUES (?, ?)";
            
            try (PreparedStatement psCart = connection.prepareStatement(queryCart, Statement.RETURN_GENERATED_KEYS)){
                psCart.setString(1, cart.getUserEmail());
                psCart.setDouble(2, cart.getTotalPrice());
                psCart.executeUpdate();
                
                try (ResultSet rs = psCart.getGeneratedKeys()){
                	
                    if (rs.next()){
                        cart.setId(rs.getLong(1));
                    }
                }
            }

            String queryItems = "INSERT INTO cart_variants (cart_id, variant_id, quantity) VALUES (?, ?, ?)";
            
            try (PreparedStatement psItems = connection.prepareStatement(queryItems)){
            	
                for (CartItemBean item : cart.getVariants().values()){
                	
                    psItems.setLong(1, cart.getId());
                    psItems.setLong(2, item.getVariant().getId());
                    psItems.setInt(3, item.getSelectedQuantity());
                    psItems.addBatch();
                }
                psItems.executeBatch();
            }

            connection.commit();
            
            return true;

        } 
        catch (SQLException e){
            e.printStackTrace();
            
            if (connection != null) try{ 
            	connection.rollback();
            	}
            
            catch (SQLException ex){
            	ex.printStackTrace(); 
            	}
            
            return false;
            
        } finally{
            if (connection != null) try { 
            	connection.setAutoCommit(true); 
            	connection.close();
            	} 
            catch (SQLException e){
            	e.printStackTrace(); 
            	}
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
    	
        Connection connection = null;
        
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false); 

            String queryCart = "UPDATE carts SET total_price = ? WHERE id = ?";
            
            try (PreparedStatement psCart = connection.prepareStatement(queryCart)){
            	
                psCart.setDouble(1, cart.getTotalPrice());
                psCart.setLong(2, cart.getId());
                psCart.executeUpdate();
            }

            String queryDeleteOld = "DELETE FROM cart_variants WHERE cart_id = ?";
            
            try (PreparedStatement psDelete = connection.prepareStatement(queryDeleteOld)){
                psDelete.setLong(1, cart.getId());
                psDelete.executeUpdate();
            }

            if (!cart.getVariants().isEmpty()){
            	
                String queryItems = "INSERT INTO cart_variants (cart_id, variant_id, quantity) VALUES (?, ?, ?)";
                
                try (PreparedStatement psItems = connection.prepareStatement(queryItems)){
                	
                    for (CartItemBean item : cart.getVariants().values()){
                    	
                        psItems.setLong(1, cart.getId());
                        psItems.setLong(2, item.getVariant().getId());
                        psItems.setInt(3, item.getSelectedQuantity());
                        psItems.addBatch();
                    }
                    psItems.executeBatch();
                }
            }

            connection.commit();
            
            return true;

        } 
        catch (SQLException e){
        	
            e.printStackTrace();
            
            if (connection != null) try { 
            	connection.rollback();
            	} 
            
            catch (SQLException ex){
            	ex.printStackTrace();
            	}
            
            return false;
            
        } finally{
        	
            if (connection != null) try { 
            	connection.setAutoCommit(true); 
            	connection.close(); 
            	}
            
            catch (SQLException e){
            	e.printStackTrace(); 
            	}
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
    	
        String query = "DELETE FROM cart_variants WHERE cart_id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)){
            ps.setLong(1, cartId);
            ps.executeUpdate();
            
            try (PreparedStatement psUpdate = connection.prepareStatement("UPDATE carts SET total_price = 0.0 WHERE id = ?")){
                psUpdate.setLong(1, cartId);
                
                return psUpdate.executeUpdate() > 0;
            }
        } 
        catch (SQLException e){
        	
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

    private Map<Integer, CartItemBean> findCartItemsByCartId(Connection connection, long cartId) throws SQLException{

        String query = "SELECT cv.quantity as selected_qty, v.* FROM cart_variants cv JOIN variants v ON cv.variant_id = v.id WHERE cv.cart_id = ?";
        
        Map<Integer, CartItemBean> itemsMap = new HashMap<>();
        
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
                        rs.getString("url_image")
                    );
                    
                    CartItemBean item = new CartItemBean(variant, rs.getInt("selected_qty"));
                    
                    itemsMap.put((int) variant.getId(), item);
                }
            }
        }
        return itemsMap;
    }
}