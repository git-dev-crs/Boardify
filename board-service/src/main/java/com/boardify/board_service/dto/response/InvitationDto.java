package com.boardify.board_service.dto.response;

import com.boardify.board_service.model.RoleEnum;
import com.boardify.board_service.model.invitation.InvitationDecisionEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationDto {
    Long boardId;
    Long invitedUser;

    Long invitedBy;
    LocalDateTime invitedAt;
    RoleEnum role;

    InvitationDecisionEnum decision;
    LocalDateTime decisionAt;
}
