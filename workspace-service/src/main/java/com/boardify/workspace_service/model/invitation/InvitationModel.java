package com.boardify.workspace_service.model.invitation;


import com.boardify.workspace_service.dto.response.InvitationDto;
import com.boardify.workspace_service.model.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "invitation")
@NoArgsConstructor
@AllArgsConstructor
public class InvitationModel {
    @EmbeddedId
    InvitationModelId id;

    Long invitedBy;
    LocalDateTime invitedAt;

    @Enumerated(EnumType.STRING)
    RoleEnum role;

    @Enumerated(EnumType.STRING)
    InvitationDecisionEnum decision;
    LocalDateTime decisionAt;

    public InvitationModel(InvitationModelId id, Long invitedBy, RoleEnum role, InvitationDecisionEnum decision){
        this.id = id;
        this.invitedBy = invitedBy;
        this.invitedAt = LocalDateTime.now();
        this.role = role;
        this.decision = decision;
    }

    public InvitationDto toInvitationDto(){
        return new InvitationDto(
                id.workspaceId,
                id.invitedUser,
                invitedBy,
                invitedAt,
                role,
                decision,
                decisionAt
        );
    }
}
