package com.nilo.communityapplication.service;

import com.nilo.communityapplication.DTO.UserRequestDTO;
import com.nilo.communityapplication.globalExceptionHandling.NotAuthorizedException;
import com.nilo.communityapplication.globalExceptionHandling.NotFoundException;
import com.nilo.communityapplication.model.Community;
import com.nilo.communityapplication.model.User;
import com.nilo.communityapplication.repository.CommunityRepository;
import com.nilo.communityapplication.repository.UserRepository;
import com.nilo.communityapplication.utils.BasicAuthorizationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    public User updateUser(UserRequestDTO userRequest) {
        User user = authUtil.getCurrentUser();

        User existingUserWithUsername = userRepository.findByUsername(userRequest.getUsername());
        Optional<User> existingUserWithEmail = userRepository.findByEmail(userRequest.getEmail());

        if (existingUserWithUsername != null) {
            throw new RuntimeException("User with the username already exists.");
        }

        if (existingUserWithEmail.isPresent()) {
            throw new RuntimeException("User with the email already exists.");
        }

        user.setFirstName(userRequest.getUsername());
        user.setLastName(userRequest.getLastName());
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setAvatar(userRequest.getAvatar());

        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
            user.setPassword(hashedPassword);
        }

        User updatedUser = userRepository.save(user);

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
