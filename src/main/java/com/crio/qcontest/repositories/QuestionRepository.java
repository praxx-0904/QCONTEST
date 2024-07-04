// QuestionRepository.java

package com.crio.qcontest.repositories;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.crio.qcontest.entities.Level;
import com.crio.qcontest.entities.Question;

public class QuestionRepository implements IQuestionRepository {
    private final Map<Long, Question> questionMap;
    private Long autoIncrement = 1L;

    public QuestionRepository() {
        questionMap = new HashMap<>();
    }

    @Override
    public Question save(Question question) {
        return question;
        // Implementation not shown as it remains the same from the previous task.
    }

    @Override
    public List<Question> findAll() {
        // Return all questions stored in the repository
        return List.copyOf(questionMap.values());
    }

    @Override
    public Optional<Question> findById(Long id) {
        return null;
        // Implementation not shown as it remains the same from the previous task.
    }

    @Override
    public List<Question> findAllQuestionLevelWise(Level level) {
        // Find all questions for a given difficulty level
        return questionMap.values().stream()
                .filter(question -> question.getLevel() == level)
                .collect(Collectors.toList());
    }

    @Override
    public Integer count() {
        return null;
        // Implementation not shown as it remains the same from the previous task.
    }
}


