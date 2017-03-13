package com.loyaltyplant.testapp.controller;

import com.loyaltyplant.testapp.domain.model.User;
import com.loyaltyplant.testapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/users")
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    /**
     * Returns {@link ResponseEntity} which contain data of created user
     *
     * Response status {@link HttpStatus#NOT_IMPLEMENTED} means that account
     * wasn't created
     *
     * @param user user structure
     * @return {@link ResponseEntity}
     */
    @PostMapping
    public ResponseEntity<? super User> addUser(@RequestBody User user) {
        User createdUser = userService.addUser(user);
        if (createdUser == null)
            return new ResponseEntity<>("Error occurred during user creation",
                    HttpStatus.NOT_IMPLEMENTED);


        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Returns {@link ResponseEntity} which contain data of all users
     *
     * @return {@link ResponseEntity}
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();

        // set lazy collections' value to null to prevent 500 error on ui
        // accounts must be fetched with accounts service
        for (User user : allUsers) {
            user.setAccountSet(null);
        }

        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    /**
     * Returns {@link ResponseEntity} which contain data of user
     *
     * Response status {@link HttpStatus#NOT_FOUND} means that account
     * wasn't faound in data store
     *
     * @param id user id
     * @return {@link ResponseEntity}
     */
    @GetMapping("/{id}")
    public ResponseEntity<? super User> getUser(@PathVariable("id") Long id) {
        User user = userService.getById(id);

        if (user == null)
            return new ResponseEntity<>("No user found with id: " + id, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Returns {@link ResponseEntity} which contain data of user
     *
     * Response status {@link HttpStatus#NOT_FOUND} means that account
     * wasn't faound in data store
     *
     * @param user user structure
     * @return {@link ResponseEntity}
     */
    @PutMapping("/{id}")
    public ResponseEntity<? super User> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);

        if (updatedUser == null)
            return new ResponseEntity<>("No user found with id: " + user.getId(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * @param id user id
     * @return {@link ResponseEntity}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User with id " + id + " has been deleted", HttpStatus.OK);
    }
}
