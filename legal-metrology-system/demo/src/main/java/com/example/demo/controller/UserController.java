package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.model.UserResponse;
import com.example.demo.repository.UserRepository;

@RestController
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    // Register a new user
    @PostMapping("/users")
    public User registerUser(@RequestBody User user) {
        return repository.save(user);
    }

    // Get all users without passwords
    @GetMapping("/users")
    public List<UserResponse> getUsers() {

        return repository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                ))
                .collect(Collectors.toList());
    }

    // Login
    @PostMapping("/login")
    public String login(@RequestBody User user) {

        User existingUser =
                repository.findByEmail(user.getEmail());

        if (existingUser == null) {
            return "Invalid email or password";
        }

        if (!existingUser.getPassword()
                .equals(user.getPassword())) {

            return "Invalid email or password";
        }

        if (!existingUser.getRole()
                .equalsIgnoreCase(user.getRole())) {

            return "Invalid role";
        }

        return "Login successful";
    }
}