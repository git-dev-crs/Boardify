package com.boardify.workspace_service.dto.request;

import com.boardify.workspace_service.model.invitation.InvitationDecisionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecideInvitationDto {
    Long workspaceId;
    Long invitedUser;
    InvitationDecisionEnum decision;
}
