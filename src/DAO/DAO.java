package DAO;

import java.util.List;

public interface DAO<T> {
    T findById(int id);
    List<T> findAll();
    boolean insert(T obj);
    boolean update(T obj);
    boolean delete(T obj);
}
