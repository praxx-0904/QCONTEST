// Contest.java

package com.crio.qcontest.entities;

import java.util.List;

public class Contest {
    private final String title;
    private final Level level;
    private final User createdBy;
    private final List<Question> questions;
    private ContestStatus contestStatus;
    private final Long id;

    public Contest(String title, Level level, User createdBy, List<Question> questions, Long id) {
        this.title = title;
        this.level = level;
        this.createdBy = createdBy;
        validateQuestions(questions, level); // Call validateQuestions method
        this.questions = questions;
        this.contestStatus = ContestStatus.NOT_STARTED;
        this.id = id;
    }

    public Contest(String title, Level level, User createdBy, List<Question> questions) {
        this(title, level, createdBy, questions, null);
    }

    // Complete the implementation of validateQuestions method
    private void validateQuestions(List<Question> questions, Level level) {
        // Verify if the level of all the questions and contest matches.
        for (Question question : questions) {
            if (question.getLevel() != level) {
                throw new IllegalArgumentException("All questions must be of the same level as the contest.");
            }
        }
    }

    // Complete the implementation of endContest method
    public void endContest() {
        // Mark the status of contest as ended.
        contestStatus = ContestStatus.ENDED;
    }

    public String getTitle() {
        return title;
    }

    public Level getLevel() {
        return level;
    }

    public User getCreator() {
        return createdBy;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public ContestStatus getContestStatus() {
        return contestStatus;
    }

    public Long getId() {
        return id;
    }



    

    @Override
    public String toString() {
        return "Contest [id=" + id + "]";
    }

    public void setContestStatus(ContestStatus inProgress) {}
}
