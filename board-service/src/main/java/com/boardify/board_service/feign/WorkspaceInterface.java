package com.boardify.board_service.feign;

import com.boardify.board_service.dto.ResponseWrapper;
import com.boardify.board_service.dto.service.response.WorkspaceMemberRoleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "WORKSPACE-SERVICE",
        configuration = FeignClientConfig.class
)
public interface WorkspaceInterface {
    @GetMapping(value = "/workspace/getWorkspaceMembersIdRole/{workspaceId}/{userId}/")
    ResponseWrapper<List<WorkspaceMemberRoleDto>> getWorkspaceMembersIdRole(
            @PathVariable("workspaceId") Long workspaceId,
            @PathVariable("userId") Long userId
    );

    @GetMapping("/workspace/isWorkspaceAndMemberValid/{workspaceId}/{userId}/")
    ResponseWrapper<Boolean> isWorkspaceAndMemberValid(
            @PathVariable("workspaceId") Long workspaceId,
            @PathVariable("userId") Long userId
    );
}
