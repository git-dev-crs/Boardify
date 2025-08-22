package com.boardify.board_service.model.tag;


import com.boardify.board_service.dto.response.BoardTagDto;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "board_tag")
@NoArgsConstructor
@AllArgsConstructor
public class BoardTagModel {
    @EmbeddedId
    BoardTagId id;

    Long createdBy;
    LocalDateTime createdAt;

    public BoardTagModel(BoardTagId id, Long createdBy) {
        this.id = id;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }

    public BoardTagDto toTagDto() {
        return new BoardTagDto(
                id.getBoardId(),
                id.getTitle(),
                createdBy,
                createdAt
        );
    }
}
