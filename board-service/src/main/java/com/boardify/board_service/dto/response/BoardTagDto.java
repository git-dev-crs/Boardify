package com.boardify.board_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardTagDto {
    Long boardId;
    String title;
    Long createdBy;
    LocalDateTime createdAt;
}
