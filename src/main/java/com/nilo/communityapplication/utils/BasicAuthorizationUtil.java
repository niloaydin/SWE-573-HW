package com.nilo.communityapplication.utils;

import com.nilo.communityapplication.globalExceptionHandling.NotAuthorizedException;
import com.nilo.communityapplication.model.User;
import com.nilo.communityapplication.repository.UserRepository;
import com.nilo.communityapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BasicAuthorizationUtil {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }
    public boolean isCurrentUserEqualsToActionUser(User actionUser){
        User currentUser = getCurrentUser();
        if (!actionUser.equals(currentUser)){
            throw new NotAuthorizedException("You are not authorized to edit this post!");
        }
        return true;
    }
}
