package com.boardify.list_service.feign;


import com.boardify.list_service.dto.ResponseWrapper;
import com.boardify.list_service.dto.response.CardTitleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "CARD-SERVICE",
        configuration = FeignClientConfig.class
)
public interface CardInterface {
    @GetMapping("/card/getTitlesInList/{listId}")
    ResponseWrapper<List<CardTitleDto>> getTitlesOfList(
            @PathVariable("listId") Long listId
    );

    @DeleteMapping("/card/deleteCardsByListId/{listId}")
    ResponseWrapper<String> deleteCommentByListId(
            @PathVariable("listId") Long listId
    );
}
