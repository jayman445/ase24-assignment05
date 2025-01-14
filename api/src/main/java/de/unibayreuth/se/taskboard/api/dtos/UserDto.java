package de.unibayreuth.se.taskboard.api.dtos;

import org.springframework.lang.NonNull;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.UUID;

//TODO: Add DTO for users.
public record UserDto(
        @Nullable UUID id, // ID of user
        @NonNull LocalDateTime createdAt, // timestamp of creation
        @NonNull String name // name of user
        ) { }
