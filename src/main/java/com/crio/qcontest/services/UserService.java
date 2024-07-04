// UserService.java

package com.crio.qcontest.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.crio.qcontest.constants.UserOrder;
import com.crio.qcontest.entities.User;
import com.crio.qcontest.repositories.IUserRepository;

public class UserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Complete the implementation of createUser method
    public User createUser(String name) {
        // Create a new user with the provided name
        User user = new User(name);
        // Save the user to the repository
        return userRepository.save(user);
    }
    

    // Complete the implementation of getUsers method
    public List<User> getUsers(UserOrder userOrder) {
        List<User> userList = new ArrayList<>(userRepository.findAll());
    
        // Check if the user list is not empty
        if (userList.isEmpty()) {
            throw new RuntimeException("User list is empty");
        }
    
        // Sort the users based on the provided order
        switch (userOrder) {
            case SCORE_ASC:
                userList.sort(Comparator.comparingInt(User::getTotalScore));
                break;
            case SCORE_DESC:
                userList.sort((u1, u2) -> Integer.compare(u2.getTotalScore(), u1.getTotalScore()));
                break;
            default:
                throw new IllegalArgumentException("Invalid user order: " + userOrder);
        }
    
        return userList;
    }
    
    
    
    
}
