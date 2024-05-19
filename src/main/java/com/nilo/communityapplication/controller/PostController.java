package com.nilo.communityapplication.controller;

import com.nilo.communityapplication.DTO.PostDTO;
import com.nilo.communityapplication.model.Post;
import com.nilo.communityapplication.requests.PostCreationRequest;
import com.nilo.communityapplication.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "swagger_authentication")
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/{communityId}/create")
    public ResponseEntity<?> createPost(
            @PathVariable Long communityId,
            @RequestParam(required = false) Long templateId,
            @RequestBody LinkedHashMap<String, String> requestData
    ) {
        try {

            Post post = postService.createPost(communityId, templateId, requestData);
            return ResponseEntity.ok(post);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

/*    @GetMapping("/{communityId}")
    public ResponseEntity<List<Post>> getPostsByCommunity(@PathVariable Long communityId) {
        List<Post> posts = postService.getPostsByCommunity(communityId);
        return ResponseEntity.ok(posts);
    }*/

    @GetMapping()
    public ResponseEntity<List<PostDTO>> getAll() {
        List<PostDTO> posts = postService.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getSinglePost(@PathVariable Long id) {
        PostDTO singlePost = postService.findPostById(id);
        return ResponseEntity.ok(singlePost);
    }

    @PutMapping("{communityId}/edit/{id}")
    public ResponseEntity<Post> editSinglePost(@PathVariable Long id, @PathVariable Long communityId, @RequestBody Map<String, String> requestData) throws Exception {

            Post editPost = postService.editPost(communityId, id, requestData);
            return ResponseEntity.ok(editPost);

    }
}

