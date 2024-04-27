package com.nilo.communityapplication.controller;

import com.nilo.communityapplication.model.Community;
import com.nilo.communityapplication.model.Post;
import com.nilo.communityapplication.model.User;
import com.nilo.communityapplication.repository.PostRepository;
import com.nilo.communityapplication.requests.CommunityRequest;
import com.nilo.communityapplication.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/community")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;
    private final PostRepository postRepository;

    @PostMapping
    public Community createNewCommunity(@RequestBody CommunityRequest communityRequest) throws Exception {
        return communityService.createCommunity(communityRequest);
    }

    @GetMapping
    public List<Community> getAllCommunities(){

        return communityService.getAllCommunities();

    }
    @GetMapping("/{communityId}")
    public ResponseEntity<Community> getCommunityById(@PathVariable Long communityId) {
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


    @GetMapping("/posts/{communityId}")
    public ResponseEntity<List<Post>> postsInCommunity(@PathVariable Long communityId) {
        List<Post> posts = communityService.getPostsInCommunity(communityId);
        return ResponseEntity.ok(posts);
    }
}
