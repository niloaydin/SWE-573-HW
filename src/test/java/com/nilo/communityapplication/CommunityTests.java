package com.nilo.communityapplication;

import com.nilo.communityapplication.model.Community;
import static org.assertj.core.api.Assertions.assertThat;

import com.nilo.communityapplication.model.PostDataField;
import com.nilo.communityapplication.model.PostTemplate;
import com.nilo.communityapplication.repository.CommunityRepository;
import com.nilo.communityapplication.repository.PostDataFieldRepository;
import com.nilo.communityapplication.repository.PostTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CommunityTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private PostTemplateRepository postTemplateRepository;

    @Autowired
    private PostDataFieldRepository postDataFieldRepository;

    @Test
    public void saveCommunityTest(){
        Community newCommunity = new Community();
        newCommunity.setName("test community");
        newCommunity.setDescription("this community is created to see if test is working");
        newCommunity.setPublic(true);



        Community savedCommunity = communityRepository.save(newCommunity);

        assertThat(savedCommunity).isNotNull();
        assertThat(savedCommunity.getId()).isGreaterThan(0);

    }

    @Test
    public void createPostTemplateWithFieldsTest() {
        Community community = new Community();
        community.setName("Test Community");
        community.setDescription("This is a test community");
        community.setPublic(true);
        entityManager.persistAndFlush(community);

        PostDataField field1 = new PostDataField();
        field1.setName("Field 1");
        field1.setType("String");
        field1.setRequired(true);
        field1.setPostTemplate(null);
        entityManager.persistAndFlush(field1);

        PostDataField field2 = new PostDataField();
        field2.setName("Field 2");
        field2.setType("Integer");
        field2.setRequired(false);
        field2.setPostTemplate(null);
        entityManager.persistAndFlush(field2);

        List<PostDataField> dataFields = new ArrayList<>();
        dataFields.add(field1);
        dataFields.add(field2);

        // Create post template with associated data fields
        PostTemplate postTemplate = new PostTemplate();
        postTemplate.setName("Test Template");
        postTemplate.setCommunity(community);
        postTemplate.setDatafields(dataFields);
        entityManager.persistAndFlush(postTemplate);

        PostTemplate savedTemplate = postTemplateRepository.findById(postTemplate.getId()).orElse(null);

        Assertions.assertNotNull(savedTemplate);
        // Assert that the number of associated data fields matches the number of created data fields
        Assertions.assertEquals(2, savedTemplate.getDatafields().size());
    }
}
