package com.boardify.board_service.dto.response;


import com.boardify.board_service.dto.service.response.ListWithCardDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardWithListAndCardDto {
    Long id;
    String title;
    String description;
    Long createdBy;
    LocalDateTime createdAt;

    List<BoardMemberDto> members;

    List<InvitationDto> invitations;

    List<BoardTagDto> tags;

    List<ListWithCardDto> lists;
}
