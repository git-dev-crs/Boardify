package com.boardify.board_service.dto.service.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardTitleDto {
    Long id;
    String title;
    LocalDateTime createdAt;
    PriorityEnum priorityEnum;
}
