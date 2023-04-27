package com.workspace.myapplication.service;

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
    
    
}
