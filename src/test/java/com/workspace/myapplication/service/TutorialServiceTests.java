package com.workspace.myapplication.service;

import static org.mockito.ArgumentMatchers.any;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.workspace.myapplication.model.Tutorial;
import com.workspace.myapplication.repository.TutorialRepository;
import com.workspace.myapplication.service.implementation.TutorialServiceImpl;

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
        Assertions.assertThat(savedTutorial).isNotNull();
    }
}
