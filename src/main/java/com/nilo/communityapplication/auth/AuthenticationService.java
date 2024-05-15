package com.nilo.communityapplication.auth;

import com.nilo.communityapplication.auth.config.JwtService;
import com.nilo.communityapplication.model.Role;
import com.nilo.communityapplication.model.TokenBlockList;
import com.nilo.communityapplication.model.User;
import com.nilo.communityapplication.repository.TokenBlockListRepository;
import com.nilo.communityapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlockListRepository tokenBlockListRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public AuthenticationResponse register (RegisterRequest request) {
        try {
            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            Optional<User> existingUserWithEmail = userRepository.findByEmail(request.getEmail());
            User existingUserWithUsername = userRepository.findByUsername(request.getUsername());
            if(existingUserWithEmail.isPresent() || existingUserWithUsername != null){
                throw new RuntimeException("User already exists.");
            }
            userRepository.save(user);
            String jwtToken = jwtService.generateToken(user);
            logger.info("JWT Token: " + jwtToken);
            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (Exception e) {
            // Log the exception
            String errorMessage = "Failed to register user: " + e.getMessage();
            logger.error("Error occurred while registering user: {}", e.getMessage(), e);
            throw new RuntimeException(errorMessage);
        }
    }

    public AuthenticationResponse authenticate (AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            String jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).build();
        }catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        }
    }


    public void addToBlacklist(String token) {
        if (!tokenBlockListRepository.existsByToken(token)) {
            TokenBlockList blocklistToken = new TokenBlockList();
            blocklistToken.setToken(token);
            tokenBlockListRepository.save(blocklistToken);
        }
    }

}
