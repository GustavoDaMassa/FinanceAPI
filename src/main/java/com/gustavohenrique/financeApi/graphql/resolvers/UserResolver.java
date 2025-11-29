package com.gustavohenrique.financeApi.graphql.resolvers;

import com.gustavohenrique.financeApi.application.interfaces.UserService;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.UserDTO;
import com.gustavohenrique.financeApi.graphql.inputs.UserInput;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserResolver {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> listUsers() {
        return userService.listUsers()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public UserDTO findUserByEmail(@Argument String email) {
        User user = userService.findUserByEmail(email);
        return modelMapper.map(user, UserDTO.class);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public UserDTO createUser(@Argument UserInput input) {
        User createdUser = userService.createUser(input);
        return modelMapper.map(createdUser, UserDTO.class);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public UserDTO updateUser(@Argument Long id, @Argument UserInput input) {
        User updatedUser = userService.updateUser(id, input);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO deleteUser(@Argument Long id) {
        User deletedUser = userService.deleteUser(id);
        return modelMapper.map(deletedUser, UserDTO.class);
    }
}
