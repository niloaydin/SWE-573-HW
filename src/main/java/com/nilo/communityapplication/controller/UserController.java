package com.nilo.communityapplication.controller;

import com.nilo.communityapplication.model.User;
import com.nilo.communityapplication.service.UserService;
import com.nilo.communityapplication.utils.BasicAuthorizationUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "swagger_authentication")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BasicAuthorizationUtil authUtil;

    @GetMapping
    public List<User> getAllUserMethod(){

            return userService.getAllUsers();

    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile() {
        // Retrieve logged-in user's profile
        User currentUser = authUtil.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }
    @GetMapping("/user_communities")
    public ResponseEntity<List<User>> getAllUsersWithCommunities() {
        List<User> users = userService.getAllUsersWithCommunities();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> userOptional = userService.getUserById(userId);

        if(userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
