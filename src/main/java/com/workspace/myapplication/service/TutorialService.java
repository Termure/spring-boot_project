package com.workspace.myapplication.service;

import java.util.List;
import java.util.Optional;
import com.workspace.myapplication.model.Tutorial;

public interface TutorialService {

    Tutorial createTutorial(Tutorial tutorial);
    List<Tutorial> getTutorials(String titile);
    Optional<Tutorial> getTutorialById(long id);
    List<Tutorial> getPublishedTutorials();
    Tutorial updateTutorial(long id, Tutorial tutorial);
    void deleteTutorialById(long id);
    void deleteAllTutorials();
}
