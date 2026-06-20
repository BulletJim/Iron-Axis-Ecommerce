package it.unisa.backend.model.dao;

import it.unisa.backend.model.bean.ProductBean;
import it.unisa.backend.model.bean.VariantBean;

import java.util.List;

public interface ProductDaoInterface extends CrudDao<ProductBean, Long> {
	
	List<ProductBean> findByCategory(long categoryId);
	
	List<ProductBean> searchProducts(String searchQuery);  //Metodo per Ajax
	
	VariantBean findVariantBySku(String sku);

	List<ProductBean> getProductsByFilters(Long categoryId, Double maxPrice, String sortBy, boolean onlyAvailable);
	
	List<ProductBean> doRetrieveSuggested(int limit);

	List<ProductBean> doRetrieveTopRated(int limit);

	boolean saveVariant(VariantBean variant);

}
