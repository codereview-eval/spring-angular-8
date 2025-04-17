package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.User;

import java.util.Collection;

public interface UserService {

    User findUserByUsername(String username);
    Collection<User> findAllUsers();
    void saveUser(User user);
    void deleteUser(User user);
}
