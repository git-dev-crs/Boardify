package com.boardify.board_service.model;

import com.boardify.board_service.dto.response.BoardDto;
import com.boardify.board_service.dto.response.BoardWithListAndCardDto;
import com.boardify.board_service.model.invitation.InvitationModel;
import com.boardify.board_service.model.member.BoardMemberModel;
import com.boardify.board_service.model.tag.BoardTagModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "board")
@NoArgsConstructor
@AllArgsConstructor
public class BoardModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String description;
    Long createdBy;
    LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    List<BoardMemberModel> members;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    List<InvitationModel> invitations;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    List<BoardTagModel> tags;

    public BoardModel(String title, String description, Long createdBy) {
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }

    public BoardDto toBoardDto() {
        return new BoardDto(
                id,
                title,
                description,
                createdBy,
                createdAt,

                members != null
                        ? members.stream().map(BoardMemberModel::toBoardMemberDto).toList()
                        : List.of(),
                invitations != null
                        ? invitations.stream().map(InvitationModel::toInvitationDto).toList()
                        : List.of(),
                tags != null
                        ? tags.stream().map(BoardTagModel::toTagDto).toList()
                        : List.of()
        );
    }

    public BoardWithListAndCardDto toBoardWithListAndCardDto() {
        return new BoardWithListAndCardDto(
                id,
                title,
                description,
                createdBy,
                createdAt,

                members != null
                        ? members.stream().map(BoardMemberModel::toBoardMemberDto).toList()
                        : List.of(),
                invitations != null
                        ? invitations.stream().map(InvitationModel::toInvitationDto).toList()
                        : List.of(),
                tags != null
                        ? tags.stream().map(BoardTagModel::toTagDto).toList()
                        : List.of(),

                List.of()
        );
    }
}
