package com.abank.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<E, ID> {
    E save(E entity);
    Optional<E> findById(ID id);
    List<E> findAll();
}
