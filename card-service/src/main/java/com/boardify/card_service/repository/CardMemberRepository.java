package com.boardify.card_service.repository;

import com.boardify.card_service.model.cardmember.CardMemberId;
import com.boardify.card_service.model.cardmember.CardMemberModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardMemberRepository extends JpaRepository<CardMemberModel, CardMemberId> {
    @Modifying
    @Query("DELETE FROM CardMemberModel cmm WHERE cmm.id.cardId = :cardId")
    void deleteAllByCardId(@Param("cardId") Long cardId);
}
