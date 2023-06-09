 package com.workspace.myapplication.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @DisplayName("Junit test for Get all tutorials when no records are found")
    @Test 
    void givenEmptyListOfTutorials_whenFindAll_thenReturnEmptyList() throws Exception{
        // given - precondition or setup
        given(tutorialService.getTutorials(null)).willReturn(Collections.emptyList());

        // when - action or the behavior that we are going to test 
        ResultActions response = mockMvc.perform(get("/api/tutorials"))
                .andExpect(status().isNoContent());

        // then - verify the result or output
        response.andExpect(jsonPath("$").doesNotExist());       
    }

    @DisplayName("Junit test for Get all tutorials when no records are found")
    @Test 
    void givenError_whenFindAll_thenReturnError() throws Exception{
        // given - precondition or setup
        given(tutorialService.getTutorials(null)).willThrow(new RuntimeException("Internal Server Error"));

        // when - action or the behavior that we are going to test 
        ResultActions response = mockMvc.perform(get("/api/tutorials"))
                .andExpect(status().isInternalServerError());

        // then - verify the result or output
        response.andExpect(jsonPath("$").doesNotExist());       
    }

    @DisplayName("Junit test for Get tutorial by id when tutorial is present")
    @Test
    void givenTutorialId_whenFindById_thenReturnTutorial() throws Exception{
        // given - precondition or setup
        long tutorialId = 1L;
        Tutorial tutorial = Tutorial.builder()
                .title("title")
                .description("null")
                .build();
        given(tutorialService.getTutorialById(tutorialId)).willReturn(Optional.of(tutorial));

        // when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/tutorials/{id}", tutorialId));

        // then - verfy the result 
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title", is(tutorial.getTitle())))
                .andExpect(jsonPath("$.description", is(tutorial.getDescription())));
    }

    @DisplayName("Junit test for Get tutorials by id when record is not present")
    @Test
    void givenTutorialId_whenFindById_thenNotFoundIsReturned() throws Exception{
        // given - precondition or setup
        long tutorial_id = 1L;
        given(tutorialService.getTutorialById(tutorial_id)).willReturn(Optional.empty());
        
        // when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/tutorials/{id}", tutorial_id));

        // then - verify the result
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @DisplayName("JUnit test for update tutorial")
    @Test
    void givenTutorialId_whenUpdate_thenReturnTutorial() throws Exception {
        // given - precondition or setup
        long tutorialId = 1L;
        Tutorial updatedTutorial = Tutorial.builder()
                .title("updated title")
                .description("update description")
                .build();
        given(tutorialService.updateTutorial(anyLong(), any(Tutorial.class)))
                .willAnswer((invocation) -> invocation.getArgument(1));
    
        // when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/tutorials/{id}", tutorialId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTutorial)));
    
        // then - verify the result
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title", is(updatedTutorial.getTitle())))
                .andExpect(jsonPath("$.description", is(updatedTutorial.getDescription())));
    }

    @DisplayName("Junit test for update tutorial negative scenario")
    @Test 
    void givenTutorial_whenTutorialNotfound_thenReturn404() throws Exception{
        // give - precondition or setup
        Tutorial tutorial = Tutorial.builder()
                .title("null")
                .description("null")
                .build();
        given(tutorialService.updateTutorial(anyLong(), any(Tutorial.class)))
                .willReturn(null);

        // when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/tutorials/{id}", tutorial.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tutorial)));

        // then - verify the result
        response.andExpect(status().isNotFound());
    }

    @DisplayName("Junit test for delete tutorial by id")
    @Test 
    void givenTutorialId_whenDelete_thenReturn204() throws Exception{
        // given - precondition or setup
        long tutorialId = 2L;
        doNothing().when(tutorialService).deleteTutorialById(tutorialId);

        // when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/tutorials/{id}", tutorialId));
        
        // then - verify the result
        response.andExpect(status().isNoContent());
    }

    @DisplayName("Junit test for delete tutorial by id negative scenario")
    @Test 
    void givenTutorialId_whenDelete_Retuns500() throws Exception{
        // given - precondition or setup
        doThrow(new RuntimeException("500")).when(tutorialService).deleteTutorialById(anyLong());

        // when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/tutorials/{id}", anyLong()));

        // then - verify the response 
        response.andExpect(status().isInternalServerError());
    }

    @DisplayName("Junit test for delete all tutorial")
    @Test 
    void givenTutorials_whenDelete_thenReturn204() throws Exception{
        // given - precondition or setup 
        doNothing().when(tutorialService).deleteAllTutorials();
        
        // when - action or the behavior that we are going to test 
        ResultActions response = mockMvc.perform(delete("/api/tutorials"));

        // then - verify the result
        response.andExpect(status().isNoContent());
    }

    @DisplayName("Junit test for delete all tutorial negative scenario")
    @Test 
    void givenTutorial_whenDelete_thenReturn500() throws Exception{
        // given - precondition or setup 
       doThrow(new RuntimeException("500")).when(tutorialService).deleteAllTutorials();
        
        // when - action or the behavior that we are going to test 
       ResultActions response = mockMvc.perform(delete("/api/tutorials"));

       // then - verify the result
       response.andExpect(status().isInternalServerError());
    }

    @DisplayName("Junit test for find published tutorials")
    @Test
    void givenTutorialsList_whenFindPublished_thenReturnList() throws Exception{
        // given - precondition or setup 
        List<Tutorial> listOfTutorialstutoriallist = new ArrayList<>();
        listOfTutorialstutoriallist.add(Tutorial.builder()
                .title("null")
                .description("null")
                .published(true)
                .build());
        listOfTutorialstutoriallist.add(Tutorial.builder()
                .title("null")
                .description("null")
                .published(true)
                .build());
        given(tutorialService.getPublishedTutorials()).willReturn(listOfTutorialstutoriallist);
        
        // when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/tutorials/published"));
        MvcResult result = response.andReturn();
        String customResponse = result.getResponse().getContentAsString();
        List<Tutorial> listOfTutorials = objectMapper.readValue(customResponse, new TypeReference<List<Tutorial>>() {});
        
        // then - verify the result
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(2)));
        
        boolean onlyPublishedTutorials = true;
        for (Tutorial tutorial : listOfTutorials) {
                if (tutorial.isPublished() == false)
                        onlyPublishedTutorials = false;
        }

        assertThat(listOfTutorials.size()).isEqualTo(2);
        assertThat(onlyPublishedTutorials).isEqualTo(true);
    }

    @DisplayName("Junit test for Get published tutorials when no records are found")
    @Test
    void givenNoPublishedTutorils_whenGetPublished_theEmptyListIsReturned() throws Exception{
        // given - precondition or setup 
        given(tutorialService.getPublishedTutorials()).willReturn(Collections.emptyList());

        // when - action or the bechavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/tutorials/published"));

        // then - verify the result
        response.andExpect(jsonPath("$").doesNotExist());
    }

    @DisplayName("Junit test for Get published tutotials when exceptin is thrown")
    @Test
    void givenTutorilaList_whenGetPublshed_thenExceptionIsThrown() throws Exception{
        // given - precondition or setup
        doThrow(new RuntimeException("500")).when(tutorialService).getPublishedTutorials();

        // when - action or the behvior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/tutorials/published"));

        // then - verofy the response 
        response.andExpect(status().isInternalServerError());
    }
}