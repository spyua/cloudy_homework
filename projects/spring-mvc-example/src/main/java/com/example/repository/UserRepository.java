package com.example.repository;

import com.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByStatus(User.UserStatus status);

    Page<User> findByStatus(User.UserStatus status, Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
           "(:username IS NULL OR u.username LIKE %:username%) AND " +
           "(:email IS NULL OR u.email LIKE %:email%) AND " +
           "(:firstName IS NULL OR u.firstName LIKE %:firstName%) AND " +
           "(:lastName IS NULL OR u.lastName LIKE %:lastName%) AND " +
           "(:status IS NULL OR u.status = :status)")
    Page<User> findBySearchCriteria(
            @Param("username") String username,
            @Param("email") String email,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("status") User.UserStatus status,
            Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
           "u.firstName LIKE %:query% OR " +
           "u.lastName LIKE %:query% OR " +
           "u.username LIKE %:query% OR " +
           "u.email LIKE %:query%")
    Page<User> findBySearchQuery(@Param("query") String query, Pageable pageable);

    long countByStatus(User.UserStatus status);
}