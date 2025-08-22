package com.boardify.board_service.repository;

import com.boardify.board_service.model.workspaceboard.WorkspaceBoardId;
import com.boardify.board_service.model.workspaceboard.WorkspaceBoardModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceBoardRepository extends JpaRepository<WorkspaceBoardModel, WorkspaceBoardId> {
    @Query("SELECT COUNT(w) = 1 FROM WorkspaceBoardModel w WHERE w.id.boardId = :boardId")
    Boolean isBoardInWorkspace(@Param("boardId") Long boardId);

    @Query("SELECT w.id.boardId FROM WorkspaceBoardModel w WHERE w.id.workspaceId = :workspaceId")
    List<Long> getBoardIdsOfWorkspace(@Param("workspaceId") Long workspaceId);

    @Modifying
    @Transactional
    @Query("""
            DELETE FROM WorkspaceBoardModel wbm
            WHERE wbm.id.boardId = :boardId""")
    void deleteByBoardId(@Param("boardId") Long boardId);
}
