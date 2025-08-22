package com.boardify.profile_service.feign;

import com.boardify.profile_service.dto.ResponseWrapper;
import com.boardify.profile_service.dto.request.WorkspaceInvitationDecisionDto;
import com.boardify.profile_service.dto.service.WorkspaceInvitationDto;
import com.boardify.profile_service.dto.service.WorkspaceTitleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "WORKSPACE-SERVICE",
        configuration = FeignClientConfig.class
)
public interface WorkspaceInterface {
    @GetMapping("/workspace/getWorkspaceTitleByUser/{userId}")
    ResponseWrapper<List<WorkspaceTitleDto>> getWorkspaceTitleByUser(
            @PathVariable("userId") Long userId
    );

    @GetMapping("/workspace/invitationsByUser/{userId}")
    ResponseWrapper<List<WorkspaceInvitationDto>> getInvitationsByUser(
            @PathVariable("userId") Long userId
    );

    @PostMapping("/workspace/decideInvite")
    ResponseWrapper<String> decideInvite(
            @RequestBody WorkspaceInvitationDecisionDto body
    );

    @PostMapping("/workspace/deleteUser/{userId}")
    ResponseWrapper<String> deleteUser(
            @PathVariable("userId") Long userId
    );
}
