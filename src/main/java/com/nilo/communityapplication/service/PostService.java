package com.nilo.communityapplication.service;

import com.nilo.communityapplication.model.*;
import com.nilo.communityapplication.repository.*;
import com.nilo.communityapplication.requests.PostCreationRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final PostDataFieldRepository postDataFieldRepository;

    public final PostTemplateRepository postTemplateRepository;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

/*    public Post createPost(PostCreationRequest request) {
        // Fetch user, community, and template from repositories
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Community community = communityRepository.findById(request.getCommunityId())
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));
        PostTemplate template = postTemplateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new EntityNotFoundException("Post template not found"));


        // Create a new post based on the request data and template
        Post post = new Post();
        post.setUser(user);
        post.setCommunity(community);
        post.setTemplate(template);
        post.setCreatedAt(LocalDateTime.now());

        Set<PostDataField> dataFields = new HashSet<>();
        for (Map.Entry<String, String> entry : request.getDataValues().entrySet()) {
            PostDataField dataField = new PostDataField();
            dataField.setName(entry.getKey());
            dataField.setValue(entry.getValue());
            dataField.setPostTemplate(template);
            dataFields.add(dataField);
        }
        post.setDataFields(dataFields);

        // Set any additional properties of the post, if needed

        // Save the post in the database
        return postRepository.save(post);
    }*/
    public Post createPost(Long communityId, Long templateId, Map<String, String> requestData) throws Exception {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Fetch community by ID
        Community community = communityRepository.findById(communityId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        // Fetch template by ID
        PostTemplate template = postTemplateRepository.findById(templateId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        // Validate request data against template data fields
        validateRequestData(template, requestData);

        // Create a new post
        Post post = new Post();
        post.setCommunity(community);
        post.setTemplate(template);
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        logger.info("REQUEST DATA IN POST {}", requestData);
        // You may set other fields of the post as needed

        Set<PostDataField> dataFields = template.getDatafields();
        for (PostDataField dataField : dataFields) {
            String fieldName = dataField.getName();
            String value = requestData.get(fieldName);
            dataField.setValue(value);
            postDataFieldRepository.save(dataField);

        }
        // Save the post
        return postRepository.save(post);
    }

    private void validateRequestData(PostTemplate template, Map<String, String> requestData) throws Exception {
        Set<PostDataField> dataFields = template.getDatafields();
        for (PostDataField field : dataFields) {
            String fieldName = field.getName();
            if (field.isRequired() && !requestData.containsKey(fieldName)) {
                throw new Exception("Field '" + fieldName + "' is required");
            }
            // You may add more validation logic here as needed
        }
    }

    public List<Post> getPostsByCommunity(Long communityId) {
        return postRepository.findByCommunityIdWithFields(communityId);
    }
}
