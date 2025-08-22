package com.boardify.workspace_service.dto.response;

import com.boardify.workspace_service.model.RoleEnum;
import com.boardify.workspace_service.model.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMemberDto {
    Long workspaceId;
    Long userId;

    RoleEnum role;
    LocalDateTime joinedAt;

    StatusEnum status;
    LocalDateTime statusAt;
}
