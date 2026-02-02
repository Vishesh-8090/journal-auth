package com.vishesh.journal.auth.service;

import com.vishesh.journal.auth.dto.RegisterRequest;
import com.vishesh.journal.auth.dto.UserResponse;
import com.vishesh.journal.auth.entity.User;
import com.vishesh.journal.auth.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void validateUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            throw new RuntimeException("Username already exists!");
        }
    }
    public void validateEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            throw new RuntimeException("User Email is not unique!");
        }
    }

    public UserResponse registerUser(RegisterRequest request){
        validateUsername(request.getUsername());
        validateEmail(request.getEmail());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    public UserResponse loginUser(String email, String password){
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Incorrect Email!"));

        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (!matches){
            throw new RuntimeException("Incorrect password!");
        }
        return mapToResponse(user);
    }

    public UserResponse mapToResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(user.getId().toHexString());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }
}
