package it.unisa.backend.model.dao.impl;

import it.unisa.backend.model.dao.OrderDaoInterface;
import it.unisa.backend.model.bean.*;
import it.unisa.backend.model.bean.util.OrderStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements OrderDaoInterface{

    private final DataSource dataSource;

    public OrderDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean save(OrderBean order){
    	
    	String queryOrder = "INSERT INTO orders (user_email, shipping_address_id, status, total_items, total_price) VALUES (?, ?, ?, ?, ?)";
        String queryDetails = "INSERT INTO order_details (order_id, variant_id, quantity, purchase_price, vat) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false); 

            try (PreparedStatement psOrder = connection.prepareStatement(queryOrder, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psDetails = connection.prepareStatement(queryDetails)) {
                
                psOrder.setString(1, order.getUser().getEmail());
                psOrder.setLong(2, order.getShippingAddress().getId());
                psOrder.setString(3, order.getStatus().name());
                
                int totalItems = order.getItems().stream().mapToInt(OrderItemBean::getQuantity).sum();
                psOrder.setInt(4, totalItems);
                psOrder.setDouble(5, order.getTotalAmount());
                psOrder.executeUpdate();
                
                try (ResultSet rs = psOrder.getGeneratedKeys()) {
                    if (rs.next()) {
                        order.setId(rs.getLong(1)); 
                    } else {
                        throw new SQLException("No ID generated for this order");
                    }
                }

                if (order.getItems() != null) {
                    for (OrderItemBean item : order.getItems()) {
                        psDetails.setLong(1, order.getId()); 
                        psDetails.setLong(2, item.getVariant().getId());
                        psDetails.setInt(3, item.getQuantity());
                        psDetails.setDouble(4, item.getPriceAtPurchase()); 
                        psDetails.setDouble(5, item.getVat()); 
                        psDetails.addBatch(); 
                    }
                    psDetails.executeBatch();
                }

                if (order.getPayment() != null) {
                    savePayment(connection, order);
                }

                if (order.getInvoice() != null) {
                    saveInvoice(connection, order);
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
    public OrderBean findById(Long id) {
       
        String query = "SELECT * FROM orders WHERE id = ?";
        OrderBean order = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    order = extractOrderFromResultSet(rs, connection);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public List<OrderBean> findAll() {
      
        String query = "SELECT * FROM orders ORDER BY order_date DESC";
        
        List<OrderBean> orders = new ArrayList<>();
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()){
        	
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs, connection));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public boolean update(OrderBean order) {
        
        String query = "UPDATE orders SET status = ? WHERE id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, order.getStatus().toString());
            ps.setLong(2, order.getId());
            
            return ps.executeUpdate() > 0;
            
        } 
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        
        String query = "DELETE FROM orders WHERE id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, id);
            
            return ps.executeUpdate() > 0;
            
        } 
        catch (SQLException e) {
            e.printStackTrace();
            
            return false;
        }
    }

    @Override
    public List<OrderBean> findByUserEmail(String email){
    	
        String query = "SELECT * FROM orders WHERE user_email = ? ORDER BY order_date DESC";
        
        List<OrderBean> orders = new ArrayList<>();
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)){
        	
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()){
            	
                while (rs.next()) {
                    orders.add(extractOrderFromResultSet(rs, connection));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<OrderBean> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate){
    	
        String query = "SELECT * FROM orders WHERE order_date BETWEEN ? AND ? ORDER BY order_date DESC";
        
        List<OrderBean> orders = new ArrayList<>();
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)){
        	
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));
            
            try (ResultSet rs = ps.executeQuery()){
            	
                while (rs.next()) {
                    orders.add(extractOrderFromResultSet(rs, connection));
                }
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    private OrderBean extractOrderFromResultSet(ResultSet rs, Connection connection) throws SQLException {
        OrderBean order = new OrderBean();
        order.setId(rs.getLong("id"));
        order.setTotalAmount(rs.getDouble("total_price"));
        
        try {
            order.setStatus(OrderStatus.valueOf(rs.getString("status").toUpperCase().trim()));
        } catch (IllegalArgumentException | NullPointerException e) {
            order.setStatus(OrderStatus.PENDING); // Fallback Value
        }

        if (rs.getTimestamp("order_date") != null) {
            order.setCreatedAt(rs.getTimestamp("order_date").toLocalDateTime());
        }

        UserBean user = new UserBean();
        user.setEmail(rs.getString("user_email"));
        order.setUser(user);

        AddressBean address = new AddressBean();
        address.setId(rs.getLong("shipping_address_id"));
        order.setShippingAddress(address);

        order.setItems(findOrderItemsByOrderId(connection, order.getId()));
        
        return order;
    }

    private List<OrderItemBean> findOrderItemsByOrderId(Connection connection, Long orderId) throws SQLException {
        String query = "SELECT * FROM order_details WHERE order_id = ?";
        List<OrderItemBean> items = new ArrayList<>();
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItemBean item = new OrderItemBean();
                    item.setOrderId(rs.getLong("order_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPriceAtPurchase(rs.getDouble("purchase_price"));
                    item.setVat(rs.getDouble("vat"));
                    
                    VariantBean variant = new VariantBean();
                    variant.setId(rs.getLong("variant_id"));
                    item.setVariant(variant);
                    
                    items.add(item);
                }
            }
        }
        return items;
    }

    private void savePayment(Connection connection, OrderBean order) throws SQLException {
        String queryPayment = "INSERT INTO payments (order_id, payment_method, transaction_id, total_price, payment_status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement psPayment = connection.prepareStatement(queryPayment)) {
            psPayment.setLong(1, order.getId());
            psPayment.setString(2, order.getPayment().getPaymentMethod());
            psPayment.setString(3, order.getPayment().getTransactionId());
            psPayment.setDouble(4, order.getPayment().getTotalPrice());
            psPayment.setString(5, "COMPLETED"); 
            psPayment.executeUpdate();
        }
    }

    private void saveInvoice(Connection connection, OrderBean order) throws SQLException {
        String queryInvoice = "INSERT INTO invoices (order_id, invoice_number, holder_first_name, holder_last_name, billing_address_id, taxable_total, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement psInvoice = connection.prepareStatement(queryInvoice)) {
            psInvoice.setLong(1, order.getId());
            psInvoice.setString(2, order.getInvoice().getNumber());
            psInvoice.setString(3, order.getInvoice().getHolderFirstName());
            psInvoice.setString(4, order.getInvoice().getHolderLastName());
            psInvoice.setLong(5, order.getInvoice().getBillingAddress().getId()); 
            psInvoice.setDouble(6, order.getInvoice().getTaxableAmount());
            psInvoice.setDouble(7, order.getInvoice().getTotalAmount());
            psInvoice.executeUpdate();
        }
    }
    
}