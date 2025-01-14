package de.unibayreuth.se.taskboard.api.controller;

import de.unibayreuth.se.taskboard.api.dtos.UserDto;
import de.unibayreuth.se.taskboard.api.mapper.UserDtoMapper;
import de.unibayreuth.se.taskboard.business.ports.UserService;
import de.unibayreuth.se.taskboard.business.domain.User;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@OpenAPIDefinition(
        info = @Info(
                title = "TaskBoard",
                version = "0.0.1"
        )
)
@Tag(name = "Users")
@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    // TODO: Add GET /api/users endpoint to retrieve all users.
    // TODO: Add GET /api/users/{id} endpoint to retrieve a user by ID.
    // TODO: Add POST /api/users endpoint to create a new user based on a provided user DTO.
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

        @GetMapping
        public ResponseEntity<List<UserDto>> getAllUsers() {
            List<User> users = userService.getAllUsers();

            // convert to Dtos
            List<UserDto> userDtos = users.stream()
                    .map(userDtoMapper::fromBusiness)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userDtos);
        }

        @GetMapping("/{id}")
        public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
            Optional<User> user = userService.getUserById(id);
            Optional<UserDto> userDto = user.map(userDtoMapper::fromBusiness);
            return userDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PostMapping
        public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
                User user = new User(userDto.name());
                User createdUser = userService.createUser(user);
                UserDto createdUserDto = userDtoMapper.fromBusiness(createdUser);
                return ResponseEntity.ok(createdUserDto);
        }
}
