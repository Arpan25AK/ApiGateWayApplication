package com.example.ApiGateWayApplication.ApiService;

import com.example.ApiGateWayApplication.ApiDTO.AuthResponse;
import com.example.ApiGateWayApplication.ApiEntity.User;
import com.example.ApiGateWayApplication.ApiRepo.UserRepo;
import com.example.ApiGateWayApplication.ApiSecurity.JwtUtill;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtill jwtUtill;
    private final RedisTemplate redisTemplate;

    public AuthService(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtUtill jwtUtill, RedisTemplate redisTemplate){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtill = jwtUtill;
        this.redisTemplate = redisTemplate;
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

    public String login(String username , String rawpassword){
        Optional<User> existingUser = userRepo.findByUsername(username);
        if(existingUser.isPresent()) {
            User user = existingUser.get();
            if (passwordEncoder.matches(rawpassword, user.getPassword())) {
                return jwtUtill.generateToken(username);
            } else {
                return "Error : Invalid Password";
            }
        }else{
            return "Error: User Not Found!";
            }
    }

    public AuthResponse generateNewAccessToken(String refreshToken){

        String username = (String)redisTemplate.opsForValue().get("refresh:" + refreshToken);

        if(username != null){
            String accessToken = jwtUtill.generateToken(username);
            return new AuthResponse(refreshToken, accessToken);
        }else{
            throw new RuntimeException("Error : Invalid username");
        }
    }

}
