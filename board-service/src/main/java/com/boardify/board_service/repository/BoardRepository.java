package com.boardify.board_service.repository;

import com.boardify.board_service.dto.response.BoardDto;
import com.boardify.board_service.dto.response.BoardTitleDto;
import com.boardify.board_service.model.BoardModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardModel, Long> {
    @Query("""
                SELECT b FROM BoardModel b
                LEFT JOIN FETCH b.members
                WHERE b.id = :id
            """)
    Optional<BoardModel> findBoardWithMembers(@Param("id") Long id);

    @Query("""
            SELECT new com.boardify.board_service.dto.response.BoardTitleDto(b.id, b.title, b.description, b.createdAt)
            FROM BoardModel b, WorkspaceBoardModel wb
            WHERE wb.id.workspaceId = :workspaceId AND b.id = wb.id.boardId
            """)
    List<BoardTitleDto> getBoardTitlesFromWorkspaceId(@Param("workspaceId") Long workspaceId);

    @Query("""
            SELECT new com.boardify.board_service.dto.response.BoardTitleDto(b.id, b.title, b.description, b.createdAt)
            FROM BoardModel b, BoardMemberModel m
            WHERE m.id.userId = :userId AND b.id = m.id.boardId
            """)
    List<BoardTitleDto> getBoardTitlesByUser(@Param("userId") Long userId);


//    @Query("""
//                SELECT b FROM BoardModel b
//                LEFT JOIN FETCH b.members
//                LEFT JOIN FETCH b.invitations
//                LEFT JOIN FETCH b.tags
//                WHERE b.id = :id
//            """)
//    Optional<BoardModel> findCompleteBoardData(@Param("id") Long id);
//
//    @Query("""
//            SELECT new com.boardify.board_service.dto.response.BoardDto(b, m, i, t)
//            FROM BoardModel b
//            LEFT JOIN b.members m
//            LEFT JOIN b.invitations i
//            LEFT JOIN b.tags t
//            WHERE b.id = :id
//            """)
//    BoardDto findBoardDtoById(@Param("id") Long id);

}
