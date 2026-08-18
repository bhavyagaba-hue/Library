package com.Project.Library.controller;

import com.Project.Library.dto.AuthResponse;
import com.Project.Library.dto.LoginRequest;
import com.Project.Library.dto.RegisterRequest;
import com.Project.Library.dto.UserResponse;
import com.Project.Library.entity.User;
import com.Project.Library.security.CustomUserDetailsService;
import com.Project.Library.security.JwtService;
import com.Project.Library.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthController(UserService userService,AuthenticationManager authenticationManager,CustomUserDetailsService userDetailsService,JwtService jwtService ){
        this.userService = userService;
        this.authenticationManager=authenticationManager;
        this.userDetailsService=userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request){
        User savedUser = userService.registerUser(request);

        return new UserResponse(
                savedUser.getUserId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getJoinedOn()
        );
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername( request.getEmail());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }
}