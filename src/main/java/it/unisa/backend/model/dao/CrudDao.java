package it.unisa.backend.model.dao;

import java.util.List;

public interface CrudDao<T, ID>{
    
    boolean save(T entity);
    
    T findById(ID id);
    
    List<T> findAll();
    
    boolean update(T entity);
    
    boolean delete(ID id);
}