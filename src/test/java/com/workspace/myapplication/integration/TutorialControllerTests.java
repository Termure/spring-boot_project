package com.workspace.myapplication.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspace.myapplication.model.Tutorial;
import com.workspace.myapplication.repository.TutorialRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test") 
public class TutorialControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TutorialRepository tutorialRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        tutorialRepository.deleteAll();
    }

    @DisplayName("Junit Integration test for save tutorial")
    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception{

        // given - precondition or setup
        Tutorial tutorial = Tutorial.builder()
                .title("some titile")
                .description("some description")
                .published(true)
                .build();
    
        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/tutorials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(tutorial)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.title",
                        is(tutorial.getTitle())))
                .andExpect(jsonPath("$.description",
                        is(tutorial.getDescription())))
                .andExpect(jsonPath("$.published",
                        is(tutorial.isPublished())));
    }

    @DisplayName("Junit Integration test for GET all tutorials when title is not provided")
    @Test
    void ghivenListOfTutorials_whenFindAll_thenReturnTutorials() throws Exception{
        // given -> precondition or setup
        List<Tutorial> listOfTutorials = new ArrayList<>();
        listOfTutorials.add(Tutorial.builder()
                .title("first")
                .description("first")
                .build());
        listOfTutorials.add(Tutorial.builder()
                .title("second")
                .description("decond")    
                .build());  
        tutorialRepository.saveAll(listOfTutorials);
        
        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/tutorials"));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(listOfTutorials.size())));
    }

    @DisplayName("Junit Integration test for Get tutorial by id when tutorial is present")
    @Test
    void givenTutorialId_whenFindById_thenReturnTutorial() throws Exception{
        // given - precondition or setup
        Tutorial tutorial = Tutorial.builder()
                .title("title")
                .description("null")
                .build();
        tutorialRepository.save(tutorial);

        // when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/tutorials/{id}", tutorial.getId()));

        // then - verfy the result 
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title", is(tutorial.getTitle())))
                .andExpect(jsonPath("$.description", is(tutorial.getDescription())));
    }
}
