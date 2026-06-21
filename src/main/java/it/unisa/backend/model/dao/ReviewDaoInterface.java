package it.unisa.backend.model.dao;

import it.unisa.backend.model.bean.ReviewBean;
import java.util.List;

public interface ReviewDaoInterface extends CrudDao<ReviewBean, String>{
	
	ReviewBean findByUserAndProduct(String email, long productId);  //Metodo per trovare una specifica recensione
	
	List<ReviewBean> findByProductId(long productId);    //Metodo per la pagina del prodotto che carica tutti i commenti 
	
	List<ReviewBean> findByUserEmail(String email);    

}