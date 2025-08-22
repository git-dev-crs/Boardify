package com.boardify.board_service.model.member;

import com.boardify.board_service.dto.response.BoardMemberDto;
import com.boardify.board_service.model.RoleEnum;
import com.boardify.board_service.model.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "board_member")
@NoArgsConstructor
@AllArgsConstructor
public class BoardMemberModel {
    @EmbeddedId
    BoardMemberId id;

    @Enumerated(EnumType.STRING)
    RoleEnum role;
    LocalDateTime joinedAt;

    @Enumerated(EnumType.STRING)
    StatusEnum status;
    LocalDateTime statusAt;

    public BoardMemberModel(BoardMemberId id, RoleEnum role){
        this.id = id;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
        this.status = StatusEnum.ACTIVE;
    }

    public BoardMemberDto toBoardMemberDto(){
        return new BoardMemberDto(
                id.boardId,
                id.userId,
                role,
                joinedAt,
                status,
                statusAt
        );
    }
}
