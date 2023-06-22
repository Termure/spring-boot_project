 package com.workspace.myapplication.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspace.myapplication.model.Tutorial;
import com.workspace.myapplication.service.TutorialService;

@WebMvcTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TutorialControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TutorialService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception{

        // given - precondition or setup
        Tutorial tutorial = Tutorial.builder()
                .title("some titile")
                .description("some description")
                .published(true)
                .build();
        given(employeeService.createTutorial(any(Tutorial.class)))
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

}