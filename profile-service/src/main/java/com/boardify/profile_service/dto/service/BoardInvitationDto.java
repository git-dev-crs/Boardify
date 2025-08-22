package com.boardify.profile_service.dto.service;

import com.boardify.profile_service.utills.InvitationDecisionEnum;
import com.boardify.profile_service.utills.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardInvitationDto {
    Long boardId;
    Long invitedUser;

    Long invitedBy;
    LocalDateTime invitedAt;
    RoleEnum role;

    InvitationDecisionEnum decision;
    LocalDateTime decisionAt;
}
