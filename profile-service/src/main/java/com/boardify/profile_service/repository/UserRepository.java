package com.boardify.profile_service.repository;

import com.boardify.profile_service.model.UserModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    @Query("SELECT u FROM UserModel u WHERE u.email = :email AND deletedAt IS NULL")
    Optional<UserModel> findByEmailNotDeleted(@Param("email") String email);

    @Query("SELECT COUNT(u) = 1 FROM UserModel u WHERE u.id = :id AND deletedAt IS NULL")
    Boolean isUserValid(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("""
            UPDATE UserModel um
            SET um.deletedAt = :deletedAt
            WHERE um.id = :id
            """)
    void deleteUser(@Param("id") Long id, @Param("deletedAt") LocalDateTime deletedAt);
}
