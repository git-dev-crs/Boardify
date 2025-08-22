package com.boardify.workspace_service.feign;

import com.boardify.workspace_service.dto.ResponseWrapper;
import com.boardify.workspace_service.dto.service.AddMemberFromWorkspaceDto;
import com.boardify.workspace_service.dto.service.BoardTitleDto;
import com.boardify.workspace_service.dto.service.UpdateMemberStatusFromWorkspace;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "BOARD-SERVICE",
        configuration = FeignClientConfig.class
)
public interface BoardInterface {
    @GetMapping("/board/getBoardTitleInWorkspace/{workspaceId}")
    ResponseWrapper<List<BoardTitleDto>>getBoardTitleInWorkspace(
            @PathVariable("workspaceId") Long workspaceId
    );

    @PostMapping("/board/addMemberToBoardFromWorkspace")
    ResponseWrapper<Boolean> addMemberToBoardFromWorkspace(
            @RequestBody AddMemberFromWorkspaceDto body
    );

    @PostMapping("/board/updateMemberStatusFromWorkspace")
    ResponseWrapper<Boolean> updateMemberStatusFromWorkspace(
            @RequestBody UpdateMemberStatusFromWorkspace body
    );

    @PostMapping("/board/removeUserFromWorkspace/{workspaceId}/{userId}")
    ResponseWrapper<String> removeUserFromWorkspace(
            @PathVariable("workspaceId") Long workspaceId,
            @PathVariable("userId") Long userId
    );

    @DeleteMapping("/board/deleteBoardFromWorkspace/{workspaceId}")
    ResponseWrapper<String> deleteBoardFromWorkspace(
            @PathVariable("workspaceId") Long workspaceId
    );
}
