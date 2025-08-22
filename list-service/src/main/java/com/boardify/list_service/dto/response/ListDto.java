package com.boardify.list_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListDto {
    Long id;
    String title;
    String description;
    Long boardId;
    Long createdBy;
    LocalDateTime createdAt;
}
