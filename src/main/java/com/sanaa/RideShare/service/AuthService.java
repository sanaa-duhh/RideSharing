package com.sanaa.RideShare.service;

import com.sanaa.RideShare.dto.AuthLoginRequest;
import com.sanaa.RideShare.dto.AuthRegisterRequest;
import com.sanaa.RideShare.dto.AuthResponse;
import com.sanaa.RideShare.model.User;
import com.sanaa.RideShare.repository.UserRepository;
import com.sanaa.RideShare.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public void register(AuthRegisterRequest req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User(req.getUsername(), passwordEncoder.encode(req.getPassword()), req.getRole());
        userRepo.save(user);
    }

    public AuthResponse login(AuthLoginRequest req) {
        // authenticate (will throw if credentials invalid)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        // load user & generate token
        User user = userRepo.findByUsername(req.getUsername()).orElseThrow();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token);
    }
}
