package com.boardify.board_service.repository;

import com.boardify.board_service.model.invitation.InvitationDecisionEnum;
import com.boardify.board_service.model.invitation.InvitationModel;
import com.boardify.board_service.model.invitation.InvitationModelId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationModel, InvitationModelId> {
    @Query("SELECT im FROM InvitationModel im WHERE im.id.invitedUser = :userId")
    List<InvitationModel> getInvitationsByUser(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE InvitationModel i SET i.decision = :decision, i.decisionAt = :decisionAt WHERE i.id = :id")
    void updateInvitationDecision(@Param("decision") InvitationDecisionEnum decision,
                                  @Param("decisionAt") LocalDateTime decisionAt,
                                  @Param("id") InvitationModelId id);

    @Modifying
    @Transactional
    @Query("""
            DELETE FROM InvitationModel im
            WHERE im.id.boardId = :boardId""")
    void deleteByBoardId(@Param("boardId") Long boardId);
}
