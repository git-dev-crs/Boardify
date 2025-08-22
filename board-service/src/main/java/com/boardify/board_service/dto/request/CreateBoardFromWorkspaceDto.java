package com.boardify.board_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBoardFromWorkspaceDto {
    String title;
    String description;
    Long createdBy;
    Long workspaceId;
}
