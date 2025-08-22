package com.boardify.list_service.feign;

import com.boardify.list_service.dto.ResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "BOARD-SERVICE",
        configuration = FeignClientConfig.class
)
public interface BoardInterface {
    @GetMapping("/board/isBoardAndMemberValid/{boardId}/{userId}/")
    ResponseWrapper<Boolean> isBoardAndMemberValid(
            @PathVariable("boardId") Long boardId,
            @PathVariable("userId") Long userId
    );
}
