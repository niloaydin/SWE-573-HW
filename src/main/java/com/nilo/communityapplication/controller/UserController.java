package com.nilo.communityapplication.controller;

import com.nilo.communityapplication.DTO.UpdatedUserDTO;
import com.nilo.communityapplication.DTO.UserDTO;
import com.nilo.communityapplication.DTO.UserRequestDTO;
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
    public ResponseEntity<?> getUserProfile() {

        try {
            UserDTO currentUser = userService.getProfile();

            return ResponseEntity.ok(currentUser);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PutMapping("/profile/edit")
    public ResponseEntity<?> updateUser(
            @RequestBody UserRequestDTO userRequest) {
        try {
            UpdatedUserDTO updatedUser = userService.updateUser(userRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) throws Exception {
        userService.deleteUserMethod(userId);
        return ResponseEntity.ok("User is deleted succesfully!");
    }
}
