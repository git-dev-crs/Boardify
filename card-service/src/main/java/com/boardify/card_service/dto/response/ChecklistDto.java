package com.boardify.card_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistDto {
    Long id;
    String text;
    Integer seqNo;
    Long cardId;
    Long createdBy;
    LocalDateTime createdAt;
    Boolean isChecked;
    Long checkedBy;
    LocalDateTime checkedAt;
}
