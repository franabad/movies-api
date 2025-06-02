package com.project.movies.user;


import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.status(OK).body(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getUserById(@PathVariable String id) {
        UserModel user = userRepository.findById(id).orElse(null);

        if (user == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return ResponseEntity.status(NOT_FOUND).body(response);
        }

        return ResponseEntity.status(OK).body(user);
    }

    @Transactional
    public ResponseEntity<Object> createUser(@RequestBody UserModel user) {
        Optional<UserModel> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User already exists");
            return ResponseEntity.status(BAD_REQUEST).body(response);
        }

        // TO DO: Check this
        if(user.getRole() == null) {
            user.setRole("user");
        }

        UserModel newUser = userRepository.save(user);

        return ResponseEntity.status(CREATED).body(newUser);
    }

    @Transactional
    public ResponseEntity<Object> deleteUserById(@PathVariable String id) {
        Optional<UserModel> user = userRepository.findById(id);

        if (user.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return ResponseEntity.status(NOT_FOUND).body(response);
        }

        userRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        response.put("deletedUser", user);

        return ResponseEntity.status(NO_CONTENT).body(response);
    }
}
