package com.boardify.card_service.repository;

import com.boardify.card_service.model.ChecklistModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ChecklistRepository extends JpaRepository<ChecklistModel, Long> {
    @Query("UPDATE ChecklistModel SET isChecked = :isChecked, checkedBy = :checkedBy, checkedAt = :checkedAt WHERE id=:id")
    @Modifying
    @Transactional
    int updateCheckStatus(
            @Param("isChecked") Boolean isChecked,
            @Param("checkedBy") Long checkBy,
            @Param("checkedAt") LocalDateTime checkedAt,
            @Param("id") Long id
    );

    @Modifying
    @Query("DELETE FROM ChecklistModel cm WHERE cm.cardId = :cardId")
    void deleteAllByCardId(@Param("cardId") Long cardId);
}
