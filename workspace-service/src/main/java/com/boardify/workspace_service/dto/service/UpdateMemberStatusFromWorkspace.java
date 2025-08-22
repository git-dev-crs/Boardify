package com.boardify.workspace_service.dto.service;


import com.boardify.workspace_service.model.RoleEnum;
import com.boardify.workspace_service.model.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberStatusFromWorkspace {
    Long workspaceId;
    Long userId;
    RoleEnum role;
    StatusEnum status;
}
