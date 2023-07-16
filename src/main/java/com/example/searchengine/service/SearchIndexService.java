package com.example.searchengine.service;

import com.example.searchengine.entity.User;
import com.example.searchengine.exception.RoleNotFoundException;
import com.example.searchengine.factory.RoleFactory;
import com.example.searchengine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class SearchIndexService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleFactory roleFactory;

    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void save(User user) throws RoleNotFoundException {
        user.setRoles(Set.of(roleFactory.getUserRole()));
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public User findByUsername(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return byUsername.get();
        }
        throw new RuntimeException();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
