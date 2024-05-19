package com.nilo.communityapplication.service;

import com.nilo.communityapplication.controller.PostTemplateController;
import com.nilo.communityapplication.globalExceptionHandling.NotAuthorizedException;
import com.nilo.communityapplication.globalExceptionHandling.NotFoundException;
import com.nilo.communityapplication.model.*;
import com.nilo.communityapplication.repository.CommunityRepository;
import com.nilo.communityapplication.repository.PostDataFieldRepository;
import com.nilo.communityapplication.repository.PostTemplateRepository;
import com.nilo.communityapplication.requests.DataFieldRequest;
import com.nilo.communityapplication.utils.BasicAuthorizationUtil;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostTemplateService {

    private final PostDataFieldRepository postDataFieldRepository;
    private final PostTemplateRepository postTemplateRepository;
    private final BasicAuthorizationUtil basicAuthorizationUtil;
    private static final Logger logger = LoggerFactory.getLogger(PostTemplateService.class);
    private final CommunityRepository communityRepository;

    @PostConstruct
    public void initializeTemplates() {
        // Check if templates already exist in the database
        if (postTemplateRepository.count() == 0) {
            createDefaultTemplate();
        }
    }

    public void createDefaultTemplate(){
        PostTemplate defaultTemplate = new PostTemplate();
        defaultTemplate.setName("Default Template");
        postTemplateRepository.save(defaultTemplate);

        PostDataField firstField = new PostDataField();
        firstField.setName("Title");
        firstField.setType(mapFieldType(FieldType.valueOf("TEXT")));
        firstField.setRequired(true);
        firstField.setPostTemplate(defaultTemplate);
        postDataFieldRepository.save(firstField);

        PostDataField secondField = new PostDataField();
        secondField.setName("Description");
        secondField.setType(mapFieldType(FieldType.valueOf("TEXT")));
        secondField.setRequired(true);
        secondField.setPostTemplate(defaultTemplate);
        postDataFieldRepository.save(secondField);

    }

    @Transactional
    public PostTemplate createPostTemplate(String templateName, List<DataFieldRequest> dataFields, Long communityId) throws Exception {
        try {

            PostTemplate postTemplate = new PostTemplate();
            postTemplate.setName(templateName);

            Community communityToSaveTemplate = communityRepository.findById(communityId).orElseThrow(() -> new NotFoundException("Community not found with id: " + communityId));

            User currentUser = basicAuthorizationUtil.getCurrentUser();

            if(!currentUser.getId().equals(communityToSaveTemplate.getOwner().getId())){
                throw new NotAuthorizedException("You are not authorized to create a template!");
            }

            List<PostDataField> postDataFields = new ArrayList<>();
            if (dataFields != null) {
                for (DataFieldRequest fieldRequest : dataFields) {
                    PostDataField dataField = new PostDataField();
                    dataField.setName(fieldRequest.getName());
                    dataField.setType(mapFieldType(fieldRequest.getType()));
                    dataField.setRequired(fieldRequest.isRequired());
                    dataField.setPostTemplate(postTemplate);
                    postDataFieldRepository.save(dataField);
                    postDataFields.add(dataField);
                }
            }
            logger.info("post data fields  {}", postDataFields);
            postTemplate.setDatafields(postDataFields);
            postTemplate.setCommunity(communityToSaveTemplate);

            return postTemplateRepository.save(postTemplate);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    private String mapFieldType(FieldType fieldType) {
        switch (fieldType) {
            case TEXT:
                return "String";
            case NUMBER:
                return "Integer";
            case DATE:
                return "Date";
            case URL:
                return "URL";
            default:
                throw new IllegalArgumentException("Unsupported field type: " + fieldType);
        }

    }


    public void deletePostTemplateMethod(Long communityId, Long templateId) throws Exception {
        try {
            Community community = communityRepository.findById(communityId).orElseThrow(() -> new NotFoundException("Community does not exists"));
            PostTemplate template = postTemplateRepository.findById(templateId).orElseThrow(() -> new NotFoundException("Template does not exists"));

            User communityOwner = template.getCommunity().getOwner();

            User currentUser = basicAuthorizationUtil.getCurrentUser();

            if (!currentUser.getId().equals(communityOwner.getId())) {
                throw new NotAuthorizedException("You are not authorized to delete this template");
            }

            // Finally, delete the PostTemplate
            postTemplateRepository.deleteById(templateId);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }
    public List<PostTemplate> getTemplates(){
        return postTemplateRepository.findAll();
    }
    public List<PostTemplate> getTemplatesForCommunity(Long communityId){
        return postTemplateRepository.findByCommunityId(communityId);
    }
}
