package com.basicarch.base.service;

import com.basicarch.base.model.BaseEntity;
import com.basicarch.base.model.BaseSearchParam;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseService<T extends BaseEntity<ID>, P extends BaseSearchParam<ID>, ID extends Serializable> {
    boolean existsById(ID id);
    Optional<T> findById(ID id);
    List<T> findAllBy(P p);
    T save(T entity);
    T update(T entity);
    void deleteById(ID id);
}
