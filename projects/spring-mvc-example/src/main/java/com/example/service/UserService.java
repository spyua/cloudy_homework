package com.example.service;

import com.example.dto.CreateUserRequest;
import com.example.dto.UpdateUserRequest;
import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.template.dto.PageResponse;
import com.template.exception.BusinessException;
import com.template.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDto createUser(CreateUserRequest request) {
        log.debug("Creating user with username: {}", request.getUsername());

        // 檢查用戶名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username '" + request.getUsername() + "' already exists");
        }

        // 檢查郵箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email '" + request.getEmail() + "' already exists");
        }

        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        
        log.info("User created successfully with ID: {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    @Cacheable(value = "users", key = "#id")
    public UserDto getUserById(Long id) {
        log.debug("Fetching user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        
        return userMapper.toDto(user);
    }

    public UserDto getUserByUsername(String username) {
        log.debug("Fetching user with username: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username '" + username + "' not found"));
        
        return userMapper.toDto(user);
    }

    public UserDto getUserByEmail(String email) {
        log.debug("Fetching user with email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email '" + email + "' not found"));
        
        return userMapper.toDto(user);
    }

    public PageResponse<UserDto> getAllUsers(Pageable pageable) {
        log.debug("Fetching users with pagination: {}", pageable);
        
        Page<User> userPage = userRepository.findAll(pageable);
        return userMapper.toPageResponse(userPage);
    }

    public PageResponse<UserDto> getUsersByStatus(User.UserStatus status, Pageable pageable) {
        log.debug("Fetching users by status: {} with pagination: {}", status, pageable);
        
        Page<User> userPage = userRepository.findByStatus(status, pageable);
        return userMapper.toPageResponse(userPage);
    }

    public PageResponse<UserDto> searchUsers(String username, String email, String firstName, 
                                           String lastName, User.UserStatus status, Pageable pageable) {
        log.debug("Searching users with criteria - username: {}, email: {}, firstName: {}, lastName: {}, status: {}", 
                  username, email, firstName, lastName, status);
        
        Page<User> userPage = userRepository.findBySearchCriteria(
                StringUtils.hasText(username) ? username : null,
                StringUtils.hasText(email) ? email : null,
                StringUtils.hasText(firstName) ? firstName : null,
                StringUtils.hasText(lastName) ? lastName : null,
                status,
                pageable
        );
        
        return userMapper.toPageResponse(userPage);
    }

    public PageResponse<UserDto> searchUsers(String query, Pageable pageable) {
        log.debug("Searching users with query: {}", query);
        
        Page<User> userPage = userRepository.findBySearchQuery(query, pageable);
        return userMapper.toPageResponse(userPage);
    }

    @Transactional
    @CachePut(value = "users", key = "#id")
    public UserDto updateUser(Long id, UpdateUserRequest request) {
        log.debug("Updating user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        
        userMapper.updateEntityFromRequest(request, user);
        User updatedUser = userRepository.save(user);
        
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        log.debug("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    @Transactional
    @CachePut(value = "users", key = "#id")
    public UserDto updateUserStatus(Long id, User.UserStatus status) {
        log.debug("Updating user status - ID: {}, status: {}", id, status);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        
        user.setStatus(status);
        User updatedUser = userRepository.save(user);
        
        log.info("User status updated successfully - ID: {}, status: {}", updatedUser.getId(), status);
        return userMapper.toDto(updatedUser);
    }

    public List<UserDto> getActiveUsers() {
        log.debug("Fetching active users");
        
        List<User> activeUsers = userRepository.findByStatus(User.UserStatus.ACTIVE);
        return userMapper.toDtoList(activeUsers);
    }

    public long getUserCountByStatus(User.UserStatus status) {
        log.debug("Counting users by status: {}", status);
        
        return userRepository.countByStatus(status);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}