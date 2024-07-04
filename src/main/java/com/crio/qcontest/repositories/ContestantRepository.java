package com.crio.qcontest.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.crio.qcontest.entities.Contestant;

public class ContestantRepository implements IContestantRepository {
    private final Map<String, Contestant> contestantMap;

    public ContestantRepository() {
        contestantMap = new HashMap<>();
    }

    @Override
public Contestant save(Contestant contestant) {
    // Save the contestant to the repository
    contestantMap.put(generateKey(contestant.getContest().getId(), contestant.getUser().getName()), contestant);
    return contestant;
}



    @Override
    public Optional<Contestant> find(Long contestId, String userName) {
        // Find a contestant by contest ID and user name
        return Optional.ofNullable(contestantMap.get(generateKey(contestId, userName)));
    }

    @Override
    public List<Contestant> findAllByContestId(Long contestId) {
        // Find all contestants for a given contest ID
        return contestantMap.values().stream()
                .filter(contestant -> contestant.getContest().getId().equals(contestId))
                .collect(Collectors.toList());
    }

    @Override
    public Contestant delete(Contestant contestant) {
        return contestantMap.remove(generateKey(contestant.getContest().getId(), contestant.getUser().getName()));
    }

    private String generateKey(Long contestId, String userName) {
        return contestId.toString() + "-" + userName;
    }
    
    
    
}
