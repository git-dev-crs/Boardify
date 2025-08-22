package com.boardify.profile_service.dto.request;

import com.boardify.profile_service.utills.InvitationDecisionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardInvitationDecisionDto {
    Long boardId;
    Long invitedUser;
    InvitationDecisionEnum decision;
}

