package com.crio.qcontest.entities;

public class User {
    private final String name;
    private Integer totalScore;
    private Long id;

    public User(String name, Long id) {
        this.name = name;
        this.id = id;
        this.totalScore = 100;
    }

    public User(String name) {
        this(name, null);
    }

    public String getName() {
        return name;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Complete the implementation of modifyScore method
    public void modifyScore(Integer score) {
        // Set an appropriate totalScore.
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative.");
        }
        totalScore = score;
    }

    @Override
    public String toString() {
        return "User [id=" + id + "]";
    }
}
