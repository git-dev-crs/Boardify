package com.boardify.card_service.dto.response;

import com.boardify.card_service.model.PriorityEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    Long id;
    Long listId;
    String title;
    String description;
    Long createdBy;
    LocalDateTime createdAt;
    PriorityEnum priority;

    List<CardMemberDto> members;

    List<ChecklistDto> checklists;

    List<CommentDto> comments;

    List<CardTagDto> tags;
}
