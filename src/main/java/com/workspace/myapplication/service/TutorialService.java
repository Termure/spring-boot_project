package com.workspace.myapplication.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.workspace.myapplication.model.Tutorial;
import com.workspace.myapplication.repository.TutorialRepository;

@Service
public class TutorialService {

    @Autowired 
    TutorialRepository tutorialRepository;

    public Tutorial createTutorial(Tutorial tutorial){
        return tutorialRepository.save(new Tutorial(tutorial.getTitle(), 
                                                    tutorial.getDescription(), 
                                                    tutorial.isPublished() || false));
    }

    public List<Tutorial> getTutorials(String title){
        List<Tutorial> tutorials = new ArrayList<Tutorial>();
        if(title == null)
            tutorialRepository.findAll().forEach(tutorials::add);
        else
            tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);
        return tutorials;
    }

    public Optional<Tutorial> getTutorialById(long id){
        return tutorialRepository.findById(id);
    }
}
