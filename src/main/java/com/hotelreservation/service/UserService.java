package com.hotelreservation.service;

import com.hotelreservation.entity.User;
import com.hotelreservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * User Service - Implements Spring Core IOC and Dependency Injection (BO3 - Spring Core)
 * Provides business logic for user operations
 */
@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    // Constructor injection (BO3 - Spring Core Dependency Injection)
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Register a new user
     * @param user the user to register
     * @return the saved user
     * @throws IllegalArgumentException if username or email already exists
     */
    public User registerUser(User user) {
        // Validate username uniqueness
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        
        // Validate email uniqueness
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        
        return userRepository.save(user);
    }
    
    /**
     * Authenticate user login
     * @param usernameOrEmail the username or email
     * @param password the password
     * @return the user if authentication successful
     * @throws IllegalArgumentException if credentials are invalid
     */
    public User authenticateUser(String usernameOrEmail, String password) {
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + usernameOrEmail);
        }
        
        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        
        return user;
    }
    
    /**
     * Find user by ID
     * @param id the user ID
     * @return the user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Find user by username
     * @param username the username
     * @return the user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Find user by email
     * @param email the email
     * @return the user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Get all users
     * @return list of all users
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Update user information
     * @param user the user to update
     * @return the updated user
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * Delete user by ID
     * @param id the user ID to delete
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    /**
     * Check if username exists
     * @param username the username to check
     * @return true if username exists
     */
    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
    
    /**
     * Check if email exists
     * @param email the email to check
     * @return true if email exists
     */
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}

