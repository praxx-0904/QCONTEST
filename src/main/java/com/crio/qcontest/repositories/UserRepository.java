// UserRepository.java

package com.crio.qcontest.repositories;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.crio.qcontest.entities.User;

public class UserRepository implements IUserRepository {
    private final Map<Long, User> userMap;
    private Long autoIncrement = 1L;

    public UserRepository() {
        userMap = new HashMap<>();
    }

    // Complete the implementation of save method
    @Override
    public User save(User user) {
        // Assign an auto-incremented ID to the user
        user.setId(autoIncrement);
        // Store the user in the repository
        userMap.put(autoIncrement, user);
        // Increment the auto-increment variable for the next user
        autoIncrement++;
        return user;
    }

    // Complete the implementation of findAll method
    @Override
    public List<User> findAll() {
        // Return all users stored in the repository
        return List.copyOf(userMap.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        // Find a user by ID
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public Optional<User> findByName(String name) {
        // Find a user by name
        return userMap.values().stream().filter(u -> u.getName().equals(name)).findFirst();
    }
}
