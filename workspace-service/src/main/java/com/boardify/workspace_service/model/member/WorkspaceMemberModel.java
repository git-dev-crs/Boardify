package com.boardify.workspace_service.model.member;

import com.boardify.workspace_service.dto.response.WorkspaceDto;
import com.boardify.workspace_service.dto.response.WorkspaceMemberDto;
import com.boardify.workspace_service.model.RoleEnum;
import com.boardify.workspace_service.model.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "workspace_member")
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMemberModel {

    @EmbeddedId
    WorkspaceMemberId id;

    @Enumerated(EnumType.STRING)
    RoleEnum role;

    LocalDateTime joinedAt;

    @Enumerated(EnumType.STRING)
    StatusEnum status;

    LocalDateTime statusAt;

    public WorkspaceMemberModel(WorkspaceMemberId memberKey, RoleEnum role){
        this.id = memberKey;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
        this.status = StatusEnum.ACTIVE;
    }

    public WorkspaceMemberDto toWorkspaceMemberDto(){
        return new WorkspaceMemberDto(
                id.workspaceId,
                id.userId,
                role,
                joinedAt,
                status,
                statusAt
        );
    }
}
