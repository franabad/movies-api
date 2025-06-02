package com.project.movies.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @GetMapping
    public List<UserModel> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Object getUserById(@PathVariable String id) {
        Optional<UserModel> user = userRepository.findById(id);

        if (user.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        return ResponseEntity.status(200).body(user);

    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserModel user) {
        Optional<UserModel> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User already exists");
            return ResponseEntity.status(400).body(response);
        }

        if(user.getRole() == null) {
            user.setRole("user");
        }

        UserModel newUser = userRepository.save(user);

        return ResponseEntity.status(201).body(newUser);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        Optional<UserModel> user = userRepository.findById(id);

        if (user.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        userRepository.deleteById(id);

//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "User deleted successfully");
//        response.put("deletedUser", user);

        // Code 204 para No content. El body no lo manda, por lo tanto null
        return ResponseEntity.status(204).body(null);
    }

}
