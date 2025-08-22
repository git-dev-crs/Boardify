package com.boardify.card_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    Long id;
    String comment;
    Long cardId;
    Long createdBy;
    LocalDateTime createdAt;
    Long taggedCommentId;

    List<CommentDto> replyComments;
}
