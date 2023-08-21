package com.workspace.myapplication.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.workspace.myapplication.integration.AbstractionBaseTest;
import com.workspace.myapplication.model.Tutorial;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TutorialRepositoryIT extends AbstractionBaseTest {
    
    @Autowired TutorialRepository tutorialRepository;

    private Tutorial tutorial;

    @BeforeEach
    void setup(){
        tutorial = Tutorial.builder()
                .title("Spring boot 3.x")
                .description("Tutorial description")
                .published(true)
                .build();
    }

    @DisplayName("JUnit test for save tutorial operation")
    @Test
    void givenTutorialObject_wheSave_thenReturnSavedTutorial(){
        // when - action or the behavior than we are going to test
        Tutorial savedTutorial = tutorialRepository.save(tutorial);

        // then - verify the output
        assertThat(savedTutorial).isNotNull();
        assertThat(savedTutorial.getId()).isGreaterThan(0);
    }

    @DisplayName("JUnit test for get all tutorial operation")
    @Test
    void givenTutorial_whenFindAll_thenTutorialList(){
        // given - precondition or setup 
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
    void givenTutorial_whenFindById_thenReturnTutorial(){
        // given - precondition or setup 
        tutorialRepository.save(tutorial);

        // when - action or the behavior that we are going to test
        Tutorial tutorialDB = tutorialRepository.findById(tutorial.getId()).get();

        //then - verify the output
        assertThat(tutorialDB).isNotNull();
    }

    @DisplayName("JUnit test for checking if tutorial is published")
    @Test
    void givenTutorial_whenFindByPublished_thenReturnTutorial(){
        // given - precondition or setup       
        Tutorial tutorial1 = Tutorial.builder()               
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
        //assertThat(tutorialDB.get(0).getId()).isEqualTo(5);
        //assertThat(tutorialDB.get(1).getId()).isEqualTo(6);
    }

    @DisplayName("JUnit test for update tutorial")
    @Test
    void givenTutorial_whenUpdateTutorial_thenReturnUpdatedTutorial(){
        // given - precondition or setup       
        tutorialRepository.save(tutorial);

        // when - action or the behavior that we are going to test
        Tutorial savedTutorial = tutorialRepository.findById(tutorial.getId()).get();
        savedTutorial.setTitle("New title");
        savedTutorial.setDescription("New description");
        savedTutorial.setPublished(true);
        Tutorial updatedTutorial = tutorialRepository.save(savedTutorial);

        // then - verfy the output
        assertThat(updatedTutorial.getTitle()).isEqualTo("New title");
        assertThat(updatedTutorial.getDescription()).isEqualTo("New description");
        assertThat(updatedTutorial.isPublished()).isEqualTo(true);
    }

    @DisplayName("JUNit test for delete tutorial operation")
    @Test
    void givenTutorial_whenDelete_thenRemove() {
        // given - precondition or setup
        tutorialRepository.save(tutorial);

        // when - action or the behavior that we are going to test
        tutorialRepository.delete(tutorial);
        Optional<Tutorial> tutorialOptional = tutorialRepository.findById(tutorial.getId());
        
        // then - verify the output
        assertThat(tutorialOptional).isEmpty();

    }

    @DisplayName("JUnit test for delete all tutorils")
    @Test
    void givenTutorialList_whenDeteleAll_thenRemoveAll(){
        // given - precondition or setup
        Tutorial tutorial1 = Tutorial.builder()               
                .title("Published tutorial")
                .description("This tutorial is also publoshed")
                .published(true)
                .build();
        tutorialRepository.save(tutorial);
        tutorialRepository.save(tutorial1);

        // when - action or the behavior that we are going to test
        tutorialRepository.deleteAll();

        // then - verify the output
        assertThat(tutorialRepository.findAll()).isEmpty();
    }

    @DisplayName("JUnit test from cusotme query using JPQL with index")
    @Test
    void givenTitlePublished_whenFindByJPQL_thenReturnTutorialObject(){
        //given - precondition or setup
        tutorialRepository.save(tutorial);
     
        // when - action or the behavior that we are going to test
        Tutorial savedTutorial = tutorialRepository.findByJPQL(tutorial.getTitle(), tutorial.isPublished());
        
        // then - verify the output
        assertThat(savedTutorial).isNotNull();
    }

    @DisplayName("JUnit test for custom query using JPQL with named params")
    @Test
    void givenTitlePublished_whenFindByJPQLNamedParms_retunTutorial(){
        // given - precondition or setup 
        tutorialRepository.save(tutorial);
       
        // when - action or the behavior that we are giong to test
        Tutorial saveTutorial = tutorialRepository.findByJPQLNamedParams(tutorial.getTitle(), tutorial.isPublished());

        // then - verify the output
        assertThat(saveTutorial).isNotNull();
    }

    @DisplayName("JUnit test for custom query using native SQL with index params")
    @Test
    void givenTitleDescription_whenFindByNativeSQL_thenReturnTutorial(){
        // given - recondition or setup 
        tutorialRepository.save(tutorial);
    
        // when - action or the behavior that we are going to test
        Tutorial findTutorial = tutorialRepository.findByNativeSQL(tutorial.getTitle(), tutorial.getDescription());

        // then - verify the output
        assertThat(findTutorial).isNotNull();
      }

    @DisplayName("JUnit test for custom query using native SQL with named params")
    @Test
    void givenTitleDescription_whenFindByNativeSQLNamed_thenReturnTutorial(){
          // given - recondition or setup 
          tutorialRepository.save(tutorial);
      
          // when - action or the behavior that we are going to test
          Tutorial findTutorial = tutorialRepository.findByNativeSQLNamed(tutorial.getTitle(), tutorial.getDescription());
  
          // then - verify the output
          assertThat(findTutorial).isNotNull();
        }

}
