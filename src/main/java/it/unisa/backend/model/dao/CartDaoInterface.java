package it.unisa.backend.model.dao;

import it.unisa.backend.model.bean.CartBean;

public interface CartDaoInterface extends CrudDao<CartBean, Long>{
    
    CartBean findByUserEmail(String email);
    
    boolean clearCart(long cartId);
}