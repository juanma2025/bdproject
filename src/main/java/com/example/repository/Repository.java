package com.example.repository;

import java.sql.SQLException;
import java.util.List;

import com.mysql.cj.protocol.a.SqlDateValueEncoder;

public interface Repository<T>  {
    
    List<T> findAll() throws SQLException;
    T getByID(Integer id) throws SQLException;
    void save(T t);
    void delete(Integer id);
}
