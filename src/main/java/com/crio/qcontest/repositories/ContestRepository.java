// ContestRepository.java

package com.crio.qcontest.repositories;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.crio.qcontest.entities.Contest;
import com.crio.qcontest.entities.Level;

public class ContestRepository implements IContestRepository {
    private final Map<Long, Contest> contestMap;
    private Long autoIncrement = 1L;

    public ContestRepository() {
        contestMap = new HashMap<>();
    }

    @Override
    public Contest save(Contest contest) {
        // Save the contest with an auto-incremented ID
        Contest savedContest = new Contest(contest.getTitle(), contest.getLevel(), contest.getCreator(), contest.getQuestions(), autoIncrement);
        contestMap.put(autoIncrement, savedContest);
        autoIncrement++;
        return savedContest;
    }

    @Override
    public List<Contest> findAll() {
        // Return all contests stored in the repository
        return contestMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public Optional<Contest> findById(Long id) {
        // Find a contest by ID
        return Optional.ofNullable(contestMap.get(id));
    }

    @Override
    public List<Contest> findAllContestLevelWise(Level level) {
        // Find contests for a given difficulty level
        return contestMap.values().stream()
                .filter(contest -> contest.getLevel() == level)
                .collect(Collectors.toList());
    }
}
