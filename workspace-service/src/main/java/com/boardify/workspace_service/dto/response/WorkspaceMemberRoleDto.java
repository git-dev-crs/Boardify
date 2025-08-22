package com.boardify.workspace_service.dto.response;

import com.boardify.workspace_service.model.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMemberRoleDto {
    Long userId;
    RoleEnum role;
}