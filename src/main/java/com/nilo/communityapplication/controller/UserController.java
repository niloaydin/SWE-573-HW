package com.nilo.communityapplication.controller;

import com.nilo.communityapplication.model.User;
import com.nilo.communityapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
