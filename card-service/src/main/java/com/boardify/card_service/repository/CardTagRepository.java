package com.boardify.card_service.repository;

import com.boardify.card_service.model.cardtag.CardTagId;
import com.boardify.card_service.model.cardtag.CardTagModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTagRepository extends JpaRepository<CardTagModel, CardTagId> {
    @Modifying
    @Query("DELETE FROM CardTagModel ctm WHERE ctm.id.cardId = :cardId")
    void deleteAllByCardId(@Param("cardId") Long cardId);
}
