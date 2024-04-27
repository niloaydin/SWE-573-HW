package com.nilo.communityapplication.service;

import com.nilo.communityapplication.controller.PostTemplateController;
import com.nilo.communityapplication.model.FieldType;
import com.nilo.communityapplication.model.PostDataField;
import com.nilo.communityapplication.model.PostTemplate;
import com.nilo.communityapplication.repository.PostDataFieldRepository;
import com.nilo.communityapplication.repository.PostTemplateRepository;
import com.nilo.communityapplication.requests.DataFieldRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostTemplateService {

    private final PostDataFieldRepository postDataFieldRepository;
    private final PostTemplateRepository postTemplateRepository;
    private static final Logger logger = LoggerFactory.getLogger(PostTemplateService.class);


    @Transactional
    public PostTemplate createPostTemplate(String templateName, Set<DataFieldRequest> dataFields) {
        PostTemplate postTemplate = new PostTemplate();
        postTemplate.setName(templateName);
        logger.info("datafield request {}", dataFields);

        Set<PostDataField> postDataFields = new HashSet<>();
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

        return postTemplateRepository.save(postTemplate);
    }

    private String mapFieldType(FieldType fieldType) {
        switch (fieldType) {
            case TEXT:
                return "String";
            case NUMBER:
                return "Integer";
            // Add mappings for other types if needed
            default:
                throw new IllegalArgumentException("Unsupported field type: " + fieldType);
        }
    }

    public List<PostTemplate> getTemplates(){
        return postTemplateRepository.findAll();
    }
}
