package com.boardify.card_service.feign;

import com.boardify.card_service.dto.ResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "BOARD-SERVICE",
        configuration = FeignClientConfig.class
)
public interface BoardInterface {
    @GetMapping("/board/isBoardAndTagValid/{boardId}/{tag}/")
    ResponseWrapper<Boolean> isBoardAndTagValid(
            @PathVariable("boardId") Long boardId,
            @PathVariable("tag") String tag
    );
}
