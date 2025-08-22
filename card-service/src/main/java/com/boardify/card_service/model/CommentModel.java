package com.boardify.card_service.model;

import com.boardify.card_service.dto.response.CommentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
public class CommentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String comment;
    @Column(name = "card_id")
    Long cardId;
    Long createdBy;
    LocalDateTime createdAt;
    Long taggedCommentId;

    public CommentModel(String comment, Long cardId, Long createdBy) {
        this.comment = comment;
        this.cardId = cardId;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }

    public CommentModel(String comment, Long cardId, Long createdBy, Long taggedCommentId) {
        this.comment = comment;
        this.cardId = cardId;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.taggedCommentId = taggedCommentId;
    }

    public CommentDto toDto() {
        return new CommentDto(
                id,
                comment,
                cardId,
                createdBy,
                createdAt,
                taggedCommentId,
                List.of()
        );
    }
}
