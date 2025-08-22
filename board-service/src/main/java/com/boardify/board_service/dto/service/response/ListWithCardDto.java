package com.boardify.board_service.dto.service.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListWithCardDto {
    Long id;
    String title;
    String description;
    Long boardId;
    Long createdBy;
    LocalDateTime createdAt;

    List<CardTitleDto> cards;
}
