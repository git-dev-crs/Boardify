package com.boardify.workspace_service.controller;


import com.boardify.workspace_service.dto.request.*;
import com.boardify.workspace_service.dto.ResponseWrapper;
import com.boardify.workspace_service.dto.response.*;
import com.boardify.workspace_service.model.WorkspaceModel;
import com.boardify.workspace_service.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspace/")
public class WorkspaceController {
    @Autowired
    private WorkspaceService workspaceService;

    //public
    @GetMapping("/details/{workspace_id}")
    public ResponseEntity<ResponseWrapper<WorkspaceDto>> getWorkspace(
            @PathVariable("workspace_id") Long workspaceId,
            @RequestParam Long userId
    ) {
        return workspaceService.getWorkspace(workspaceId, userId);
    }

    //public
    @GetMapping("/{workspace_id}")
    public ResponseEntity<ResponseWrapper<WorkspaceWithBoardDto>> getWorkspaceWithBoards(
            @PathVariable("workspace_id") Long workspaceId,
            @RequestParam Long userId
    ) {
        return workspaceService.getWorkspaceWithBoards(workspaceId, userId);
    }

    //private
    @GetMapping("/getWorkspaceTitleByUser/{userId}")
    public ResponseEntity<ResponseWrapper<List<WorkspaceTitleDto>>> getWorkspaceTitleByUser(
            @PathVariable("userId") Long userId
    ) {
        return workspaceService.getWorkspaceTitleByUser(userId);
    }

    //private
    @GetMapping("/getWorkspaceMembersIdRole/{workspaceId}/{userId}/")
    public ResponseEntity<ResponseWrapper<List<WorkspaceMemberRoleDto>>> getWorkspaceMembersIdRole(
            @PathVariable("workspaceId") Long workspaceId,
            @PathVariable("userId") Long userId
    ) {
        return workspaceService.getWorkspaceMembersIdRole(workspaceId, userId);
    }

    //private
    @GetMapping("/isWorkspaceAndMemberValid/{workspaceId}/{userId}/")
    public ResponseEntity<ResponseWrapper<Boolean>> isWorkspaceAndMemberValid(
            @PathVariable("workspaceId") Long workspaceId,
            @PathVariable("userId") Long userId
    ) {
        return workspaceService.isWorkspaceAndMemberValid(workspaceId, userId);
    }

    //private
    @GetMapping("/invitationsByUser/{userId}")
    public ResponseEntity<ResponseWrapper<List<InvitationDto>>> getInvitationsByUser(
            @PathVariable("userId") Long userId
    ){
        return workspaceService.getInvitationsByUser(userId);
    }

//    public
    @PostMapping("create")
    public ResponseEntity<ResponseWrapper<WorkspaceModel>> createWorkspace(@RequestBody CreateWorkspaceDto workspaceDto) {
        return workspaceService.createWorkspace(workspaceDto);
    }

//    public
    @PostMapping("/{workspace_id}/invite")
    public ResponseEntity<ResponseWrapper<String>> invite(
            @PathVariable("workspace_id") Long workspaceId,
            @RequestBody WorkspaceInviteDto body) {
        return workspaceService.invite(workspaceId, body.getInvitedBy(), body.getInvitedUser(), body.getRole());
    }

//    private
    @PostMapping("/decideInvite")
    public ResponseEntity<ResponseWrapper<String>> decideInvite(
            @RequestBody DecideInvitationDto body
    ) {
        return workspaceService.decideInvite(body.getWorkspaceId(), body.getInvitedUser(), body.getDecision());
    }

//    public
    @PostMapping("/removeUser")
    public ResponseEntity<ResponseWrapper<String>> removeUser(
            @RequestBody RemoveUserDto body
    ) {
        return workspaceService.removeUser(body.getWorkspaceId(), body.getUserId(), body.getRemoverId());
    }

    //private
    @PostMapping("/deleteUser/{userId}")
    public ResponseEntity<ResponseWrapper<String>> deleteUser(
            @PathVariable("userId") Long userId
    ){
        return workspaceService.deleteUser(userId);
    }

//    public
    @DeleteMapping("/deleteWorkspace/{workspaceId}")
    public ResponseEntity<ResponseWrapper<String>> deleteWorkspace(
            @PathVariable("workspaceId") Long workspaceId,
            @RequestParam("userId") Long userId
    ) {
        return workspaceService.deleteWorkspace(workspaceId, userId);
    }
}
