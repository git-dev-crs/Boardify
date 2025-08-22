package com.boardify.card_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveCardDto {
    Long cardId;
    Long inList;
    Long toList;
    Long userId;
}
