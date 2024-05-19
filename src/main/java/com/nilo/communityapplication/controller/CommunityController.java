package com.nilo.communityapplication.controller;

import com.nilo.communityapplication.DTO.CommunityDTO;
import com.nilo.communityapplication.DTO.PostInCommunityDTO;
import com.nilo.communityapplication.DTO.UserInCommunityDTO;
import com.nilo.communityapplication.auth.config.JwtService;
import com.nilo.communityapplication.globalExceptionHandling.NotFoundException;
import com.nilo.communityapplication.model.Community;
import com.nilo.communityapplication.model.Post;
import com.nilo.communityapplication.model.PostTemplate;
import com.nilo.communityapplication.model.User;
import com.nilo.communityapplication.repository.PostRepository;
import com.nilo.communityapplication.repository.UserJoinedCommunityRepository;
import com.nilo.communityapplication.requests.CommunityRequest;
import com.nilo.communityapplication.service.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@SecurityRequirement(name = "swagger_authentication")
@RequestMapping("/api/v1/community")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;
    private final UserJoinedCommunityService userJoinedCommunityService;
    private final PostTemplateService postTemplateService;
    private final PostService postService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommunityController.class);

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
    public List<CommunityDTO> getAllCommunities(){

        return communityService.getAllCommunitiesWithDetails();

    }
    @GetMapping("/{communityId}")
    @Operation(summary = "Fetches one community with ID", description = "Fetches one community from database with community ID.")
    public ResponseEntity<CommunityDTO> getCommunityById( @Parameter(description = "Community Id", required = true) @PathVariable Long communityId) {
        CommunityDTO singleCommunity = communityService.getSingleCommunity(communityId);
        return ResponseEntity.ok(singleCommunity);

    }

    @PutMapping("/{communityId}")
    public ResponseEntity<Object> updateCommunity(
          @PathVariable Long communityId,
          @RequestBody CommunityRequest communityRequest) {
        try {
            Community updatedCommunity = communityService.updateCommunity(communityId, communityRequest);
            return ResponseEntity.ok(updatedCommunity);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Return a 400 Bad Request response with the error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{communityId}/members")
    @Operation(summary = "Fetches one community with ID", description = "Fetches one community from database with community ID.")
    public ResponseEntity<List<UserInCommunityDTO>> getCommunityMembers( @Parameter(description = "Community Id", required = true) @PathVariable Long communityId) {
         List<UserInCommunityDTO> membersOfCommunity = communityService.getMembersOfCommunity(communityId);
        return ResponseEntity.ok(membersOfCommunity);

    }
    @PostMapping("/join/{communityId}")
    public ResponseEntity<String> joinCommunity(@PathVariable Long communityId) {
        communityService.joinCommunity(communityId);
        return ResponseEntity.ok("User joined the community successfully.");
    }

    @PostMapping("/leave/{communityId}")
    public ResponseEntity<String> leaveCommunity(@PathVariable Long communityId) {
        communityService.leaveCommunity(communityId);
        return ResponseEntity.ok("User leaved the community successfully.");
    }

    @GetMapping("/neyzo")
    public List<Community> neyzo(@RequestParam("role") Long roleId ){
        return communityService.ilkerAbi(roleId);
    }


/*    @GetMapping("/{communityId}/posts")
    public ResponseEntity<List<Post>> postsInCommunity(@PathVariable Long communityId) {
        List<Post> posts = communityService.getPostsInCommunity(communityId);
        return ResponseEntity.ok(posts);
    }*/

    @GetMapping("/{communityId}/posts")
    public ResponseEntity<List<PostInCommunityDTO>> postsInCommunity(@PathVariable Long communityId) {
        List<PostInCommunityDTO> postDTOs = postService.getPostsInCommunity(communityId);
        return ResponseEntity.ok(postDTOs);
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

    @GetMapping("/{communityId}/searchPosts")
    public ResponseEntity<List<PostInCommunityDTO>> searchPostsByTemplateFieldsInCommunity(
            @PathVariable Long communityId,
            @RequestParam(required = false) Long template,
            @RequestParam Map<String, String> searchCriteria) {
        try {

            List<PostInCommunityDTO> filteredPosts = postService.searchPostsByTemplateFieldsInCommunity(communityId, template, searchCriteria);

            return ResponseEntity.ok(filteredPosts);
        } catch (Exception e) {
           logger.info("HATA VAR {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{communityId}/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long communityId, @PathVariable Long postId) {
        try {
            postService.deletePost(communityId, postId);
            return ResponseEntity.ok("Post is deleted succesfully!");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{communityId}/templates")
    public ResponseEntity<List<PostTemplate>> getTemplatesForCommunity(@PathVariable Long communityId) {
        try {
            List<PostTemplate> templates = postTemplateService.getTemplatesForCommunity(communityId);
            return new ResponseEntity<>(templates, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
