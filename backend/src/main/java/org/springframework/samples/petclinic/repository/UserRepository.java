package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.User;

import java.util.Collection;

public interface UserRepository {

    User findByUsername(String username) throws DataAccessException;

    Collection<User> findAll() throws DataAccessException;

    void save(User user) throws DataAccessException;

    void delete(User user) throws DataAccessException;
}
