package com.boardify.board_service.dto.request;

import com.boardify.board_service.model.invitation.InvitationDecisionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecideInvitationDto {
    Long boardId;
    Long invitedUser;
    InvitationDecisionEnum decision;
}
