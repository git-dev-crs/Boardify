package com.boardify.board_service.dto.response;

import com.boardify.board_service.model.RoleEnum;
import com.boardify.board_service.model.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardMemberDto {
    Long boardId;
    Long userId;

    RoleEnum role;
    LocalDateTime joinedAt;

    StatusEnum status;
    LocalDateTime statusAt;
}
