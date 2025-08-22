package com.boardify.board_service.model.workspaceboard;

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
public class WorkspaceBoardId implements Serializable {
    Long workspaceId;
    Long boardId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkspaceBoardId that = (WorkspaceBoardId) o;
        return Objects.equals(boardId, that.boardId) &&
                Objects.equals(workspaceId, that.workspaceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, workspaceId);
    }
}
