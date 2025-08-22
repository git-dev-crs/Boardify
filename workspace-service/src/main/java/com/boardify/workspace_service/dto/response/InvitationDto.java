package com.boardify.workspace_service.dto.response;

import com.boardify.workspace_service.model.RoleEnum;
import com.boardify.workspace_service.model.invitation.InvitationDecisionEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationDto {
    Long workspaceId;
    Long invitedUser;

    Long invitedBy;
    LocalDateTime invitedAt;
    RoleEnum role;

    InvitationDecisionEnum decision;
    LocalDateTime decisionAt;
}
