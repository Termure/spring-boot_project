package com.workspace.myapplication.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.workspace.myapplication.exception.ResourceNotFoundException;
import com.workspace.myapplication.model.Tutorial;
import com.workspace.myapplication.repository.TutorialRepository;
import com.workspace.myapplication.service.implementation.TutorialServiceImpl;

import ch.qos.logback.classic.turbo.TurboFilter;

@ExtendWith(MockitoExtension.class)
public class TutorialServiceTests {

    @Mock
    private TutorialRepository tutorialRepository;

    @InjectMocks
    private TutorialServiceImpl tutorialService;

    private Tutorial tutorial;
    @BeforeEach
    public void setup(){
        tutorial = Tutorial.builder()
                .id(1L)
                .title("Some title")
                .description("some description")
                .published(false)
                .build();
    }

    @DisplayName("JUnit test for createTutorials")
    @Test 
    public void givenTutorialObject_whenCreateTutorial_thenReturnTutorialObject(){
        // given - precondition or setup
        given(tutorialRepository.findByTitleContaining(tutorial.getTitle()))
                .willReturn(Collections.emptyList());
        given(tutorialRepository.save(any(Tutorial.class))).willReturn(tutorial);

        // when - action or the behavior that we are going to test
        Tutorial savedTutorial = tutorialService.createTutorial(tutorial);

        // then - verify the output
        assertThat(savedTutorial).isNotNull();
    }

    @DisplayName("JUnit test for createTutorial which throw a custom exception")
    @Test 
    public void givenTutorialObject_whenCreateTutorial_thenThrowException(){
        // given - preconditions or setup
        List<Tutorial> tutorials = new ArrayList<Tutorial>();
        tutorials.add(tutorial);
        given(tutorialRepository.findByTitleContaining(tutorial.getTitle()))
                .willReturn(tutorials); 

        // when - action or the behavior that we are going to test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            tutorialService.createTutorial(tutorial);
        });

        // then - verify the output
        verify(tutorialRepository, never()).save(tutorial);
    }

    @DisplayName("JUnit test for get all tutorials when titile is not provided")
    @Test
    public void givenListOfTutorials_whenFindAll_thenReturnListOfTutorials(){
        // given - preconditions or setup
        Tutorial tutorial2 = Tutorial.builder()
                .title("Tutorial 1")
                .description("description")
                .build();
        given(tutorialRepository.findAll()).willReturn(List.of(tutorial, tutorial2));

        // when - action or the behavior that we are going to test
        List<Tutorial> tutorials = tutorialService.getTutorials(null);

        // then - verify the output
        assertThat(tutorials).isNotNull();
        assertThat(tutorials.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for get all tutorials when titile is provided")
    @Test
    public void givenListOfTutorials_whenFindAll_thenReturnTutorials(){
        // given - preconditions or setup
        given(tutorialRepository.findByTitleContaining(tutorial.getTitle()))
                .willReturn(List.of(tutorial));

        // when - action or the behavior that we are going to test
        List<Tutorial> tutorials = tutorialService.getTutorials(tutorial.getTitle());

        // then - verify the output
        assertThat(tutorials).isNotNull();
        assertThat(tutorials.size()).isEqualTo(1);
        assertThat(tutorials.get(0).getTitle()).isEqualTo(tutorial.getTitle());
    }

    @DisplayName("JUnit test for get all tutorials when titile is provided")
    @Test
    public void givenEmptyListOfTutorials_whenFindAll_thenReturnEmptyTutorials(){
        // given - preconditions or setup
        given(tutorialRepository.findByTitleContaining(tutorial.getTitle()))
                .willReturn(Collections.emptyList());

        // when - action or the behavior that we are going to test
        List<Tutorial> tutorials = tutorialService.getTutorials(tutorial.getTitle());

        // then - verify the output
        assertThat(tutorials).isEmpty();
        assertThat(tutorials.size()).isEqualTo(0);
    }

    @DisplayName("JUnit test for get tutorial by id")
    @Test
    public void givenTutorial_whenFindById_thenReturnTutorial(){
        // given - preconditions or setup
        given(tutorialRepository.findById(tutorial.getId()))
                .willReturn(Optional.of(tutorial));

        // when - action or the behavior that we are going to test
        Tutorial savedTutorial = tutorialService.getTutorialById(tutorial.getId()).get();

        // then - verify the output
        assertThat(savedTutorial).isNotNull();
    }

    @DisplayName("JUnit test for find published tutorial")
    @Test
    public void givenTutorials_whenFindPublshed_thenReturnTutorials(){
        // given - preconditions or setup
        given(tutorialRepository.findByPublished(true))
                .willReturn(List.of(tutorial));

        // when - action or the behavior that we are going to test
        List<Tutorial> publishedTutorial = tutorialService.getPublishedTutorials();

        // then - verify the output
        assertThat(publishedTutorial).isNotNull();
    }

    @DisplayName("JUnit test for update tutorial")
    @Test
    public void givenTutorial_whenUpdate_thenReturnTutorial(){
        // given - preconditions or setup
        tutorial.setTitle("New title");
        tutorial.setDescription("New description");
        tutorial.setPublished(true);
        given(tutorialRepository.findById(tutorial.getId())).willReturn(Optional.of(tutorial));
        given(tutorialRepository.save(any(Tutorial.class))).willReturn(tutorial);

        // when - action or the behavior that we are going to test
        Tutorial updatedTutorial = tutorialService.updateTutorial(tutorial.getId(), tutorial);

        // then - verify the output
        assertThat(updatedTutorial.getTitle()).isEqualTo("New title");
    }

    @DisplayName("JUnit test for update not existing tutorial")
    @Test
    public void givenTutorial_whenUpdate_thenReturnNewTutorial(){
        // given - preconditions or setup
        given(tutorialRepository.findById(tutorial.getId())).willReturn(Optional.empty());
        given(tutorialRepository.save(any(Tutorial.class))).willReturn(tutorial);

        // when - action or the behavior that we are going to test
        Tutorial updatedTutorial = tutorialService.updateTutorial(tutorial.getId(), tutorial);

        // then - verify the output
        assertThat(updatedTutorial.getTitle()).isEqualTo("Some title");
    }
}
