package com.project.movies.user;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserModel user) {
        return userService.createUser(user);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        return userService.deleteUserById(id);
    }
}
