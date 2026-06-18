package it.unisa.backend.model.dao;

import it.unisa.backend.model.bean.AddressBean;
import it.unisa.backend.model.bean.UserBean;

public interface UserDaoInterface extends CrudDao<UserBean, String> {
    
    UserBean findByEmail(String email);
    
    boolean isEmailExists(String email);
    
    boolean addAddressToUserByEmail(String email, AddressBean address);
}








