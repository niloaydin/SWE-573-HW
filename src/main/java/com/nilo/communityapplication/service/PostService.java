package com.nilo.communityapplication.service;

import com.nilo.communityapplication.DTO.PostFieldDTO;
import com.nilo.communityapplication.DTO.PostInCommunityDTO;
import com.nilo.communityapplication.globalExceptionHandling.NotAuthorizedException;
import com.nilo.communityapplication.globalExceptionHandling.NotFoundException;
import com.nilo.communityapplication.model.*;
import com.nilo.communityapplication.repository.*;
import com.nilo.communityapplication.utils.BasicAuthorizationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final UserService userService;
    public final PostTemplateRepository postTemplateRepository;
    private final PostFieldValueRepository postFieldValueRepository;
    private final BasicAuthorizationUtil authUtil;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);


    public List<Post> getPostsByCommunity(Long communityId) {

        return postRepository.findByCommunityIdWithFields(communityId);
    }

    @Transactional
    public Post createPost(Long communityId, Long templateId, Map<String, String> requestData) throws Exception {
        try {
            User user = authUtil.getCurrentUser();

            // Fetch community by ID
            Community community = communityRepository.findById(communityId)
                    .orElseThrow(() ->new NotFoundException("Community not found with ID: " + communityId));
            PostTemplate template;
            if (templateId != null) {
                template = postTemplateRepository.findById(templateId)
                        .orElseThrow(() ->new NotFoundException("Post template not found with ID: " + templateId));
            } else {
                template = postTemplateRepository.findByName("Default Template");
            }

            validateRequestData(template, requestData);

            PostDataValueValidator validator = new PostDataValueValidator();
            if(!validator.validateFieldTypes(template,requestData)){
                throw  new RuntimeException("Field values does not match with field types!");
            }

            Post post = new Post();
            post.setCommunity(community);
            post.setTemplate(template);
            post.setUser(user);
            post.setCreatedAt(LocalDateTime.now());
            post = postRepository.save(post);
            return getPostFieldsandFillPostValues(requestData, post, template);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }


    private void validateRequestData(PostTemplate template, Map<String, String> requestData) throws Exception {
        Set<PostDataField> dataFields = template.getDatafields();
        for (PostDataField field : dataFields) {
            String fieldName = field.getName();
            if (field.isRequired() && !requestData.containsKey(fieldName)) {
                throw new Exception("Field '" + fieldName + "' is required");
            }
        }
    }


    @Transactional
    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public Post findPostById(Long id){
       Post singlePost = postRepository.findPostById(id);
        if (singlePost == null){
            new NotFoundException("Post not found with ID: " + id);
        }

        return singlePost;
    }

    @Transactional
    public Post editPost(Long communityId, Long id, Map<String, String> requestData) throws Exception {

        User currentUser = authUtil.getCurrentUser();
        Post post = postRepository.findPostById(id);

        if(post == null){
            throw new NotFoundException("Post not found with ID: " + id);
        }

        authUtil.isCurrentUserEqualsToActionUser(post.getUser());

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() ->new NotFoundException("Community not found with ID: " + communityId));



        PostTemplate template = post.getTemplate();

        validateRequestData(template, requestData);

        PostDataValueValidator validator = new PostDataValueValidator();

        if(!validator.validateFieldTypes(template,requestData)){
            throw new RuntimeException("Field values does not match with field types!");
        }

        Set<PostDataField> dataFields = template.getDatafields();
        for(PostDataField field : dataFields){
            String fieldName = field.getName();
            String fieldValue = requestData.get(fieldName);
            PostFieldValue existingFieldValue = post.getFieldValues().stream().filter(value -> value.getPostDataField().equals(field)).findFirst().orElse(null);

            if(!fieldValue.equals(existingFieldValue.getValue())) {

                if (existingFieldValue != null) {
                    existingFieldValue.setValue(fieldValue);
                    postFieldValueRepository.save(existingFieldValue);
                } else {
                    throw new RuntimeException("Inconsistent data: Missing value for field " + fieldName);
                }
            }
        }
        return post;

    }

    private Post getPostFieldsandFillPostValues(Map<String, String> requestData, Post post, PostTemplate template) {
        Set<PostDataField> dataField = template.getDatafields();

        Map<String, PostDataField> fieldMap = new HashMap<>();
        for (PostDataField field : dataField) {
            fieldMap.put(field.getName(), field);
        }

        for (Map.Entry<String, String> entry : requestData.entrySet()) {
            String fieldName = entry.getKey();
            String value = entry.getValue();
            PostDataField field = fieldMap.get(fieldName);
            if (field != null) {
                PostFieldValue postValue = new PostFieldValue();
                PostFieldValueCompositeKey key = new PostFieldValueCompositeKey();
                key.setPostId(post.getId());
                key.setDataFieldId(field.getId());
                postValue.setId(key);
                postValue.setPostDataField(field);
                postValue.setPost(post);
                postValue.setValue(value);
                postFieldValueRepository.save(postValue);
            }
        }
        return post;
    }


    public List<PostInCommunityDTO> getPostsInCommunity(Long communityId) {

        List<Post> posts = postRepository.findByCommunityId(communityId);


        List<PostInCommunityDTO> postDTOs = new ArrayList<>();


        for (Post post : posts) {

            PostInCommunityDTO postDTO = new PostInCommunityDTO();
            postDTO.setId(post.getId());
            postDTO.setCreatedAt(post.getCreatedAt());


            List<PostFieldDTO> fieldDTOs = new ArrayList<>();


            for (PostFieldValue fieldValue : post.getFieldValues()) {
                // Create a DTO for the current field value
                PostFieldDTO fieldDTO = new PostFieldDTO();
                fieldDTO.setFieldName(fieldValue.getPostDataField().getName());
                fieldDTO.setFieldValue(fieldValue.getValue());

                // Add the field DTO to the list
                fieldDTOs.add(fieldDTO);
            }


            postDTO.setFieldDTOs(fieldDTOs);


            postDTOs.add(postDTO);
        }

        return postDTOs;
    }
}


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
/*
    public Post createPost(Long communityId, Long templateId, Map<String, String> requestData) throws Exception {

        ObjectMapper mahmut = new ObjectMapper();
        JsonNode nilo = mahmut.readTree(requestData.get("fields"));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Fetch community by ID
        Community community = communityRepository.findById(communityId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        // Fetch template by ID
        PostTemplate template = postTemplateRepository.findById(templateId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        // Validate request data against template data fields
        validateRequestData(template, nilo);

        // Create a new post
        Post post = new Post();
        post.setCommunity(community);
        post.setTemplate(template);
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        logger.info("REQUEST DATA IN POST {}", requestData);
        post.setValue(requestData.get("fields"));

*/
/*        Set<PostDataField> dataFields = template.getDatafields();
        for (PostDataField dataField : dataFields) {
            String fieldName = dataField.getName();
            String value = requestData.get(fieldName);
            postDataFieldRepository.save(dataField);

        }*//*

        // Save the post
        return postRepository.save(post);
    }

    private void validateRequestData(PostTemplate template, JsonNode request) throws Exception {
        Set<PostDataField> dataFields = template.getDatafields();
        for (PostDataField field : dataFields) {
            String fieldName = field.getName();
            if (field.isRequired() && !request.has(fieldName)) {
                throw new Exception("Field '" + fieldName + "' is required");
            }
            // You may add more validation logic here as needed
        }
    }
*/

