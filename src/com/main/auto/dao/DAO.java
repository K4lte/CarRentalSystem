package com.main.auto.dao;

import java.util.List;

public interface DAO <T> {
//    Request to change an existing field of model
    boolean edit(T t);

//    Request to delete a field of model by identification number (id)
    boolean delete(int id);

//    Request to add a new field
//    returns identification number (id) that was autogenerated in database
    int add(T t);

//    Request for finding a field by identification number
//    returns model T
    T getById(int id);

//    Request for finding all positions
//    returns collection (list)of all model included in table
    List<T> getAll();
}
