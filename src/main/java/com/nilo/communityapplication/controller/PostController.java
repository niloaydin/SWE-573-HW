package com.nilo.communityapplication.controller;

import com.nilo.communityapplication.model.Post;
import com.nilo.communityapplication.requests.PostCreationRequest;
import com.nilo.communityapplication.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "swagger_authentication")
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/create/{communityId}")
    public ResponseEntity<?> createPost(
            @PathVariable Long communityId,
            @RequestParam(required = false) Long templateId,
            @RequestBody Map<String, String> requestData
    ) throws Exception {

            Post post = postService.createPost(communityId, templateId, requestData);
            return ResponseEntity.ok(post);

    }

/*    @GetMapping("/{communityId}")
    public ResponseEntity<List<Post>> getPostsByCommunity(@PathVariable Long communityId) {
        List<Post> posts = postService.getPostsByCommunity(communityId);
        return ResponseEntity.ok(posts);
    }*/

    @GetMapping()
    public ResponseEntity<List<Post>> getAll() {
        List<Post> posts = postService.findAll();
        return ResponseEntity.ok(posts);
    }
}

