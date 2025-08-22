package com.boardify.profile_service.feign;

import com.boardify.profile_service.dto.ResponseWrapper;
import com.boardify.profile_service.dto.request.BoardInvitationDecisionDto;
import com.boardify.profile_service.dto.service.BoardInvitationDto;
import com.boardify.profile_service.dto.service.BoardTitleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "BOARD-SERVICE",
        configuration = FeignClientConfig.class
)
public interface BoardInterface {
    @GetMapping("/board/getBoardTitleByUser/{userId}")
    ResponseWrapper<List<BoardTitleDto>> getBoardTitleByUser(
            @PathVariable("userId") Long userId
    );

    @GetMapping("/board/invitationsByUser/{userId}")
    ResponseWrapper<List<BoardInvitationDto>> getInvitationsByUser(
            @PathVariable("userId") Long userId
    );

    @PostMapping("/board/deleteUserAccount/{userId}")
    ResponseWrapper<String> deleteUserAccount(
            @PathVariable("userId") Long userId
    );

    @PostMapping("/board/decideInvite")
    ResponseWrapper<String> decideInvite(
            @RequestBody BoardInvitationDecisionDto body
    );
}
