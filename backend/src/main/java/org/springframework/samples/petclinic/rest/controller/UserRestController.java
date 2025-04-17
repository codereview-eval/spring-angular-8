/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.UserMapper;
import org.springframework.samples.petclinic.model.Role;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.rest.api.UsersApi;
import org.springframework.samples.petclinic.rest.dto.UserDto;
import org.springframework.samples.petclinic.rest.dto.UserFieldsDto;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api")
public class UserRestController implements UsersApi {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PreAuthorize( "hasRole(@roles.ADMIN)" )
    @Override
    public ResponseEntity<List<UserDto>> listUsers() {
        Collection<User> owners = this.userService.findAllUsers();
        return new ResponseEntity<>(userMapper.toUserDtoCollection(owners), HttpStatus.OK);
    }

    @PreAuthorize( "hasRole(@roles.ADMIN)" )
    @Override
    public ResponseEntity<UserDto> getUser(String username) {
        User user = this.userService.findUserByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userMapper.toUserDto(user), HttpStatus.OK);
    }

    @PreAuthorize( "hasRole(@roles.ADMIN)" )
    @Override
    public ResponseEntity<UserDto> addUser(UserDto userDto) {
        HttpHeaders headers = new HttpHeaders();
        User user = userMapper.toUser(userDto);
        final var role = new Role();
        role.setUser(user);
        role.setName("ROLE_OWNER_ADMIN");
        user.getRoles().add(role);
        user.setEnabled(true);
        this.userService.saveUser(user);
        return new ResponseEntity<>(userMapper.toUserDto(user), headers, HttpStatus.CREATED);
    }

    @PreAuthorize( "hasRole(@roles.ADMIN)" )
    @Override
    public ResponseEntity<UserDto> updateUser(String username, UserFieldsDto userFieldsDto) {
        User user = this.userService.findUserByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (userFieldsDto.getPassword() != null && !userFieldsDto.getPassword().isEmpty()) {
            user.setPassword(userFieldsDto.getPassword());
        }
        user.setEnabled(userFieldsDto.getEnabled() == null || userFieldsDto.getEnabled());
        this.userService.saveUser(user);
        return new ResponseEntity<>(userMapper.toUserDto(user), HttpStatus.NO_CONTENT);
    }

    @PreAuthorize( "hasRole(@roles.ADMIN)" )
    @Override
    public ResponseEntity<UserDto> deleteUser(String username) {
        User user = this.userService.findUserByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.userService.deleteUser(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
