package com.boardify.board_service.feign;

import com.boardify.board_service.dto.ResponseWrapper;
import com.boardify.board_service.dto.service.response.ListWithCardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "LIST-SERVICE",
        configuration = FeignClientConfig.class
)
public interface ListInterface {
    @GetMapping("/list/getListAndCardByBoardId/{boardId}")
    ResponseWrapper<List<ListWithCardDto>> getListAndCardByBoardId(
            @PathVariable("boardId") Long boardId
    );

    @DeleteMapping("/list/deleteListsFromBoard/{boardId}")
    ResponseWrapper<String> deleteListsFromBoard(
            @PathVariable("boardId") Long boardId
    );
}
