package org.springframework.samples.petclinic.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.samples.petclinic.model.Role;
import org.springframework.samples.petclinic.model.User;

import java.util.Collection;
import java.util.HashSet;

public interface UserService {

    User findUserByUsername(String username);

    @Cacheable("users")
    Collection<User> findAllUsers();

    void addUserRoles(User user, HashSet<Role> roles);

    void saveUser(User user);

    void deleteUser(User user);

    void blockUserIfExists(String username);

    boolean isAnyAdminEnabled();

}
