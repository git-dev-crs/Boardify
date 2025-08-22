package com.boardify.board_service.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardTitleDto {
    Long boardId;
    String title;
    String description;
    LocalDateTime createdAt;
}
