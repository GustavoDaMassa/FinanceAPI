package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.UserService;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.enums.Role;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.exception.EmailAlreadyExistException;
import com.gustavohenrique.financeApi.exception.InvalidPasswordException;
import com.gustavohenrique.financeApi.exception.UserNotFoundException;
import com.gustavohenrique.financeApi.graphql.inputs.ChangeEmailInput;
import com.gustavohenrique.financeApi.graphql.inputs.ChangePasswordInput;
import com.gustavohenrique.financeApi.graphql.inputs.UserInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException(email));
    }

    @Override
    public User createUser(UserInput input) {
        if(userRepository.existsByEmail(input.getEmail())) {
            throw new EmailAlreadyExistException(input.getEmail());
        }

        User user = User.builder()
                .name(input.getName())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .build();

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserInput input) {
        var user = findById(id);

        // Only check email uniqueness if email is being changed
        if (!user.getEmail().equals(input.getEmail()) && userRepository.existsByEmail(input.getEmail())) {
            throw new EmailAlreadyExistException(input.getEmail());
        }

        user.setEmail(input.getEmail());
        user.setName(input.getName());

        // Only update password if provided and different
        if (input.getPassword() != null && !input.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(input.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    public User deleteUser(Long id) {
        var user = findById(id);
        userRepository.delete(user);
        return user;
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
    }

    @Override
    public User changeEmail(Long id, ChangeEmailInput input) {
        var user = findById(id);
        if (!passwordEncoder.matches(input.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }
        if (!user.getEmail().equals(input.getNewEmail()) && userRepository.existsByEmail(input.getNewEmail())) {
            throw new EmailAlreadyExistException(input.getNewEmail());
        }
        user.setEmail(input.getNewEmail());
        return userRepository.save(user);
    }

    @Override
    public User changePassword(Long id, ChangePasswordInput input) {
        var user = findById(id);
        if (!passwordEncoder.matches(input.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }
        user.setPassword(passwordEncoder.encode(input.getNewPassword()));
        return userRepository.save(user);
    }

    @Override
    public User deleteMyAccount(Long id, String currentPassword) {
        var user = findById(id);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidPasswordException();
        }
        userRepository.delete(user);
        return user;
    }
}


