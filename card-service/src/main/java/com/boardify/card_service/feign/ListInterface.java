package com.boardify.card_service.feign;

import com.boardify.card_service.dto.ResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "LIST-SERVICE",
        configuration = FeignClientConfig.class
)
public interface ListInterface {
    @GetMapping("/list/isListAndMemberValid/{list_id}/{user_id}")
    ResponseWrapper<Boolean> isListAndMemberValid(
            @PathVariable("list_id") Long listId,
            @PathVariable("user_id") Long userId
    );

    @GetMapping("/list/isListsAndUserInSameBoard/{list_id_1}/{list_id_2}/{user_id}")
    ResponseWrapper<Boolean> isListsAndUserInSameBoard(
            @PathVariable("list_id_1") Long listId1,
            @PathVariable("list_id_2") Long listId2,
            @PathVariable("user_id") Long userId
    );

    @GetMapping("/list/boardIdIfListAndMemberValid/{list_id}/{user_id}")
    ResponseWrapper<Long> boardIdIfListAndMemberValid(
            @PathVariable("list_id") Long listId,
            @PathVariable("user_id") Long userId
    );
}
