package com.boardify.board_service.dto.service.response;

import com.boardify.board_service.model.RoleEnum;
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