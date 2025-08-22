package com.boardify.board_service.model.workspaceboard;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "workspace_board")
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceBoardModel {
    @EmbeddedId
    WorkspaceBoardId id;

    Long addedBy;
    LocalDateTime addedAt;

    public WorkspaceBoardModel(WorkspaceBoardId id, Long addedBy){
        this.id = id;
        this.addedBy = addedBy;
        this.addedAt = LocalDateTime.now();
    }
}
