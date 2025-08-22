package com.boardify.list_service.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateListDto {
    Long boardId;
    String title;
    String description;
    Long createdBy;
}
