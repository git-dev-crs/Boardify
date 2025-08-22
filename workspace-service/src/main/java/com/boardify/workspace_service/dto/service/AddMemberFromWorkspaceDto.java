package com.boardify.workspace_service.dto.service;


import com.boardify.workspace_service.model.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddMemberFromWorkspaceDto {
    Long workspaceId;
    Long userId;
    RoleEnum role;
}