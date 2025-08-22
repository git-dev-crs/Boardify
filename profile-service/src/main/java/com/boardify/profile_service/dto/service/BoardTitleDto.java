package com.boardify.profile_service.dto.service;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardTitleDto {
    Long boardId;
    String title;
    String description;
    LocalDateTime createdAt;
}
