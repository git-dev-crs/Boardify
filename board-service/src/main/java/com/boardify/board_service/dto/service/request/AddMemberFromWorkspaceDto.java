package com.boardify.board_service.dto.service.request;


import com.boardify.board_service.model.RoleEnum;
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