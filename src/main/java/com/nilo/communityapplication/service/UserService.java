package com.nilo.communityapplication.service;

import com.nilo.communityapplication.model.User;
import com.nilo.communityapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
}
