package com.boardify.workspace_service.dto.request;

import com.boardify.workspace_service.model.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceInviteDto {
    Long invitedUser;
    Long invitedBy;
    RoleEnum role;
}
