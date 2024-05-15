package com.nilo.communityapplication.service;

import com.nilo.communityapplication.DTO.UpdatedUserDTO;
import com.nilo.communityapplication.DTO.UserRequestDTO;
import com.nilo.communityapplication.auth.AuthenticationRequest;
import com.nilo.communityapplication.auth.AuthenticationResponse;
import com.nilo.communityapplication.auth.AuthenticationService;
import com.nilo.communityapplication.auth.config.JwtService;
import com.nilo.communityapplication.globalExceptionHandling.NotAuthorizedException;
import com.nilo.communityapplication.globalExceptionHandling.NotFoundException;
import com.nilo.communityapplication.model.Community;
import com.nilo.communityapplication.model.User;
import com.nilo.communityapplication.repository.CommunityRepository;
import com.nilo.communityapplication.repository.UserRepository;
import com.nilo.communityapplication.utils.BasicAuthorizationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BasicAuthorizationUtil authUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<User> getAllUsers(){
        List<User> users = userRepository.findAll();
        System.out.println("Retrieved users: " + users.size()); // Logging
        return users;
    }

    public List<User> getAllUsersWithCommunities() {
        List<User> users = userRepository.findAllWithCommunities();
        System.out.println("Retrieved users: " + users.size());
        return users;
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public UpdatedUserDTO updateUser(UserRequestDTO userRequest) {
        User user = authUtil.getCurrentUser();
        boolean emailChanged = false;

        if(!user.getEmail().equals(userRequest)) {

            emailChanged = true;

            Optional<User> existingUserWithEmail = userRepository.findByEmail(userRequest.getEmail());


            if (existingUserWithEmail.isPresent()) {
                throw new RuntimeException("User with the email already exists.");
            }
        }
        if(!user.getUsername().equals(userRequest.getUsername())){
            User existingUserWithUsername = userRepository.findByUsername(userRequest.getUsername());
            if (existingUserWithUsername != null) {
                throw new RuntimeException("User with the username already exists.");
            }
        }

            user.setFirstName(userRequest.getUsername());
            user.setLastName(userRequest.getLastName());
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setAvatar(userRequest.getAvatar());

            UpdatedUserDTO updatedUser = new UpdatedUserDTO();
            updatedUser.setId(user.getId());
            updatedUser.setFirstName(userRequest.getUsername());
            updatedUser.setLastName(userRequest.getLastName());
            updatedUser.setUsername(userRequest.getUsername());
            updatedUser.setEmail(userRequest.getEmail());
            updatedUser.setAvatar(userRequest.getAvatar());

        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
            user.setPassword(hashedPassword);
        }

        userRepository.save(user);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(user.getEmail(), userRequest.getPassword());
        String jwt = null;

        if(emailChanged){
            AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
            jwt = authenticationResponse.getToken();
        }
        updatedUser.setJwt(jwt);

        logger.info("+++++++++++++++++++ NEW JWT {}", jwt);

        return updatedUser;
    }

    public void deleteUserMethod(Long userId) throws Exception {
        try{
        User currentUser = authUtil.getCurrentUser();
        User userToDelete = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User with the id does not exist."));

        if(!currentUser.getId().equals(userToDelete.getId())){
            throw new NotAuthorizedException("You are not authorized to delete this user!");
        }

        userRepository.deleteById(userId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

}
