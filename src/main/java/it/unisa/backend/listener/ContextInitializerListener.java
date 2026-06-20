package it.unisa.backend.listener;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import it.unisa.backend.model.bean.CategoryBean;
import it.unisa.backend.model.dao.impl.ProductDAO;
import it.unisa.backend.model.db.DBManager;

@WebListener
public class ContextInitializerListener implements ServletContextListener {

	
	
	@Override
    public void contextDestroyed(ServletContextEvent sce)  { 
            sce.getServletContext().removeAttribute("globalCategories");
            System.out.println("[SHUTDOWN] Removed categories from context");
    }
    
    @Override
    public void contextInitialized(ServletContextEvent sce)  { 

    	List<CategoryBean> globalCategories = new ArrayList<>();
    	
    	try {
    		
    		ProductDAO productDao = new ProductDAO(DBManager.getDataSource());            
            globalCategories = productDao.findAllCategories();
            
            sce.getServletContext().setAttribute("globalCategories", globalCategories);
            
            System.out.println("[BOOT SUCCESS] Product categories loaded in ServletContext");
    		
    	} catch(Exception e) {
    		
    		System.err.println("[BOOT ERROR] Error loading products categories from db");
            e.printStackTrace();
            
            CategoryBean backupCategory = new CategoryBean();
            backupCategory.setId(1L);
            backupCategory.setName("Generica");
            globalCategories.add(backupCategory);
            
            sce.getServletContext().setAttribute("globalCategories", globalCategories);
    	}
            
    }
	
}
