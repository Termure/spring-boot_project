 package com.workspace.myapplication.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspace.myapplication.model.Tutorial;
import com.workspace.myapplication.service.TutorialService;

@WebMvcTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TutorialControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TutorialService tutorialService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Junit test for POST tutorial")
    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception{

        // given - precondition or setup
        Tutorial tutorial = Tutorial.builder()
                .title("some titile")
                .description("some description")
                .published(true)
                .build();
        given(tutorialService.createTutorial(any(Tutorial.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

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

    @DisplayName("Junit test for GET all tutorials when title is not provided")
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
        given(tutorialService.getTutorials(null))
                .willReturn(listOfTutorials);
        
        // when - action or behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/tutorials"));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(listOfTutorials.size())));
    }

    @DisplayName("Junit test for Get all tutorials when title is provided")
    @Test
    void givenListOfTutorials_whenFindAllAndTitleIsPriveded_thenReturnList() throws Exception{
        // given - precondition or setup 
        Tutorial tutorial = Tutorial.builder()
                .title("Ana")
                .description("Ana gots appeles")
                .build();
        given(tutorialService.getTutorials("Ana")).willReturn(List.of(tutorial));

        // when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/tutorials?title=Ana"))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = response.andReturn();
        String resultContent = result.getResponse().getContentAsString();
        List<Tutorial> tutorialList = objectMapper.readValue(resultContent, new TypeReference<List<Tutorial>>() {});
        
        // then - verify the result or putput
        assertThat(tutorialList).hasSize(1);
        assertThat(tutorialList.get(0).getTitle()).isEqualTo("Ana");
    }

}