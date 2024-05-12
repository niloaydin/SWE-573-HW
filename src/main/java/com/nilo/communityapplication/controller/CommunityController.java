package com.nilo.communityapplication.controller;

import com.nilo.communityapplication.DTO.UserInCommunityDTO;
import com.nilo.communityapplication.model.Community;
import com.nilo.communityapplication.model.Post;
import com.nilo.communityapplication.model.User;
import com.nilo.communityapplication.repository.PostRepository;
import com.nilo.communityapplication.repository.UserJoinedCommunityRepository;
import com.nilo.communityapplication.requests.CommunityRequest;
import com.nilo.communityapplication.service.CommunityService;
import com.nilo.communityapplication.service.UserJoinedCommunityService;
import com.nilo.communityapplication.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "swagger_authentication")
@RequestMapping("/api/v1/community")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;
    private final UserJoinedCommunityService userJoinedCommunityService;

    @PostMapping
    @Operation(summary = "Creates new community", description = "Get the info from the request body and save to the database.")
    public ResponseEntity<Object> createNewCommunity(@Parameter(description = "Necessary information to create a community", required = true) @RequestBody CommunityRequest communityRequest) throws Exception {
        try {
            Community community = communityService.createCommunity(communityRequest);
            return ResponseEntity.ok(community);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Return a 400 Bad Request response with the error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Fetches all communities", description = "Fetches all communities from database.")
    public List<Community> getAllCommunities(){


        return communityService.getAllCommunities();

    }
    @GetMapping("/{communityId}")
    @Operation(summary = "Fetches one community with ID", description = "Fetches one community from database with community ID.")
    public ResponseEntity<Community> getCommunityById( @Parameter(description = "Community Id", required = true) @PathVariable Long communityId) {
        Optional<Community> community = communityService.getCommunityById(communityId);

        if(community.isPresent()) {
            return new ResponseEntity<>(community.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/joinCommunity/{communityId}")
    public ResponseEntity<String> joinCommunity(@PathVariable Long communityId) {
        communityService.joinCommunity(communityId);
        return ResponseEntity.ok("User joined the community successfully.");
    }

    @GetMapping("/neyzo")
    public List<Community> neyzo(@RequestParam("role") Long roleId ){
        return communityService.ilkerAbi(roleId);
    }


    @GetMapping("/{communityId}/posts")
    public ResponseEntity<List<Post>> postsInCommunity(@PathVariable Long communityId) {
        List<Post> posts = communityService.getPostsInCommunity(communityId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{communityId}/posts/{postId}")
    public ResponseEntity<Post> singlePostInCommunity(@PathVariable Long communityId, @PathVariable Long postId){
        Post singlePost = communityService.getSinglePostInCommunity(communityId, postId);
        return ResponseEntity.ok(singlePost);
    }

    @GetMapping("/{communityId}/users")
    public ResponseEntity<List<UserInCommunityDTO>> getUsersInCommunity(@PathVariable Long communityId) {
        // Query the database for users in the specified community
        List<UserInCommunityDTO> users = userJoinedCommunityService.findUsersWithRoleByCommunityId(communityId);

        // Return the users as a response
        return ResponseEntity.ok(users);
    }


}
