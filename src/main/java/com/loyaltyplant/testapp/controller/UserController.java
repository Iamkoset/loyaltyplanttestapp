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

@SuppressWarnings("all")
@RestController
@RequestMapping("/users")
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    @PostMapping
    public ResponseEntity addUser(@RequestBody User user) {
        User createdUser = userService.addUser(user);
//        user.setId(ThreadLocalRandom.current().nextInt(10, 101));
        if (createdUser == null)
            return new ResponseEntity("Not today", HttpStatus.NOT_FOUND);


        return new ResponseEntity(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        // set lazy collections' value to null to prevent 500 error on ui
        // accounts must be fetched with accounts service
        for (User user : allUsers) {
            user.setAccountSet(null);
        }

        return new ResponseEntity(allUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable("id") Long id) {
        User user = userService.getById(id);

        if (user == null)
            return new ResponseEntity("No user found with id: " + id, HttpStatus.NOT_FOUND);

        return new ResponseEntity(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);

        if (updatedUser == null)
            return new ResponseEntity("No user found with id: " + user.getId(), HttpStatus.NOT_FOUND);

        return new ResponseEntity(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity("User with id " + id + " has been deleted", HttpStatus.OK);
    }
}
