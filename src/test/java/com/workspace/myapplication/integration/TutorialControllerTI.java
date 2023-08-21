package com.workspace.myapplication.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspace.myapplication.model.Tutorial;
import com.workspace.myapplication.repository.TutorialRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TutorialControllerTI extends AbstractionBaseTest{

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private TutorialRepository tutorialRepository;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        void setup() {
                tutorialRepository.deleteAll();
        }

        @DisplayName("Junit Integration test for save tutorial")
        @Test
        void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

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
                response.andDo(print()).andExpect(status().isCreated())
                                .andExpect(jsonPath("$.title",
                                                is(tutorial.getTitle())))
                                .andExpect(jsonPath("$.description",
                                                is(tutorial.getDescription())))
                                .andExpect(jsonPath("$.published",
                                                is(tutorial.isPublished())));
        }

        @DisplayName("Junit Integration test for GET all tutorials when title is not provided")
        @Test
        void ghivenListOfTutorials_whenFindAll_thenReturnTutorials() throws Exception {
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
        void givenTutorialId_whenFindById_thenReturnTutorial() throws Exception {
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

        @DisplayName("JUnit Integration test for update tutorial")
        @Test
        void givenTutorialId_whenUpdate_thenReturnTutorial() throws Exception {
                // given - precondition or setup
                Tutorial updatedTutorial = Tutorial.builder()
                                .title("updated title")
                                .description("update description")
                                .build();

                // when - action or the behavior that we are going to test
                ResultActions response = mockMvc.perform(put("/api/tutorials/{id}", updatedTutorial.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedTutorial)));

                // then - verify the result
                response.andExpect(status().isOk())
                                .andDo(print())
                                .andExpect(jsonPath("$.title", is(updatedTutorial.getTitle())))
                                .andExpect(jsonPath("$.description", is(updatedTutorial.getDescription())));
        }

        @DisplayName("Junit Integration test for delete all tutorial")
        @Test
        void givenTutorials_whenDelete_thenReturn204() throws Exception {
                // when - action or the behavior that we are going to test
                ResultActions response = mockMvc.perform(delete("/api/tutorials"));

                // then - verify the result
                response.andExpect(status().isNoContent());
        }

        @DisplayName("Junit Integration test for delete tutorial by id")
        @Test
        void givenTutorialId_whenDelete_thenReturn204() throws Exception {
                // given - precondition or setup
                Tutorial tutorial = Tutorial.builder()
                                .title("sfs")
                                .description("null")
                                .build();
                tutorialRepository.save(tutorial);

                // when - action or the behavior that we are going to test
                ResultActions response = mockMvc.perform(delete("/api/tutorials/{id}", tutorial.getId()));

                // then - verify the result
                response.andExpect(status().isNoContent());
        }

        @DisplayName("Junit Integration test for find published tutorials")
        @Test
        void givenTutorialsList_whenFindPublished_thenReturnList() throws Exception {
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
                tutorialRepository.saveAll(listOfTutorialstutoriallist);

                // when - action or the behavior that we are going to test
                ResultActions response = mockMvc.perform(get("/api/tutorials/published"));
                MvcResult result = response.andReturn();
                String customResponse = result.getResponse().getContentAsString();
                List<Tutorial> listOfTutorials = objectMapper.readValue(customResponse,
                                new TypeReference<List<Tutorial>>() {
                                });

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

}
