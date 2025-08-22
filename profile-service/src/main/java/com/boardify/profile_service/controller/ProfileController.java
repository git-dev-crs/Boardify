package com.boardify.profile_service.controller;

import com.boardify.profile_service.dto.ResponseWrapper;
import com.boardify.profile_service.dto.request.BoardInvitationDecisionDto;
import com.boardify.profile_service.dto.request.CreateAccountDto;
import com.boardify.profile_service.dto.request.WorkspaceInvitationDecisionDto;
import com.boardify.profile_service.dto.response.UserDetailsDto;
import com.boardify.profile_service.dto.response.WorkspaceAndBoardByUserDto;
import com.boardify.profile_service.dto.service.*;
import com.boardify.profile_service.model.UserModel;
import com.boardify.profile_service.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile/")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    //public
    @GetMapping("/detail/{userId}")
    public ResponseEntity<ResponseWrapper<UserDetailsDto>> getUserDetails(
            @PathVariable("userId") Long userId
    ) {
        return profileService.getUserDetails(userId);
    }

    //public
    @GetMapping("/home/{userId}")
    public ResponseEntity<ResponseWrapper<WorkspaceAndBoardByUserDto>> getWorkspacesAndBoards(
            @PathVariable("userId") Long userId
    ) {
        return profileService.getWorkspacesAndBoards(userId);
    }

    //public
    @GetMapping("/workspaceInvitations/{userId}")
    public ResponseEntity<ResponseWrapper<List<WorkspaceInvitationDto>>> getWorkspaceInvitations(
            @PathVariable("userId") Long userId
    ) {
        return profileService.getWorkspaceInvitations(userId);
    }

    //public
    @GetMapping("/boardInvitations/{userId}")
    public ResponseEntity<ResponseWrapper<List<BoardInvitationDto>>> getBoardInvitations(
            @PathVariable("userId") Long userId
    ) {
        return profileService.getBoardInvitations(userId);
    }

    //public
    @GetMapping("/workspaces/{userId}")
    public ResponseEntity<ResponseWrapper<List<WorkspaceTitleDto>>> getWorkspaceTitles(
            @PathVariable("userId") Long userId
    ) {
        return profileService.getWorkspaceTitles(userId);
    }

    //public
    @GetMapping("/boards/{userId}")
    public ResponseEntity<ResponseWrapper<List<BoardTitleDto>>> getBoardsTitle(
            @PathVariable("userId") Long userId
    ) {
        return profileService.getBoardsTitle(userId);
    }

    //private
    @GetMapping("/isUserValid/{user_id}")
    public ResponseEntity<ResponseWrapper<Boolean>> isUserValid(@PathVariable("user_id") Long userId) {
        return profileService.isUserValid(userId);
    }

    //public
    @PostMapping("/createAccount")
    public ResponseEntity<ResponseWrapper<UserModel>> createAccount(
            @RequestBody CreateAccountDto body
    ) {
        return profileService.createAccount(body.firstName, body.middleName, body.lastName, body.email);
    }

    @PostMapping("/decideWorkspaceInvitation")
    public ResponseEntity<ResponseWrapper<String>> decideWorkspaceInvitation(
            @RequestBody WorkspaceInvitationDecisionDto body
    ) {
        return profileService.decideWorkspaceInvitation(body);
    }

    @PostMapping("/decideBoardInvitation")
    public ResponseEntity<ResponseWrapper<String>> decideBoardInvitation(
            @RequestBody BoardInvitationDecisionDto body
    ) {
        return profileService.decideBoardInvitation(body);
    }

    //public
    @DeleteMapping("/deleteAccount/{userId}")
    public ResponseEntity<ResponseWrapper<String>> deleteAccount(
            @PathVariable("userId") Long userId
    ) {
        return profileService.deleteAccount(userId);
    }
}
