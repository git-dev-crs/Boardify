package com.boardify.card_service.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentDto {
    String comment;
    Long cardId;
    Long addedBy;
    Long taggedComment;
}
