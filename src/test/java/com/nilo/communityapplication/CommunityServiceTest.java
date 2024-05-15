package com.nilo.communityapplication;

import com.nilo.communityapplication.model.Community;
import com.nilo.communityapplication.model.User;
import com.nilo.communityapplication.model.UserCommunityRole;
import com.nilo.communityapplication.requests.CommunityRequest;
import com.nilo.communityapplication.repository.CommunityRepository;
import com.nilo.communityapplication.repository.UserCommunityRoleRepository;
import com.nilo.communityapplication.repository.UserJoinedCommunityRepository;
import com.nilo.communityapplication.service.CommunityService;
import com.nilo.communityapplication.utils.BasicAuthorizationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommunityServiceTest {

    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private UserCommunityRoleRepository userCommunityRoleRepository;

    @Mock
    private UserJoinedCommunityRepository userJoinedCommunityRepository;

    @Mock
    private BasicAuthorizationUtil authUtil;

    @InjectMocks
    private CommunityService communityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCommunity_Success() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // Mock SecurityContextHolder to return the authenticated user


        CommunityRequest communityRequest = new CommunityRequest();
        communityRequest.setName("Test Community");
        communityRequest.setDescription("Test Description");
        communityRequest.setPublic(true);

        UserCommunityRole ownerRole = new UserCommunityRole();
        ownerRole.setId(1L);
        ownerRole.setName("OWNER");

        when(userCommunityRoleRepository.findByName("OWNER")).thenReturn(java.util.Optional.of(ownerRole));
        when(authUtil.getCurrentUser()).thenReturn(user);
        when(communityRepository.save(any(Community.class))).thenAnswer(invocation -> {
            Community savedCommunity = invocation.getArgument(0);
            savedCommunity.setId(1L); // Mocking the saved community ID
            return savedCommunity;
        });

        // Act
        Community createdCommunity = communityService.createCommunity(communityRequest);

        // Assert
        verify(userCommunityRoleRepository, times(1)).findByName("OWNER");
        verify(authUtil, times(1)).getCurrentUser();
        verify(communityRepository, times(1)).save(any(Community.class));
        verify(userJoinedCommunityRepository, times(1)).save(any());

        // Additional assertions on the createdCommunity object if needed
        // assertEquals("Test Community", createdCommunity.getName());
        // assertEquals("Test Description", createdCommunity.getDescription());
        // assertTrue(createdCommunity.isPublic());
        // assertEquals(user, createdCommunity.getOwner());
    }
}
