package com.boardify.list_service.repository;

import com.boardify.list_service.model.ListModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListRepository extends JpaRepository<ListModel, Long> {
    @Query("SELECT lm FROM ListModel lm WHERE lm.boardId = :boardId")
    List<ListModel> getByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT lm.id FROM ListModel lm WHERE lm.boardId = :boardId")
    List<Long> getIdsByBoardId(@Param("boardId") Long boardId);
}
