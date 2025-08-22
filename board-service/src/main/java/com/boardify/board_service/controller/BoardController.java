package com.boardify.board_service.controller;


import com.boardify.board_service.dto.ResponseWrapper;
import com.boardify.board_service.dto.request.*;
import com.boardify.board_service.dto.response.BoardDto;
import com.boardify.board_service.dto.response.BoardTitleDto;
import com.boardify.board_service.dto.response.BoardWithListAndCardDto;
import com.boardify.board_service.dto.response.InvitationDto;
import com.boardify.board_service.dto.service.request.AddMemberFromWorkspaceDto;
import com.boardify.board_service.dto.service.request.UpdateMemberStatusFromWorkspace;
import com.boardify.board_service.service.BoardServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board/")
public class BoardController {
    @Autowired
    BoardServices boardServices;

//    public
    @GetMapping("/details/{board_id}")
    public ResponseEntity<ResponseWrapper<BoardDto>> getBoard(
            @PathVariable("board_id") Long boardId,
            @RequestParam Long userId
    ) {
        return boardServices.getBoard(boardId, userId);
    }

//    public
    @GetMapping("/{board_id}")
    public ResponseEntity<ResponseWrapper<BoardWithListAndCardDto>> getBoardWithListAndCard(
            @PathVariable("board_id") Long boardId,
            @RequestParam Long userId
    ) {
        return boardServices.getBoardWithListAndCard(boardId, userId);
    }

    //private
    @GetMapping("/getBoardTitleInWorkspace/{workspaceId}")
    public ResponseEntity<ResponseWrapper<List<BoardTitleDto>>> getBoardTitleInWorkspace(
            @PathVariable("workspaceId") Long workspaceId
    ) {
        return boardServices.getBoardTitleInWorkspace(workspaceId);
    }

    //private
    @GetMapping("/getBoardTitleByUser/{userId}")
    public ResponseEntity<ResponseWrapper<List<BoardTitleDto>>> getBoardTitleByUser(
            @PathVariable("userId") Long userId
    ) {
        return boardServices.getBoardTitleByUser(userId);
    }

    //private
    @GetMapping("/isBoardAndMemberValid/{boardId}/{userId}/")
    public ResponseEntity<ResponseWrapper<Boolean>> isBoardAndMemberValid(
            @PathVariable("boardId") Long boardId,
            @PathVariable("userId") Long userId
    ) {
        return boardServices.isBoardAndMemberValid(boardId, userId);
    }

//    private
    @GetMapping("/isBoardAndTagValid/{boardId}/{tag}/")
    public ResponseEntity<ResponseWrapper<Boolean>> isBoardAndTagValid(
            @PathVariable("boardId") Long boardId,
            @PathVariable("tag") String tag
    ) {
        return boardServices.isBoardAndTagValid(boardId, tag);
    }

//    private
    @GetMapping("/invitationsByUser/{userId}")
    public ResponseEntity<ResponseWrapper<List<InvitationDto>>> getInvitationsByUser(
            @PathVariable("userId") Long userId
    ){
        return boardServices.getInvitationsByUser(userId);
    }

//    public
    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper<BoardDto>> createBoard(
            @RequestBody CreateBoardDto body
    ) {
        return boardServices.createBoard(body);
    }

//    public
    @PostMapping("/createFromWorkspace")
    public ResponseEntity<ResponseWrapper<BoardDto>> createBoardFromWorkspace(
            @RequestBody CreateBoardFromWorkspaceDto body
    ) {
        System.out.println("createFromWorkspace controller hit");
        return boardServices.createBoardFromWorkspace(body);
    }

    //public
    @PostMapping("/{board_id}/invite")
    public ResponseEntity<ResponseWrapper<String>> invite(
            @PathVariable("board_id") Long workspaceId,
            @RequestBody BoardInviteDto body) {
        return boardServices.invite(workspaceId, body.getInvitedBy(), body.getInvitedUser(), body.getRole());
    }

//    private
    @PostMapping("/decideInvite")
    public ResponseEntity<ResponseWrapper<String>> decideInvite(
            @RequestBody DecideInvitationDto body
    ) {
        return boardServices.decideInvite(body.getBoardId(), body.getInvitedUser(), body.getDecision());
    }

    //public
    @PostMapping("/{board_id}/addTag")
    public ResponseEntity<ResponseWrapper<String>> addTag(
            @PathVariable("board_id") Long boardId,
            @RequestBody AddTagDto body
    ) {
        return boardServices.addTag(boardId, body.getTitle(), body.getCreatedBy());
    }

    //private
    @PostMapping("/addMemberToBoardFromWorkspace")
    public ResponseEntity<ResponseWrapper<Boolean>> addMemberToBoardFromWorkspace(
        @RequestBody AddMemberFromWorkspaceDto body
    ){
        return boardServices.addMemberToBoardFromWorkspace(body.getWorkspaceId(), body.getUserId(), body.getRole());
    }

    //private
    @PostMapping("/updateMemberStatusFromWorkspace")
    public ResponseEntity<ResponseWrapper<Boolean>> updateMemberStatusFromWorkspace(
            @RequestBody UpdateMemberStatusFromWorkspace body
    ){
        return boardServices.updateMemberStatusFromWorkspace(
                body.getWorkspaceId(),
                body.getUserId(),
                body.getRole(),
                body.getStatus()
        );
    }

//  public
    @PostMapping("/removeUser")
    public ResponseEntity<ResponseWrapper<String>> removeUser(
            @RequestBody RemoveUserDto body
    ){
        return boardServices.removeUser(body.getBoardId(), body.getUserId(), body.getRemoverId());
    }

//    private
    @PostMapping("/removeUserFromWorkspace/{workspaceId}/{userId}")
    public ResponseEntity<ResponseWrapper<String>> removeUserFromWorkspace(
            @PathVariable("workspaceId") Long workspaceId,
            @PathVariable("userId") Long userId
    ){
        return boardServices.removeUserFromWorkspace(workspaceId, userId);
    }

    //private
    @PostMapping("/deleteUserAccount/{userId}")
    public ResponseEntity<ResponseWrapper<String>> deleteUserAccount(
            @PathVariable("userId") Long userId
    ){
        return boardServices.deleteUserAccount(userId);
    }

    //public
    @DeleteMapping("/deleteBoard/{boardId}")
    public ResponseEntity<ResponseWrapper<String>> deleteBoard(
        @PathVariable("boardId") Long boardId,
        @RequestParam Long userId
    ){
        return boardServices.deleteBoard(boardId, userId);
    }

//    private
    @DeleteMapping("/deleteBoardFromWorkspace/{workspaceId}")
    public ResponseEntity<ResponseWrapper<String>> deleteBoardFromWorkspace(
            @PathVariable("workspaceId") Long workspaceId
    ){
        return boardServices.deleteBoardFromWorkspace(workspaceId);
    }
}
