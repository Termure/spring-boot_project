package com.workspace.myapplication.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.workspace.myapplication.model.Tutorial;

@SpringBootTest // TO DO: use @DataJpaTest instead 
public class TutorialRepositoryTests {
    
    @Autowired TutorialRepository tutorialRepository;

    @DisplayName("JUnit test for save tutorial operation")
    @Test
    public void givenTutorialObject_wheSave_thenReturnSavedTutorial() {

        // given - precondition or setup 
        Tutorial tutorial = Tutorial.builder()
                 .title("Spring boot 3")
                 .description("First version of spring boot 3")
                 .published(false)
                 .build();

        // when - action or the behavior than we are going to test
        Tutorial savedTutorial = tutorialRepository.save(tutorial);

        // then - verify the output
        assertThat(savedTutorial).isNotNull();
        assertThat(savedTutorial.getId()).isGreaterThan(0);
    }
    
}
