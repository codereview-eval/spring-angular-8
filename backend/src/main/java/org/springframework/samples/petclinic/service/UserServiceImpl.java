package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Role;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public User findUserById(int id) {
        return findEntityById(() -> userRepository.findById(id));
    }

    @Override
    @Transactional
    public void saveUser(User user) {

        if(user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new IllegalArgumentException("User must have at least a role set!");
        }

        for (Role role : user.getRoles()) {
            if(!role.getName().startsWith("ROLE_")) {
                role.setName("ROLE_" + role.getName());
            }

            if(role.getUser() == null) {
                role.setUser(user);
            }
        }

        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    private <T> T findEntityById(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (ObjectRetrievalFailureException | EmptyResultDataAccessException e) {
            // Just ignore not found exceptions for Jdbc/Jpa realization
            return null;
        }
    }

}
