package com.example.ApiGateWayApplication.ApiService;

import com.example.ApiGateWayApplication.ApiEntity.User;
import com.example.ApiGateWayApplication.ApiRepo.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepo userRepo, PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(String username, String rawPassword){

        Optional<User> existingUser = userRepo.findByUsername(username);

        if(existingUser.isPresent()){
            return "User already present";
        }

        User newUser= new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        userRepo.saveUser(newUser);

        return "User registered successfully!";
    }
}
