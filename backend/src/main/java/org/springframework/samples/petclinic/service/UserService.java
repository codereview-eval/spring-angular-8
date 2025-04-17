package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.User;

public interface UserService {

    User findUserById(int id);
    void saveUser(User user);
    void deleteUser(User user);
}
