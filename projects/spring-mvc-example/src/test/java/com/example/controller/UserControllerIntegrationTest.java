package com.example.controller;

import com.example.dto.CreateUserRequest;
import com.example.dto.UpdateUserRequest;
import com.example.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void createUser_ShouldReturnSuccess() throws Exception {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .phone("+1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .bio("Test user bio")
                .build();

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.firstName").value("Test"))
                .andExpect(jsonPath("$.data.lastName").value("User"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    @Order(2)
    void createUser_WithInvalidData_ShouldReturnValidationError() throws Exception {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("") // Invalid: empty username
                .email("invalid-email") // Invalid: not a valid email
                .firstName("")
                .lastName("")
                .build();

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpected(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @Order(3)
    void getUserById_ShouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("johndoe"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    @Test
    @Order(4)
    void getUserById_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/users/999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").containsString("not found"));
    }

    @Test
    @Order(5)
    void getAllUsers_ShouldReturnPagedUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpected(jsonPath("$.data.totalElements").isNumber());
    }

    @Test
    @Order(6)
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .firstName("Updated")
                .lastName("Name")
                .bio("Updated bio")
                .status(User.UserStatus.INACTIVE)
                .build();

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpected(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.data.firstName").value("Updated"))
                .andExpected(jsonPath("$.data.lastName").value("Name"))
                .andExpect(jsonPath("$.data.bio").value("Updated bio"))
                .andExpected(jsonPath("$.data.status").value("INACTIVE"));
    }

    @Test
    @Order(7)
    void searchUsers_ShouldReturnFilteredResults() throws Exception {
        mockMvc.perform(get("/api/users/search")
                .param("firstName", "John")
                .param("status", "ACTIVE"))
                .andDo(print())
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.success").value(true))
                .andExpected(jsonPath("$.data.content").isArray())
                .andExpected(jsonPath("$.data.content[0].firstName").value(containsString("John")));
    }

    @Test
    @Order(8)
    void getUserStatistics_ShouldReturnCountsByStatus() throws Exception {
        mockMvc.perform(get("/api/users/count"))
                .andDo(print())
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.success").value(true))
                .andExpected(jsonPath("$.data.active").isNumber())
                .andExpected(jsonPath("$.data.inactive").isNumber())
                .andExpected(jsonPath("$.data.suspended").isNumber());
    }

    @Test
    @Order(9)
    void checkUsernameExists_ShouldReturnCorrectStatus() throws Exception {
        // Test existing username
        mockMvc.perform(get("/api/users/exists/username/johndoe"))
                .andDo(print())
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.success").value(true))
                .andExpected(jsonPath("$.data").value(true));

        // Test non-existing username
        mockMvc.perform(get("/api/users/exists/username/nonexistentuser"))
                .andDo(print())
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.success").value(true))
                .andExpected(jsonPath("$.data").value(false));
    }

    @Test
    @Order(10)
    void deleteUser_ShouldReturnSuccess() throws Exception {
        mockMvc.perform(delete("/api/users/5"))
                .andDo(print())
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.success").value(true))
                .andExpected(jsonPath("$.message").value("User deleted successfully"));

        // Verify user is deleted
        mockMvc.perform(get("/api/users/5"))
                .andExpected(status().isNotFound());
    }
}