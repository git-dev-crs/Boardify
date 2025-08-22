package com.boardify.board_service.model.tag;

import com.boardify.board_service.model.member.BoardMemberId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class BoardTagId implements Serializable {
    @Column(name = "board_id")
    Long boardId;
    String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardTagId that = (BoardTagId) o;
        return Objects.equals(boardId, that.boardId) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, title);
    }
}
