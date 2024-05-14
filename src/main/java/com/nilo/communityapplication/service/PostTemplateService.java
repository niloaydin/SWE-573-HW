package com.nilo.communityapplication.service;

import com.nilo.communityapplication.controller.PostTemplateController;
import com.nilo.communityapplication.globalExceptionHandling.NotFoundException;
import com.nilo.communityapplication.model.Community;
import com.nilo.communityapplication.model.FieldType;
import com.nilo.communityapplication.model.PostDataField;
import com.nilo.communityapplication.model.PostTemplate;
import com.nilo.communityapplication.repository.CommunityRepository;
import com.nilo.communityapplication.repository.PostDataFieldRepository;
import com.nilo.communityapplication.repository.PostTemplateRepository;
import com.nilo.communityapplication.requests.DataFieldRequest;
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
            logger.info("datafield request {}", dataFields);
            Community communityToSaveTemplate = communityRepository.findById(communityId).orElseThrow(() -> new NotFoundException("Community not found with id: " + communityId));

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


    public List<PostTemplate> getTemplates(){
        return postTemplateRepository.findAll();
    }
}
