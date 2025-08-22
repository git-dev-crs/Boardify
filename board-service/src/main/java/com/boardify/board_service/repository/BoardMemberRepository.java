package com.boardify.board_service.repository;

import com.boardify.board_service.model.RoleEnum;
import com.boardify.board_service.model.StatusEnum;
import com.boardify.board_service.model.member.BoardMemberId;
import com.boardify.board_service.model.member.BoardMemberModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BoardMemberRepository extends JpaRepository<BoardMemberModel, BoardMemberId> {
    @Query("""
            SELECT bmm.id.boardId
            FROM BoardMemberModel bmm
            WHERE bmm.id.userId = :userId
            """)
    List<Long> getBoardIdsByUser(@Param("userId") Long userId);

    @Query("""
    SELECT COUNT(bmm)
    FROM BoardMemberModel bmm
    WHERE bmm.id.boardId = :boardId
      AND bmm.status = :status
""")
    Long getActiveCount(@Param("boardId") Long boardId, @Param("status") StatusEnum status);


    @Modifying
    @Transactional
    @Query("""
            UPDATE BoardMemberModel bmm
            SET bmm.role = :role, bmm.status = :status, bmm.statusAt = :statusAt
            WHERE bmm.id.boardId = :boardId AND bmm.id.userId = :userId
            """)
    void updateRoleAndStatus(
            @Param("boardId") Long boardId,
            @Param("userId") Long userId,
            @Param("role") RoleEnum role,
            @Param("status") StatusEnum status,
            @Param("statusAt") LocalDateTime statusAt
    );

    @Modifying
    @Transactional
    @Query("""
            UPDATE BoardMemberModel bmm
            SET bmm.status = :status, bmm.statusAt = :statusAt
            WHERE bmm.id.boardId = :boardId AND bmm.id.userId = :userId
            """)
    void updateStatus(
            @Param("boardId") Long boardId,
            @Param("userId") Long userId,
            @Param("status") StatusEnum status,
            @Param("statusAt") LocalDateTime statusAt
    );

    @Modifying
    @Transactional
    @Query("""
            UPDATE BoardMemberModel bmm
            SET bmm.status = :status, bmm.statusAt = :statusAt
            WHERE bmm.id.boardId IN :boardIds AND bmm.id.userId = :userId
            """)
    void updateStatusBulk(
            @Param("boardIds") List<Long> boardIds,
            @Param("userId") Long userId,
            @Param("status") StatusEnum status,
            @Param("statusAt") LocalDateTime statusAt
    );

    @Modifying
    @Transactional
    @Query("""
            DELETE FROM BoardMemberModel bmm
            WHERE bmm.id.boardId = :boardId""")
    void deleteByBoardId(@Param("boardId") Long boardId);
}
