package com.boardify.profile_service.dto.response;


import com.boardify.profile_service.dto.service.BoardTitleDto;
import com.boardify.profile_service.dto.service.WorkspaceTitleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceAndBoardByUserDto {
    List<WorkspaceTitleDto> workspaces;
    List<BoardTitleDto> board;
}
