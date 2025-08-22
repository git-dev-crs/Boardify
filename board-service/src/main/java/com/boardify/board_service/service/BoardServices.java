package com.boardify.board_service.service;

import com.boardify.board_service.dto.ResponseWrapper;
import com.boardify.board_service.dto.request.CreateBoardFromWorkspaceDto;
import com.boardify.board_service.dto.request.CreateBoardDto;
import com.boardify.board_service.dto.response.*;
import com.boardify.board_service.dto.service.response.ListWithCardDto;
import com.boardify.board_service.dto.service.response.WorkspaceMemberRoleDto;
import com.boardify.board_service.feign.ListInterface;
import com.boardify.board_service.feign.ProfileInterface;
import com.boardify.board_service.feign.WorkspaceInterface;
import com.boardify.board_service.model.BoardModel;
import com.boardify.board_service.model.RoleEnum;
import com.boardify.board_service.model.StatusEnum;
import com.boardify.board_service.model.invitation.InvitationDecisionEnum;
import com.boardify.board_service.model.invitation.InvitationModel;
import com.boardify.board_service.model.invitation.InvitationModelId;
import com.boardify.board_service.model.member.BoardMemberId;
import com.boardify.board_service.model.member.BoardMemberModel;
import com.boardify.board_service.model.tag.BoardTagId;
import com.boardify.board_service.model.tag.BoardTagModel;
import com.boardify.board_service.model.workspaceboard.WorkspaceBoardId;
import com.boardify.board_service.model.workspaceboard.WorkspaceBoardModel;
import com.boardify.board_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BoardServices {
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

    @Autowired
    ProfileInterface profileInterface;
    @Autowired
    WorkspaceInterface workspaceInterface;
    @Autowired
    ListInterface listInterface;

    @Autowired
    DeletionHelper deletionHelper;

    public ResponseEntity<ResponseWrapper<BoardDto>> getBoard(Long boardId, Long userId) {
        try {
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(userId);
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            Optional<BoardModel> boardModelOptional = boardRepository.findById(boardId);
            if (boardModelOptional.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist"));
            BoardModel boardModel = boardModelOptional.get();
            Optional<BoardMemberModel> userModel = boardModel.getMembers().stream()
                    .filter(member -> Objects.equals(member.getId().getUserId(), userId))
                    .findFirst();
            if (userModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("No access to board"));

            BoardDto boardDto = boardModel.toBoardDto();
            return ResponseEntity.ok(ResponseWrapper.success(boardDto));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<BoardWithListAndCardDto>> getBoardWithListAndCard(Long boardId, Long userId) {
        try {
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(userId);
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            Optional<BoardModel> boardModelOptional = boardRepository.findById(boardId);
            if (boardModelOptional.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist"));

            BoardModel boardModel = boardModelOptional.get();
            Optional<BoardMemberModel> userModel = boardModel.getMembers().stream()
                    .filter(member -> Objects.equals(member.getId().getUserId(), userId))
                    .findFirst();
            if (userModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("No access to board"));

            BoardWithListAndCardDto boardWithListAndCardDto = boardModel.toBoardWithListAndCardDto();
            ResponseWrapper<List<ListWithCardDto>> listsWrapper = listInterface.getListAndCardByBoardId(boardId);
            if (!listsWrapper.isSuccess())
                return ResponseEntity.ok(ResponseWrapper.failure(listsWrapper.getError()));

            boardWithListAndCardDto.setLists(listsWrapper.getData());
            return ResponseEntity.ok(ResponseWrapper.success(boardWithListAndCardDto));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<List<BoardTitleDto>>> getBoardTitleInWorkspace(Long workspaceId) {
        try {
            List<BoardTitleDto> boards = boardRepository.getBoardTitlesFromWorkspaceId(workspaceId);
            return ResponseEntity.ok(ResponseWrapper.success(boards));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }


    public ResponseEntity<ResponseWrapper<List<BoardTitleDto>>> getBoardTitleByUser(Long userId) {
        try {
            List<BoardTitleDto> boards = boardRepository.getBoardTitlesByUser(userId);
            List<BoardTitleDto> res = new ArrayList<>();
            for (BoardTitleDto board : boards) {
                if (!workspaceBoardRepository.isBoardInWorkspace(board.getBoardId()))
                    res.add(board);
            }
            return ResponseEntity.ok(ResponseWrapper.success(res));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<BoardDto>> createBoard(CreateBoardDto body) {
        try {
            String trimmedTitle = body.getTitle().trim();
            if (trimmedTitle.isBlank())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid title"));

            //check if user is valid.
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(body.getCreatedBy());
            //If there is any other response than 200, then automatically feign will throw FeignException
            // which will lead to catch block and respond ISE, And in all other cases it will return positive value.
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            BoardModel boardModel = boardRepository.save(
                    new BoardModel(
                            trimmedTitle,
                            body.getDescription(),
                            body.getCreatedBy()
                    )
            );
            BoardMemberModel adminMemberModel = boardMemberRepository.save(
                    new BoardMemberModel(
                            new BoardMemberId(boardModel.getId(), body.getCreatedBy()),
                            RoleEnum.ADMIN
                    )
            );

            List<BoardMemberModel> members = new ArrayList<>();
            members.add(adminMemberModel);
            boardModel.setMembers(members);

            return ResponseEntity.ok(new ResponseWrapper<>(boardModel.toBoardDto()));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(new ResponseWrapper<>("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<BoardDto>> createBoardFromWorkspace(CreateBoardFromWorkspaceDto body) {
        try {

            String trimmedTitle = body.getTitle().trim();
            if (trimmedTitle.isBlank())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid title"));

            //check if user is valid.
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(body.getCreatedBy());
            //If there is any other response than 200, then automatically feign will throw FeignException
            // which will lead to catch block and respond ISE, And in all other cases it will return positive value.
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            //is workspace valid.
            //is user is member of workspace.
            ResponseWrapper<Boolean> res = workspaceInterface
                    .isWorkspaceAndMemberValid(body.getWorkspaceId(), body.getCreatedBy());

            if (!res.isSuccess())
                switch (res.getError()) {
                    case "INVALID_WORKSPACE" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Workspace Does Not Exist"));
                    }
                    case "INVALID_USER" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User does not have the access to workspace"));
                    }
                }


            //get members of workspace to add to board.
            ResponseWrapper<List<WorkspaceMemberRoleDto>> workspaceMembers =
                    workspaceInterface.getWorkspaceMembersIdRole(body.getWorkspaceId(), body.getCreatedBy());
//            if (workspaceMembersEntity.getStatusCode() != HttpStatusCode.valueOf(200))
//                throw new Exception("Exception in calling workspaceMembersEntity method to workspace-service");

            BoardModel boardModel = boardRepository.save(
                    new BoardModel(
                            trimmedTitle,
                            body.getDescription(),
                            body.getCreatedBy()
                    )
            );

            workspaceBoardRepository.save(
                    new WorkspaceBoardModel(
                            new WorkspaceBoardId(body.getWorkspaceId(), boardModel.getId()),
                            body.getCreatedBy()
                    )
            );

            List<BoardMemberDto> membersList = new ArrayList<>();
            BoardMemberModel adminMemberModel = boardMemberRepository.save(
                    new BoardMemberModel(
                            new BoardMemberId(boardModel.getId(), body.getCreatedBy()),
                            RoleEnum.ADMIN
                    )
            );
            membersList.add(adminMemberModel.toBoardMemberDto());
            for (WorkspaceMemberRoleDto member : workspaceMembers.getData()) {
                if (Objects.equals(member.getUserId(), body.getCreatedBy())) continue;
                BoardMemberModel memberModel = boardMemberRepository.save(
                        new BoardMemberModel(
                                new BoardMemberId(boardModel.getId(), member.getUserId()),
                                member.getRole()
                        )
                );
                membersList.add(memberModel.toBoardMemberDto());
            }

            BoardDto boardDto = boardRepository.findById(boardModel.getId()).get().toBoardDto();
            boardDto.setMembers(membersList);
            return ResponseEntity.ok(new ResponseWrapper<>(boardDto));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(new ResponseWrapper<>("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> invite(Long boardId, Long invitedBy, Long invitedUser, RoleEnum role) {
        try {
            //check if inviting user is valid.
            ResponseWrapper<Boolean> isUserValid1 = profileInterface.isUserValid(invitedBy);
            if (isUserValid1.isSuccess() && !isUserValid1.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Inviting User does not exist."));
            //check if invited user is valid.
            ResponseWrapper<Boolean> isUserValid2 = profileInterface.isUserValid(invitedUser);
            if (isUserValid2.isSuccess() && !isUserValid2.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invited User does not exist."));

            Optional<BoardModel> boardModel = boardRepository.findById(boardId);
            if (boardModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist"));

            Boolean isBoardInWorkspace = workspaceBoardRepository.isBoardInWorkspace(boardId);
            if (isBoardInWorkspace)
                return ResponseEntity.ok(ResponseWrapper.failure("Can not add to board because this board is in a workspace."));

            Optional<BoardMemberModel> boardMember = boardModel.get().getMembers()
                    .stream()
                    .filter(invited -> invited.getId().equals(new BoardMemberId(boardId, invitedUser)))
                    .findFirst();
            if (boardMember.isPresent()) {
                return ResponseEntity.ok(ResponseWrapper.failure("User already member of board."));
            }

            Optional<InvitationModel> invitedUserModel = boardModel.get().getInvitations()
                    .stream()
                    .filter(invited -> invited.getId().equals(new InvitationModelId(boardId, invitedUser)))
                    .findFirst();
            if (invitedUserModel.isPresent()) {
                switch (invitedUserModel.get().getDecision()) {
                    case UNDEF -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Invitation already sent to user."));
                    }
                    case ACCEPT -> {
                        Optional<BoardMemberModel> memberModel = boardMemberRepository.findById(new BoardMemberId(boardId, invitedUser));
                        if (memberModel.isEmpty())
                            return ResponseEntity.ok(ResponseWrapper.failure("Unknown semantic error."));

                        if (memberModel.get().getStatus() == StatusEnum.ACTIVE)
                            return ResponseEntity.ok(ResponseWrapper.failure("User already member of board."));
                    }
                    case REJECT -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User has already rejected the invitation."));
                    }
                }
            }

            Optional<BoardMemberModel> adminMember = boardModel.get().getMembers()
                    .stream()
                    .filter(invited -> invited.getId().equals(new BoardMemberId(boardId, invitedBy)))
                    .findFirst();
            if (adminMember.isPresent()) {
                if (adminMember.get().getRole() == RoleEnum.ADMIN) {
                    InvitationModel invitationModel = invitationRepository.save(
                            new InvitationModel(
                                    new InvitationModelId(boardId, invitedUser),
                                    invitedBy,
                                    role,
                                    InvitationDecisionEnum.UNDEF
                            )
                    );

                    return ResponseEntity.ok(ResponseWrapper.success("Invitation sent successfully"));
                } else {
                    return ResponseEntity.ok(ResponseWrapper.failure("Member Not authorized to invite users."));
                }
            } else {
                return ResponseEntity.ok(ResponseWrapper.failure("Inviting User is not a member of board."));
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(new ResponseWrapper<>("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> decideInvite(Long boardId, Long invitedUser, InvitationDecisionEnum decision) {
        try {
            Optional<BoardModel> workspaceModel = boardRepository.findById(boardId);
            if (workspaceModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist"));

            Optional<InvitationModel> invitedUserModel = workspaceModel.get().getInvitations()
                    .stream()
                    .filter(invited -> invited.getId().equals(new InvitationModelId(boardId, invitedUser)))
                    .findFirst();
            if (invitedUserModel.isPresent()) {
                switch (invitedUserModel.get().getDecision()) {
                    case UNDEF -> {
                        invitationRepository.updateInvitationDecision(decision, LocalDateTime.now(), new InvitationModelId(boardId, invitedUser));
                        if (decision == InvitationDecisionEnum.ACCEPT) {
                            boardMemberRepository.save(
                                    new BoardMemberModel(
                                            new BoardMemberId(boardId, invitedUser),
                                            invitedUserModel.get().getRole()
                                    )
                            );

                            return ResponseEntity.ok(ResponseWrapper.success("Updated the response, User is now a member of board"));
                        }
                        return ResponseEntity.ok(ResponseWrapper.success("Updated the response"));
                    }
                    case ACCEPT -> {
                        Optional<BoardMemberModel> memberModel = boardMemberRepository.findById(new BoardMemberId(boardId, invitedUser));
                        if (memberModel.isEmpty())
                            return ResponseEntity.ok(ResponseWrapper.failure("Unknown semantic error."));

                        if (memberModel.get().getStatus() == StatusEnum.ACTIVE)
                            return ResponseEntity.ok(ResponseWrapper.failure("User already member of board."));

                        boardMemberRepository.updateRoleAndStatus(
                                boardId,
                                invitedUser,
                                invitedUserModel.get().getRole(),
                                StatusEnum.ACTIVE,
                                LocalDateTime.now()
                        );

                        return ResponseEntity.ok(ResponseWrapper.success("Updated the response, User is now a member of board"));
                    }
                    case REJECT -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User has rejected the invitation."));
                    }
                }
            } else {
                return ResponseEntity.ok(ResponseWrapper.failure("User is not invited to board."));
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(new ResponseWrapper<>("Internal Server Error"));
        }
        return null;
    }

    public ResponseEntity<ResponseWrapper<String>> addTag(Long boardId, String title, Long createdBy) {
        try {
            //Check user id
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(createdBy);
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            String trimmedTitle = title.trim();
            if (trimmedTitle.isBlank())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid Tag title"));

            Optional<BoardModel> boardOptional = boardRepository.findById(boardId);
            if (boardOptional.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist"));

            BoardModel board = boardOptional.get();
            Optional<BoardMemberModel> memberModel = board.getMembers().stream()
                    .filter(member -> member.getId().equals(new BoardMemberId(boardId, createdBy)))
                    .findFirst();
            if (memberModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("User is not a member of board."));

            Optional<BoardTagModel> tagModel = board.getTags().stream()
                    .filter(member -> member.getId().equals(new BoardTagId(boardId, trimmedTitle)))
                    .findFirst();
            if (tagModel.isPresent())
                return ResponseEntity.ok(ResponseWrapper.failure("Tag already exist in board."));

            boardTagRepository.save(
                    new BoardTagModel(
                            new BoardTagId(boardId, trimmedTitle),
                            createdBy
                    )
            );
            return ResponseEntity.ok(ResponseWrapper.success("Tag added to the board"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(new ResponseWrapper<>("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<Boolean>> isBoardAndMemberValid(Long boardId, Long userId) {
        try {
            Optional<BoardModel> workspaceModel = boardRepository.findById(boardId);
            if (workspaceModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("INVALID_BOARD"));

            Boolean isUserMember = workspaceModel.get().getMembers().stream()
                    .anyMatch(member -> member.getId().equals(new BoardMemberId(boardId, userId)));
            if (!isUserMember) return ResponseEntity.ok(ResponseWrapper.failure("INVALID_USER"));

            return ResponseEntity.ok(new ResponseWrapper<>(true));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<Boolean>> isBoardAndTagValid(Long boardId, String tag) {
        try {
            Optional<BoardModel> boardOptional = boardRepository.findById(boardId);
            if (boardOptional.isEmpty())
                return ResponseEntity.status(404).body(ResponseWrapper.failure("INVALID_BOARD"));

            Optional<BoardTagModel> tagModelOptional = boardOptional.get().getTags().stream()
                    .filter(member -> member.getId().equals(new BoardTagId(boardId, tag)))
                    .findFirst();

            if (tagModelOptional.isPresent())
                return ResponseEntity.ok(ResponseWrapper.success(true));
            else
                return ResponseEntity.ok(ResponseWrapper.failure("INVALID_TAG"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<List<InvitationDto>>> getInvitationsByUser(Long userId) {
        try {
            List<InvitationModel> invitations = invitationRepository.getInvitationsByUser(userId);
            List<InvitationDto> res = invitations.stream()
                    .map(InvitationModel::toInvitationDto)
                    .toList();

            return ResponseEntity.ok(ResponseWrapper.success(res));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<Boolean>> addMemberToBoardFromWorkspace(Long workspaceId, Long userId, RoleEnum role) {
        try {
            List<Long> boardIds = workspaceBoardRepository.getBoardIdsOfWorkspace(workspaceId);

            for (Long id : boardIds) {
                boardMemberRepository.save(
                        new BoardMemberModel(
                                new BoardMemberId(id, userId),
                                role
                        )
                );

            }

            return ResponseEntity.ok(ResponseWrapper.success(true));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<Boolean>> updateMemberStatusFromWorkspace(Long workspaceId, Long userId, RoleEnum role, StatusEnum status) {
        try {
            List<Long> boardIds = workspaceBoardRepository.getBoardIdsOfWorkspace(workspaceId);

            for (Long boardId : boardIds) {
                boardMemberRepository.updateRoleAndStatus(
                        boardId,
                        userId,
                        role,
                        status,
                        LocalDateTime.now()
                );
            }

            return ResponseEntity.ok(ResponseWrapper.success(true));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(ResponseWrapper.failure("Internal Server Error."));
        }
    }


    public ResponseEntity<ResponseWrapper<String>> deleteUserAccount(Long userId) {
        try {
            List<Long> boardIds = boardMemberRepository.getBoardIdsByUser(userId);
            for (Long boardId : boardIds) {
                boardMemberRepository.updateStatus(boardId, userId, StatusEnum.DELETED, LocalDateTime.now());

                Long activeMembers = boardMemberRepository.getActiveCount(boardId, StatusEnum.ACTIVE);
                if (activeMembers == 0) {
                    ResponseWrapper<String> res = listInterface.deleteListsFromBoard(boardId);
                    if (!res.isSuccess())
                        return ResponseEntity.ok(res);

                    deletionHelper.deleteBoardData(boardId);
                }
            }

            return ResponseEntity.ok(ResponseWrapper.success("DATA_DELETED"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> deleteBoard(Long boardId, Long userId) {
        try {
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(userId);
            if (!isUserValid.isSuccess() || !isUserValid.getData())
                ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            boolean doesBoardExist = boardRepository.existsById(boardId);
            if (!doesBoardExist)
                return ResponseEntity.ok(ResponseWrapper.failure("Board Does not Exist."));

            Optional<BoardMemberModel> memberModel = boardMemberRepository.findById(new BoardMemberId(boardId, userId));
            if (memberModel.isEmpty() || memberModel.get().getStatus() != StatusEnum.ACTIVE)
                return ResponseEntity.ok(ResponseWrapper.failure("User is not part of board."));

            if (memberModel.get().getRole() != RoleEnum.ADMIN)
                return ResponseEntity.ok(ResponseWrapper.failure("User is not authorized to delete the board."));


            ResponseWrapper<String> res = listInterface.deleteListsFromBoard(boardId);
            if (!res.isSuccess())
                return ResponseEntity.ok(res);

            deletionHelper.deleteBoardData(boardId);

            return ResponseEntity.ok(ResponseWrapper.success("Board deleted."));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> deleteBoardFromWorkspace(Long workspaceId) {
        try {
            List<Long> boardIds = workspaceBoardRepository.getBoardIdsOfWorkspace(workspaceId);

            for (Long boardId : boardIds) {
                ResponseWrapper<String> res = listInterface.deleteListsFromBoard(boardId);
                if (!res.isSuccess())
                    return ResponseEntity.ok(res);

                deletionHelper.deleteBoardData(boardId);
            }

            return ResponseEntity.ok(ResponseWrapper.success("Board deleted."));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> removeUserFromWorkspace(Long workspaceId, Long userId) {
        try {
            List<Long> boardIds = workspaceBoardRepository.getBoardIdsOfWorkspace(workspaceId);

            boardMemberRepository.updateStatusBulk(boardIds, userId, StatusEnum.REMOVED, LocalDateTime.now());

            return ResponseEntity.ok(ResponseWrapper.success("User Removed."));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> removeUser(Long boardId, Long userId, Long removerId) {
        try {
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(userId);
            if (!isUserValid.isSuccess() || !isUserValid.getData())
                ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            ResponseWrapper<Boolean> isRemoverValid = profileInterface.isUserValid(removerId);
            if (!isRemoverValid.isSuccess() || !isRemoverValid.getData())
                ResponseEntity.ok(ResponseWrapper.failure("Invalid Remover."));

            boolean doesBoardExist = boardRepository.existsById(boardId);
            if (!doesBoardExist)
                return ResponseEntity.ok(ResponseWrapper.failure("Board Does not Exist."));

            Boolean isBoardInWorkspace = workspaceBoardRepository.isBoardInWorkspace(boardId);
            if (isBoardInWorkspace)
                return ResponseEntity.ok(ResponseWrapper.failure("Can't remove from board as user is member of workspace."));

            Optional<BoardMemberModel> memberModel = boardMemberRepository.findById(new BoardMemberId(boardId, userId));
            if (memberModel.isEmpty() || memberModel.get().getStatus() != StatusEnum.ACTIVE)
                return ResponseEntity.ok(ResponseWrapper.failure("User is not part of Board."));

            Optional<BoardMemberModel> removerModel = boardMemberRepository.findById(new BoardMemberId(boardId, removerId));
            if (removerModel.isEmpty() || removerModel.get().getStatus() != StatusEnum.ACTIVE)
                return ResponseEntity.ok(ResponseWrapper.failure("Remover is not part of Board."));

            if (removerModel.get().getRole() != RoleEnum.ADMIN)
                return ResponseEntity.ok(ResponseWrapper.failure("User is not authorized to remove user."));


            boardMemberRepository.updateStatus(
                    boardId,
                    userId,
                    StatusEnum.REMOVED,
                    LocalDateTime.now()
            );

            return ResponseEntity.ok(ResponseWrapper.success("User Removed."));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(ResponseWrapper.failure("Internal Server Error."));
        }
    }
}
