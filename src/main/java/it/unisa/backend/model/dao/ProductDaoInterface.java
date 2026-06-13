package it.unisa.backend.model.dao;

import it.unisa.backend.model.bean.ProductBean;
import it.unisa.backend.model.bean.VariantBean;

import java.util.List;

public interface ProductDaoInterface extends CrudDao<ProductBean, Long> {
	
	List<ProductBean> findByCategory(long categoryId);
	
	List<ProductBean> searchProducts(String searchQuery);  //Metodo per Ajax
	
	VariantBean findVariantBySku(String sku);

}
