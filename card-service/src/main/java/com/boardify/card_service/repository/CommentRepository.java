package com.boardify.card_service.repository;

import com.boardify.card_service.model.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<CommentModel, Long> {
    @Query("SELECT cm.id FROM CommentModel cm WHERE cm.cardId = :cardId")
    List<Long> getCommentIdsByCardId(@Param("cardId") Long cardId);

    @Modifying
    @Query("DELETE FROM CommentModel cm WHERE cm.taggedCommentId = :commentId")
    void deleteAllByTaggedId(@Param("commentId") Long commentId);

    @Modifying
    @Query("DELETE FROM CommentModel cm WHERE cm.cardId = :cardId")
    void deleteAllByCardId(@Param("cardId") Long cardId);

    @Modifying
    @Query("DELETE FROM CommentModel c WHERE c.taggedCommentId IN :taggedIds")
    void deleteAllByTaggedIds(@Param("taggedIds") List<Long> taggedIds);

    @Modifying
    @Query("DELETE FROM CommentModel c WHERE c.id IN :ids")
    void deleteAllByIds(@Param("ids") List<Long> ids);
}
