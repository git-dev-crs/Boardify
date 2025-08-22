package com.boardify.card_service.repository;

import com.boardify.card_service.dto.response.CardTitleDto;
import com.boardify.card_service.model.CardModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<CardModel, Long> {

    @Query("SELECT new com.boardify.card_service.dto.response.CardTitleDto(c.id, c.title, c.createdAt, c.priority) FROM CardModel c WHERE c.listId = :listId")
    List<CardTitleDto> getCardsByListId(@Param("listId") Long listId);


    @Query("UPDATE CardModel SET listId = :listId WHERE id=:id")
    @Modifying
    @Transactional
    int updateListId(@Param("listId") Long listId, @Param("id") Long cardId);

    @Query("SELECT cm.listId FROM CardModel cm WHERE cm.id = :cardId")
    Long getListId(@Param("cardId") Long cardId);

    @Query("SELECT c.id FROM CardModel c WHERE c.listId = :listId")
    List<Long> getCardIdsByListId(@Param("listId") Long listId);
}