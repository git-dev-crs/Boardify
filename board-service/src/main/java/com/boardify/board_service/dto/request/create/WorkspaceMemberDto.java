package com.boardify.board_service.dto.request.create;

import com.boardify.board_service.model.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMemberDto {
    Long userId;
    RoleEnum role;
}