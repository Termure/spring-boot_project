package com.workspace.myapplication.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.workspace.myapplication.exception.ResourceNotFoundException;
import com.workspace.myapplication.model.Tutorial;
import com.workspace.myapplication.repository.TutorialRepository;
import com.workspace.myapplication.service.TutorialService;

@Service
public class TutorialServiceImpl implements TutorialService{

    private TutorialRepository tutorialRepository;

    public TutorialServiceImpl(TutorialRepository tutorialRepository){
        this.tutorialRepository = tutorialRepository;
    }

    @Override
    public Tutorial createTutorial(Tutorial tutorial){
        List<Tutorial> savedTutorial = tutorialRepository.findByTitleContaining(tutorial.getTitle());
        if (!savedTutorial.isEmpty()){
            throw new ResourceNotFoundException("Tutorial already exists with given title:" + tutorial.getTitle());
        }

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

    public List<Tutorial> getPublishedTutorials(){
        return tutorialRepository.findByPublished(true);
    }

    public Tutorial updateTutorial(long id, Tutorial newTutorialData){
        return tutorialRepository.findById(id)
            .map(_tutorial -> {
                _tutorial.setTitle(newTutorialData.getTitle());
                _tutorial.setDescription(newTutorialData.getDescription());
                _tutorial.setPublished(newTutorialData.isPublished());
                return tutorialRepository.save(_tutorial);
            })
            .orElseGet(() -> {
                return tutorialRepository.save(newTutorialData);
            });
    }

    public void deleteTutorialById(long id){
        tutorialRepository.deleteById(id);
    }

    public void deleteAllTutorials(){
        tutorialRepository.deleteAll();
    }
}
