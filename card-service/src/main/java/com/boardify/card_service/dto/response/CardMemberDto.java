package com.boardify.card_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardMemberDto {
    Long cardId;
    Long userId;
    LocalDateTime joinedAt;
    Long addedBy;
}
