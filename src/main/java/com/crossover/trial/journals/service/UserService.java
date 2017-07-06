package com.crossover.trial.journals.service;

import com.crossover.trial.journals.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserByLoginName(String loginName);

    void subscribe(User user, Long categoryId);

    User findById(Long id);

    List<User> findAll();
}