package it.unisa.backend.model.dao.impl;

import it.unisa.backend.model.dao.*;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.bean.AddressBean;
import it.unisa.backend.model.bean.PhoneBean;
import it.unisa.backend.model.bean.util.PhoneType;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDaoInterface{

    private final DataSource dataSource;

    public UserDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UserBean findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        UserBean user = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, email);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = new UserBean();
                    user.setEmail(resultSet.getString("email"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setPasswordHash(resultSet.getString("password_hash"));
                    user.setPasswordSalt(resultSet.getString("password_salt"));
                    user.setRole(resultSet.getString("role"));
                    
                    Date dbBirth = resultSet.getDate("date_of_birth");
                    if (dbBirth != null) user.setBirthDate(dbBirth.toLocalDate());
                    
                    Timestamp dbReg = resultSet.getTimestamp("registration_date");
                    if (dbReg != null) user.setRegistrationDate(dbReg.toLocalDateTime());
                    
                    user.setAddresses(findAddressesByEmail(connection, email));
                    user.setPhones(findPhonesByEmail(connection, email));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return user;
    }

    @Override
    public boolean isEmailExists(String email) {
        String query = "SELECT 1 FROM users WHERE email = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean save(UserBean user) {
        String query = "INSERT INTO users (email, first_name, last_name, password_hash, password_salt, role, date_of_birth) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getPasswordHash());
            preparedStatement.setString(5, user.getPasswordSalt());
            preparedStatement.setString(6, user.getRole() != null ? user.getRole() : "user");
            
            if (user.getBirthDate() != null) {
                preparedStatement.setDate(7, Date.valueOf(user.getBirthDate()));
            } 
            else {
                preparedStatement.setNull(7, Types.DATE);
            }
            
            return preparedStatement.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UserBean findById(String id) {
        return findByEmail(id); 
    }

    @Override
    public List<UserBean> findAll() {
    	
        String query = "SELECT * FROM users";
        List<UserBean> usersList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                UserBean user = new UserBean();
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setPasswordHash(resultSet.getString("password_hash"));
                user.setPasswordSalt(resultSet.getString("password_salt"));
                user.setRole(resultSet.getString("role"));
                
                Date dbBirth = resultSet.getDate("date_of_birth");
                if (dbBirth != null) user.setBirthDate(dbBirth.toLocalDate());
                
                Timestamp dbReg = resultSet.getTimestamp("registration_date");
                if (dbReg != null) user.setRegistrationDate(dbReg.toLocalDateTime());
                
                user.setAddresses(findAddressesByEmail(connection, user.getEmail()));
                user.setPhones(findPhonesByEmail(connection, user.getEmail()));
                
                usersList.add(user);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    @Override
    public boolean update(UserBean user) {
        String query = "UPDATE users SET first_name = ?, last_name = ?, password_hash = ?, password_salt = ?, role = ?, date_of_birth = ? WHERE email = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPasswordHash());
            preparedStatement.setString(4, user.getPasswordSalt());
            preparedStatement.setString(5, user.getRole());
            
            if (user.getBirthDate() != null) {
                preparedStatement.setDate(6, Date.valueOf(user.getBirthDate()));
            } 
            else {
                preparedStatement.setNull(6, Types.DATE);
            }
            preparedStatement.setString(7, user.getEmail());
            
            return preparedStatement.executeUpdate() > 0;
            
        } 
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String email) {
        String query = "DELETE FROM users WHERE email = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, email);
           
            return preparedStatement.executeUpdate() > 0;
            
        } 
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<AddressBean> findAddressesByEmail(Connection connection, String email) throws SQLException {
        String query = "SELECT id, user_email, zip_code, city, street, street_number, province, country FROM addresses WHERE user_email = ?";
        
        List<AddressBean> addresses = new ArrayList<>();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    addresses.add(new AddressBean(
                        resultSet.getLong("id"), resultSet.getString("user_email"),
                        resultSet.getString("zip_code"), resultSet.getString("city"),
                        resultSet.getString("street"), resultSet.getInt("street_number"),
                        resultSet.getString("province"), resultSet.getString("country")
                    ));
                }
            }
        }
        return addresses;
    }

    private List<PhoneBean> findPhonesByEmail(Connection connection, String email) throws SQLException {
        String query = "SELECT id, user_email, number, type FROM phones WHERE user_email = ?";
        
        List<PhoneBean> phones = new ArrayList<>();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    phones.add(new PhoneBean(
                        resultSet.getLong("id"), resultSet.getString("user_email"),
                        resultSet.getString("number"), PhoneType.valueOf(resultSet.getString("type"))
                    ));
                }
            }
        }
        return phones;
    }
}
