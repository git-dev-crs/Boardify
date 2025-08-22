package com.boardify.board_service.service;


import com.boardify.board_service.feign.ListInterface;
import com.boardify.board_service.feign.ProfileInterface;
import com.boardify.board_service.feign.WorkspaceInterface;
import com.boardify.board_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeletionHelper {
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardMemberRepository boardMemberRepository;
    @Autowired
    BoardTagRepository boardTagRepository;
    @Autowired
    InvitationRepository invitationRepository;
    @Autowired
    WorkspaceBoardRepository workspaceBoardRepository;

    void deleteBoardData(Long boardId){
        boardMemberRepository.deleteByBoardId(boardId);
        boardTagRepository.deleteByBoardId(boardId);
        invitationRepository.deleteByBoardId(boardId);
        workspaceBoardRepository.deleteByBoardId(boardId);
        boardRepository.deleteById(boardId);
    }
}
