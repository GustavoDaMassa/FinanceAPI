package com.gustavohenrique.financeApi.application.interfaces;

import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.inputs.UserInput;

import java.util.List;

public interface UserService {

    List<User> listUsers();

    User findUserByEmail(String email);

    User createUser(UserInput input);

    User updateUser(Long id, UserInput input);

    User deleteUser(Long id);
}
