package com.boardify.card_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardTagDto {
    Long cardId;
    String title;
    Long addedBy;
    LocalDateTime addedAt;
}
