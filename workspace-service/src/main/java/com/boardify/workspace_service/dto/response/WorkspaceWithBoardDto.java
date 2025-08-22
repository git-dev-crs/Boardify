package com.boardify.workspace_service.dto.response;


import com.boardify.workspace_service.dto.service.BoardTitleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceWithBoardDto {
    Long id;
    String title;
    String description;
    Long createdBy;
    LocalDateTime createdAt;

    List<WorkspaceMemberDto> members;
    List<InvitationDto> invited;

    List<BoardTitleDto> boards;
}
