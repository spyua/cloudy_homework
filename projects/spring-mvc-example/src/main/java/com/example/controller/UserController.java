package com.example.controller;

import java.util.List;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.example.dto.CreateUserRequest;
import com.example.dto.PageResponse;
import com.example.dto.RestResponse;
import com.example.dto.UpdateUserRequest;
import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.service.UserService;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided information")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "User created successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data"
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Username or email already exists"
        )
    })
    public ResponseEntity<RestResponse<UserDto>> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        
        log.info("Creating user: {}", request.getUsername());
        UserDto createdUser = userService.createUser(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.success("User created successfully", createdUser));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier")
    public ResponseEntity<RestResponse<UserDto>> getUserById(
            @Parameter(description = "User ID", required = true) 
            @PathVariable Long id) {
        
        log.debug("Getting user by ID: {}", id);
        UserDto user = userService.getUserById(id);
        
        return ResponseEntity.ok(RestResponse.success(user));
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieves a user by their username")
    public ResponseEntity<RestResponse<UserDto>> getUserByUsername(
            @Parameter(description = "Username", required = true)
            @PathVariable String username) {
        
        log.debug("Getting user by username: {}", username);
        UserDto user = userService.getUserByUsername(username);
        
        return ResponseEntity.ok(RestResponse.success(user));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email", description = "Retrieves a user by their email address")
    public ResponseEntity<RestResponse<UserDto>> getUserByEmail(
            @Parameter(description = "Email address", required = true)
            @PathVariable String email) {
        
        log.debug("Getting user by email: {}", email);
        UserDto user = userService.getUserByEmail(email);
        
        return ResponseEntity.ok(RestResponse.success(user));
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves all users with pagination support")
    public ResponseEntity<RestResponse<PageResponse<UserDto>>> getAllUsers(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        
        log.debug("Getting all users with pagination: {}", pageable);
        PageResponse<UserDto> users = userService.getAllUsers(pageable);
        
        return ResponseEntity.ok(RestResponse.success(users));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get users by status", description = "Retrieves users filtered by their status")
    public ResponseEntity<RestResponse<PageResponse<UserDto>>> getUsersByStatus(
            @Parameter(description = "User status", required = true)
            @PathVariable User.UserStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        
        log.debug("Getting users by status: {}", status);
        PageResponse<UserDto> users = userService.getUsersByStatus(status, pageable);
        
        return ResponseEntity.ok(RestResponse.success(users));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active users", description = "Retrieves all active users")
    public ResponseEntity<RestResponse<List<UserDto>>> getActiveUsers() {
        
        log.debug("Getting active users");
        List<UserDto> activeUsers = userService.getActiveUsers();
        
        return ResponseEntity.ok(RestResponse.success(activeUsers));
    }

    @GetMapping("/search")
    @Operation(summary = "Search users", description = "Search users by various criteria")
    public ResponseEntity<RestResponse<PageResponse<UserDto>>> searchUsers(
            @Parameter(description = "General search query")
            @RequestParam(required = false) String q,
            @Parameter(description = "Username filter")
            @RequestParam(required = false) String username,
            @Parameter(description = "Email filter")
            @RequestParam(required = false) String email,
            @Parameter(description = "First name filter")
            @RequestParam(required = false) String firstName,
            @Parameter(description = "Last name filter")
            @RequestParam(required = false) String lastName,
            @Parameter(description = "Status filter")
            @RequestParam(required = false) User.UserStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        
        log.debug("Searching users with query: {}", q);
        
        PageResponse<UserDto> users;
        if (StringUtils.hasText(q)) {
            users = userService.searchUsers(q, pageable);
        } else {
            users = userService.searchUsers(username, email, firstName, lastName, status, pageable);
        }
        
        return ResponseEntity.ok(RestResponse.success(users));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user's information")
    public ResponseEntity<RestResponse<UserDto>> updateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        
        log.info("Updating user: {}", id);
        UserDto updatedUser = userService.updateUser(id, request);
        
        return ResponseEntity.ok(RestResponse.success("User updated successfully", updatedUser));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update user status", description = "Updates a user's status")
    public ResponseEntity<RestResponse<UserDto>> updateUserStatus(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "New status", required = true)
            @RequestParam User.UserStatus status) {
        
        log.info("Updating user status - ID: {}, status: {}", id, status);
        UserDto updatedUser = userService.updateUserStatus(id, status);
        
        return ResponseEntity.ok(RestResponse.success("User status updated successfully", updatedUser));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by their ID")
    public ResponseEntity<RestResponse<Void>> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        
        log.info("Deleting user: {}", id);
        userService.deleteUser(id);
        
        return ResponseEntity.ok(RestResponse.success("User deleted successfully", null));
    }

    @GetMapping("/count")
    @Operation(summary = "Get user statistics", description = "Get count of users by status")
    public ResponseEntity<RestResponse<Object>> getUserStatistics() {
        
        log.debug("Getting user statistics");
        
        var statistics = new java.util.HashMap<String, Object>();
        statistics.put("active", userService.getUserCountByStatus(User.UserStatus.ACTIVE));
        statistics.put("inactive", userService.getUserCountByStatus(User.UserStatus.INACTIVE));
        statistics.put("suspended", userService.getUserCountByStatus(User.UserStatus.SUSPENDED));
        
        return ResponseEntity.ok(RestResponse.success("User statistics retrieved successfully", statistics));
    }

    @GetMapping("/exists/username/{username}")
    @Operation(summary = "Check if username exists", description = "Checks if a username is already taken")
    public ResponseEntity<RestResponse<Boolean>> checkUsernameExists(
            @Parameter(description = "Username to check", required = true)
            @PathVariable String username) {
        
        boolean exists = userService.existsByUsername(username);
        String message = exists ? "Username already exists" : "Username is available";
        
        return ResponseEntity.ok(RestResponse.success(message, exists));
    }

    @GetMapping("/exists/email/{email}")
    @Operation(summary = "Check if email exists", description = "Checks if an email is already registered")
    public ResponseEntity<RestResponse<Boolean>> checkEmailExists(
            @Parameter(description = "Email to check", required = true)
            @PathVariable String email) {
        
        boolean exists = userService.existsByEmail(email);
        String message = exists ? "Email already exists" : "Email is available";
        
        return ResponseEntity.ok(RestResponse.success(message, exists));
    }
}