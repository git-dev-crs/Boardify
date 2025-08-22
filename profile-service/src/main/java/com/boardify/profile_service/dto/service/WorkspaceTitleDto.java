package com.boardify.profile_service.dto.service;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceTitleDto {
    Long id;
    String title;
    String description;
    LocalDateTime createdAt;
}
