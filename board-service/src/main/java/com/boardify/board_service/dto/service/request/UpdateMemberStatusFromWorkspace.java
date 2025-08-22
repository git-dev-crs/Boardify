package com.boardify.board_service.dto.service.request;


import com.boardify.board_service.model.RoleEnum;
import com.boardify.board_service.model.StatusEnum;
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
