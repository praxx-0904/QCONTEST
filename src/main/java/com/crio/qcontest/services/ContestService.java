package com.crio.qcontest.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.crio.qcontest.entities.Contest;
import com.crio.qcontest.entities.ContestStatus;
import com.crio.qcontest.entities.Contestant;
import com.crio.qcontest.entities.Level;
import com.crio.qcontest.entities.Question;
import com.crio.qcontest.entities.User;
import com.crio.qcontest.repositories.IContestRepository;
import com.crio.qcontest.repositories.IContestantRepository;
import com.crio.qcontest.repositories.IQuestionRepository;
import com.crio.qcontest.repositories.IUserRepository;

public class ContestService {
    private final IContestantRepository contestantRepository;
    private final IContestRepository contestRepository;
    private final IQuestionRepository questionRepository;
    private final IUserRepository userRepository;

    public ContestService(IContestantRepository contestantRepository, IContestRepository contestRepository,
            IQuestionRepository questionRepository, IUserRepository userRepository) {
        this.contestantRepository = contestantRepository;
        this.contestRepository = contestRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    public Contest createContest(String title, Level level, String createdBy, Integer numQuestions) {
        // Find the user who is creating the contest
        User creator = userRepository.findByName(createdBy)
                .orElseThrow(() -> new RuntimeException("User with name: " + createdBy + " not found!"));

        // Retrieve all questions from the repository
        List<Question> allQuestions = questionRepository.findAll();

        // Shuffle the questions
        Collections.shuffle(allQuestions);

        // Select a specified number of random questions
        List<Question> selectedQuestions = allQuestions.stream().limit(numQuestions).collect(Collectors.toList());

        // Create the contest with the selected questions
        Contest contest = new Contest(title, level, creator, selectedQuestions);

        // Save the contest to the repository
        return contestRepository.save(contest);
    }

    public List<Contestant> runContest(Long contestId, String createdBy) {
        // Check if the user is the creator of the contest
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest with ID " + contestId + " not found!"));
        if (!contest.getCreator().getName().equals(createdBy)) {
            throw new RuntimeException("User " + createdBy + " is not the creator of contest " + contestId + "!");
        }

        // Check if the contest has not started yet
        if (contest.getContestStatus() != ContestStatus.NOT_STARTED) {
            throw new RuntimeException("Contest " + contestId + " has already started!");
        }

        // Set the contest status to IN_PROGRESS
        contest.setContestStatus(ContestStatus.IN_PROGRESS);

        // Get the level of the contest
        Level contestLevel = contest.getLevel();

        // Get all contestants registered for the contest
        List<Contestant> contestantList = contestantRepository.findAllByContestId(contestId);

        // Get the questions solved by the contestants
        Random rand = new Random();
        contestantList.forEach(contestant -> {
            int currentContestPoints = rand.nextInt(100); // Simulated currentContestPoints
            contestant.setCurrentContestPoints(currentContestPoints);
            // Update the user's score based on the difficulty level of the contest
            User user = contestant.getUser();
            int currentScore = user.getTotalScore();
            int newScore;
            switch (contestLevel) {
                case LOW:
                    newScore = currentScore + (currentContestPoints - 50);
                    break;
                case MEDIUM:
                    newScore = currentScore + (currentContestPoints - 30);
                    break;
                case HIGH:
                default:
                    newScore = currentScore + currentContestPoints;
                    break;
            }
            user.modifyScore(newScore);
        });

        // Sort the contestants in descending order of currentContestPoints
        List<Contestant> updatedContestants = contestantList.stream()
                .sorted((c1, c2) -> c2.getCurrentContestPoints() - c1.getCurrentContestPoints())
                .collect(Collectors.toList());

        // End the contest
        contest.setContestStatus(ContestStatus.ENDED);

        return updatedContestants;
    }


    private int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.ints(min, (max + 1)).findFirst().getAsInt();
    }

    private int calculateTotalPoints(List<Question> questions) {
        return questions.stream().mapToInt(Question::getScore).sum();
    }


    public List<Contest> getContests(Level level) {
        if (level == null) {
            // If difficulty level is not specified, return all contests
            return contestRepository.findAll();
        } else {
            // Otherwise, return contests for the given difficulty level
            return contestRepository.findAllContestLevelWise(level);
        }
    }


    public Contestant createContestant(Long contestId, String userName) {
        // Find the contest
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest with ID " + contestId + " not found"));
    
        // Check if the contest has ended
        if (contest.getContestStatus() == ContestStatus.ENDED) {
            throw new RuntimeException("Contest with ID " + contestId + " has already ended");
        }
    
        // Find the user
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new RuntimeException("User with name " + userName + " not found"));
    
        // Create and save the contestant
        Contestant contestant = new Contestant(user, contest);
        return contestantRepository.save(contestant);
    }
    



    public Contestant deleteContestant(Long contestId, String userName) {
        // Find the contest
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest with ID " + contestId + " not found"));

        // Find the user
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new RuntimeException("User with name " + userName + " not found"));

        // Find the contestant
        Contestant contestant = contestantRepository.find(contestId, userName)
                .orElseThrow(() -> new RuntimeException("Contestant not found for Contest ID " + contestId
                        + " and User Name " + userName));

        // Check if the contest has not started yet
        if (contest.getContestStatus() != ContestStatus.NOT_STARTED) {
            throw new RuntimeException("Contest has already started");
        }

        // Check if the user is the contest creator
        if (!contest.getCreator().equals(user)) {
            throw new RuntimeException("User is not the creator of the contest");
        }

        // Delete the contestant
        return contestantRepository.delete(contestant);
    }


    public List<Contestant> contestHistory(Long contestId) {
        // Find the contest
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest with ID " + contestId + " not found!"));
    
        // Check if the contest has ended
        if (contest.getContestStatus() == ContestStatus.NOT_STARTED) {
            throw new RuntimeException("Contest with ID " + contestId + " has not started or is not valid!");
        }
    
        // Check if the contest has ended
        // if (contest.getContestStatus() == ContestStatus.ENDED) {
        //     throw new RuntimeException("Contest with ID " + contestId + " has already ended");
        // }
    
        List<Contestant> contestantList = contestantRepository.findAllByContestId(contestId);
    
        // Sort the contestants in descending order of currentContestPoints
        List<Contestant> sortedContestants = contestantList.stream()
                .sorted(Comparator.comparingInt(Contestant::getCurrentContestPoints).reversed())
                .collect(Collectors.toList());
    
        return sortedContestants;
    }
    
    
}
