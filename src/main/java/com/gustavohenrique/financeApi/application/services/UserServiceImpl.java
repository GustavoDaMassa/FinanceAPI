package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.UserService;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.inputs.UserInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


        private final UserRepository userRepository;

        @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public User createUser(UserInput input) {

            if(userRepository.existsByEmail(input.getEmail())) throw new RuntimeException();
            User user = new User(null,input.getName(),input.getEmail(), input.getPassword(), null,null);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserInput input) {

            var user = userRepository.findById(id).orElseThrow();
            if(userRepository.existsByEmail(input.getEmail())) throw new RuntimeException();
            user.setEmail(input.getEmail());
            user.setName(input.getName());
            user.setPassword(input.getPassword());
            return userRepository.save(user);
    }

    @Override
    public User deleteUser(Long id) {
        var user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
        return user;
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }
}


