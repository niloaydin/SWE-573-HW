package com.nilo.communityapplication.controller;

import com.nilo.communityapplication.model.entity.User;
import com.nilo.communityapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUserMethod(){

            return userService.getAllUsers();

    }
    @GetMapping("/user-communities")
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
    @PostMapping("/joinCommunity/{communityId}")
    public ResponseEntity<String> joinCommunity(@PathVariable Long communityId) {
        userService.joinCommunity(communityId);
        return ResponseEntity.ok("User joined the community successfully.");
    }
}
