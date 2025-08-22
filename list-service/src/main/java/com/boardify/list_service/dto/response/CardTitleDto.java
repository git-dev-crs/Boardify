package com.boardify.list_service.dto.response;


import com.boardify.list_service.model.PriorityEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardTitleDto {
    Long id;
    String title;
    LocalDateTime createdAt;
    PriorityEnum priorityEnum;
}
