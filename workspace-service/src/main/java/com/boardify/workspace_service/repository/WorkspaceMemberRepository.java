package com.boardify.workspace_service.repository;

import com.boardify.workspace_service.dto.response.WorkspaceMemberRoleDto;
import com.boardify.workspace_service.model.RoleEnum;
import com.boardify.workspace_service.model.StatusEnum;
import com.boardify.workspace_service.model.member.WorkspaceMemberId;
import com.boardify.workspace_service.model.member.WorkspaceMemberModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMemberModel, WorkspaceMemberId> {
    @Query("SELECT m.id.workspaceId FROM WorkspaceMemberModel m WHERE m.id.userId = :userId AND m.status = 'ACTIVE'")
    List<Long> getWorkspaceIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT m FROM WorkspaceMemberModel m WHERE m.id.workspaceId = :workspaceId")
    List<WorkspaceMemberModel> getMembersByWorkspaceId(@Param("workspaceId") Long workspaceId);

    @Query("SELECT new com.boardify.workspace_service.dto.response.WorkspaceMemberRoleDto(m.id.userId, m.role) FROM WorkspaceMemberModel m WHERE m.id.workspaceId = :workspaceId")
    List<WorkspaceMemberRoleDto> getMembersIdRoleByWorkspaceId(@Param("workspaceId") Long workspaceId);

    @Query("""
            SELECT COUNT(wmm)
            FROM WorkspaceMemberModel wmm
            WHERE wmm.id.workspaceId = :workspaceId
                AND wmm.status = :status
            """)
    Integer getActiveCount(@Param("workspaceId") Long workspaceId, @Param("status") StatusEnum status);

    @Modifying
    @Transactional
    @Query("""
            UPDATE WorkspaceMemberModel wmm
            SET wmm.role = :role, wmm.status = :status, wmm.statusAt = :statusAt
            WHERE wmm.id.workspaceId = :workspaceId AND wmm.id.userId = :userId
            """)
    void updateRoleAndStatus(
            @Param("workspaceId") Long workspaceId,
            @Param("userId") Long userId,
            @Param("role") RoleEnum role,
            @Param("status") StatusEnum status,
            @Param("statusAt") LocalDateTime statusAt
    );

    @Modifying
    @Transactional
    @Query("""
            UPDATE WorkspaceMemberModel wmm
            SET wmm.status = :status, wmm.statusAt = :statusAt
            WHERE wmm.id.workspaceId = :workspaceId AND wmm.id.userId = :userId
            """)
    void updateStatus(
            @Param("workspaceId") Long workspaceId,
            @Param("userId") Long userId,
            @Param("status") StatusEnum status,
            @Param("statusAt") LocalDateTime statusAt
    );


    @Modifying
    @Transactional
    @Query("""
            DELETE FROM WorkspaceMemberModel wmm
            WHERE wmm.id.workspaceId = :workspaceId
            """)
    void deleteByWorkspaceId(@Param("workspaceId") Long workspaceId);
}
