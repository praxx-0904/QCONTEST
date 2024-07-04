// QuestionService.java

package com.crio.qcontest.services;

import java.util.Collections;
import java.util.List;

import com.crio.qcontest.entities.Level;
import com.crio.qcontest.entities.Question;
import com.crio.qcontest.repositories.IQuestionRepository;

public class QuestionService {

    private final IQuestionRepository questionRepository;

    public QuestionService(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // List all contests if difficulty level is not specified
    public List<Question> getQuestions(Level level) {
        if (level == null) {
            // Return all questions if difficulty level is not specified
            return questionRepository.findAll();
        } else {
            // Return questions for the specified difficulty level
            return questionRepository.findAllQuestionLevelWise(level);
        }
    }

	public Question createQuestion(String title, Level valueOf, Integer score) {
		return null;
	}
}


