package com.nilo.communityapplication.controller;

import com.nilo.communityapplication.globalExceptionHandling.NotFoundException;
import com.nilo.communityapplication.model.Community;
import com.nilo.communityapplication.model.PostTemplate;
import com.nilo.communityapplication.requests.DataFieldRequest;
import com.nilo.communityapplication.requests.PostTemplateRequest;
import com.nilo.communityapplication.service.PostTemplateService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@SecurityRequirement(name = "swagger_authentication")
@RequestMapping("/api/v1/template")
@RequiredArgsConstructor
public class PostTemplateController {

    private final PostTemplateService postTemplateService;
    private static final Logger logger = LoggerFactory.getLogger(PostTemplateController.class);

    @PostMapping
    public ResponseEntity<Object> createPostTemplate(
            @RequestParam Long communityId,
            @RequestBody PostTemplateRequest request) {
        try {

            String templateName = request.getTemplateName();
            List<DataFieldRequest> dataFields = request.getDataFields();
            logger.info("AAAAAAAAAAAAA");
            PostTemplate createdPostTemplate = postTemplateService.createPostTemplate(templateName, dataFields, communityId);
            return new ResponseEntity<>(createdPostTemplate, HttpStatus.CREATED);
        } catch (Exception e) {

            String errorMessage = "An error occurred in the service: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);

        }
    }

    @GetMapping
    public List<PostTemplate> getPostTemplates(){
        return postTemplateService.getTemplates();
    }

    @DeleteMapping("{communityId}/deleteTemplate/{templateId}")
    public ResponseEntity<?> deletePostTemplate(@PathVariable Long communityId, @PathVariable Long templateId) throws Exception {
            postTemplateService.deletePostTemplateMethod(communityId, templateId);
            return ResponseEntity.ok("Template is deleted succesfully!");
    }

/*    @PostMapping()
    public ResponseEntity<String> processData(@RequestBody Set<String> data) {
        // Process the data here, for example, you can print it
        System.out.println("Received data: " + data);

        // Return a success response
        return ResponseEntity.status(HttpStatus.OK).body("Data received successfully");
    }*/
}
