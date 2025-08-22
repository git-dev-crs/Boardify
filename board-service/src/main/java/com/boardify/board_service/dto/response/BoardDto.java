package com.boardify.board_service.dto.response;

import com.boardify.board_service.model.BoardModel;
import com.boardify.board_service.model.invitation.InvitationModel;
import com.boardify.board_service.model.member.BoardMemberModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    Long id;
    String title;
    String description;
    Long createdBy;
    LocalDateTime createdAt;

    List<BoardMemberDto> members;

    List<InvitationDto> invitations;

    List<BoardTagDto> tags;
}
