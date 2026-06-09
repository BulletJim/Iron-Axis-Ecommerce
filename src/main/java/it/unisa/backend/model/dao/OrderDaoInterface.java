package it.unisa.backend.model.dao;

import it.unisa.backend.model.bean.OrderBean;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderDaoInterface extends CrudDao<OrderBean, Long>{
	
	List<OrderBean> findByUserEmail(String email);   //Metodo per lo storico ordini a un utente specifico
	
	List<OrderBean> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);  

}