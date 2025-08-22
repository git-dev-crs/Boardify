package com.boardify.board_service.repository;

import com.boardify.board_service.model.tag.BoardTagId;
import com.boardify.board_service.model.tag.BoardTagModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardTagRepository extends JpaRepository<BoardTagModel, BoardTagId> {
    @Modifying
    @Transactional
    @Query("""
            DELETE FROM BoardTagModel btm
            WHERE btm.id.boardId = :boardId""")
    void deleteByBoardId(@Param("boardId") Long boardId);
}
