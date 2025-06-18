package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.UserService;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.inputs.UserInput;

import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public List<User> listUsers() {
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }

    @Override
    public User createUser(UserInput input) {
        return null;
    }

    @Override
    public User updateUser(Long id, UserInput input) {
        return null;
    }

    @Override
    public User deleteUser(Long id) {
        return null;
    }
}
