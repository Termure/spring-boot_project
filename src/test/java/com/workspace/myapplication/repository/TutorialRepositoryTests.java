package com.workspace.myapplication.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import com.workspace.myapplication.model.Tutorial;

@DataJpaTest
@TestPropertySource(locations="classpath:application-test.properties")
public class TutorialRepositoryTests {
    
    @Autowired TutorialRepository tutorialRepository;

    @DisplayName("JUnit test for save tutorial operation")
    @Test
    public void givenTutorialObject_wheSave_thenReturnSavedTutorial(){

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

    @DisplayName("JUnit test for get all tutorial operation")
    @Test
    public void givenTutorial_whenFindAll_thenTutorialList(){
        // given - precondition or setup 
        Tutorial tutorial = Tutorial.builder()
                 .title("Spring boot 3")
                 .description("First version of spring boot 3")
                 .published(false)
                 .build();

        Tutorial tutorial1 = Tutorial.builder()
                 .title("Spring boot 3")
                 .description("First version of spring boot 3")
                 .published(false)
                 .build();

        tutorialRepository.save(tutorial);
        tutorialRepository.save(tutorial1);

        // when - action of the behaviour than we are going to test
        List<Tutorial> tutorialList = tutorialRepository.findAll();

        // than - verify the output
        assertThat(tutorialList).isNotNull();
        assertThat(tutorialList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for get tutorial by id operation")
    @Test 
    public void givenTutorial_whenFindById_thenReturnTutorial(){
        // given - precondition or setup 
        Tutorial tutorial = Tutorial.builder()
                 .title("Spring boot 3")
                 .description("First version of spring boot 3")
                 .published(false)
                 .build();
        tutorialRepository.save(tutorial);

        // when - action or the behavior that we are going to test
        Tutorial tutorialDB = tutorialRepository.findById(tutorial.getId()).get();

        //then - verify the output
        assertThat(tutorialDB).isNotNull();
    }

    @DisplayName("JUnit test for checking if tutorial is published")
    @Test 
    public void givenTutorial_whenFindByPublished_thenReturnTutorial(){
        // given - precondition or setup
        Tutorial tutorial = Tutorial.builder()
                .id(5)
                .title("Published tutorial")
                .description("This tutorial is published")
                .published(true)
                .build();
        
        Tutorial tutorial1 = Tutorial.builder()
                .id(6)
                .title("Published tutorial")
                .description("This tutorial is also publoshed")
                .published(true)
                .build();
        tutorialRepository.save(tutorial);
        tutorialRepository.save(tutorial1);

        // when - action or the behavior that we are going to test
        List<Tutorial> tutorialDB = tutorialRepository.findByPublished(true);
        
        // then - verfy the output
        assertThat(tutorialDB.size()).isEqualTo(2);
        assertThat(tutorialDB.get(0).getId()).isEqualTo(5);
        assertThat(tutorialDB.get(1).getId()).isEqualTo(6);
    }
}
