package com.boardify.workspace_service.repository;

import com.boardify.workspace_service.dto.response.WorkspaceTitleDto;
import com.boardify.workspace_service.model.WorkspaceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkspaceRepository extends JpaRepository<WorkspaceModel, Long> {
    @Query("""
            SELECT new com.boardify.workspace_service.dto.response.WorkspaceTitleDto
            (wm.id, wm.title, wm.description, wm.createdAt)
            FROM WorkspaceModel wm
            WHERE wm.id = :workspaceId""")
    WorkspaceTitleDto getWorkspaceTitleDto(@Param("workspaceId") Long workspaceId);
}
