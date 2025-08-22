package com.boardify.card_service.dto.request.create;

import com.boardify.card_service.model.PriorityEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardDto {
    String title;
    String description;
    Long listId;
    Long createdBy;
    PriorityEnum priority;
    List<CreateCardChecklistDto> checklists;
}
