package com.boardify.profile_service.service;

import com.boardify.profile_service.dto.ResponseWrapper;
import com.boardify.profile_service.dto.request.BoardInvitationDecisionDto;
import com.boardify.profile_service.dto.request.WorkspaceInvitationDecisionDto;
import com.boardify.profile_service.dto.response.UserDetailsDto;
import com.boardify.profile_service.dto.response.WorkspaceAndBoardByUserDto;
import com.boardify.profile_service.dto.service.BoardInvitationDto;
import com.boardify.profile_service.dto.service.BoardTitleDto;
import com.boardify.profile_service.dto.service.WorkspaceInvitationDto;
import com.boardify.profile_service.dto.service.WorkspaceTitleDto;
import com.boardify.profile_service.feign.BoardInterface;
import com.boardify.profile_service.feign.WorkspaceInterface;
import com.boardify.profile_service.model.UserModel;
import com.boardify.profile_service.repository.UserRepository;
import com.boardify.profile_service.utills.InvitationDecisionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardInterface boardInterface;
    @Autowired
    WorkspaceInterface workspaceInterface;

    public ResponseEntity<ResponseWrapper<UserDetailsDto>> getUserDetails(Long userId) {
        try {
            Optional<UserModel> userModel = userRepository.findById(userId);

            if (userModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));
            if (userModel.get().getDeletedAt() != null)
                return ResponseEntity.ok(ResponseWrapper.failure("Deleted User."));

            return ResponseEntity.ok(ResponseWrapper.success(userModel.get().toDetailsDto()));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }


    public ResponseEntity<ResponseWrapper<WorkspaceAndBoardByUserDto>> getWorkspacesAndBoards(Long userId) {
        try {
            Optional<UserModel> userModel = userRepository.findById(userId);

            if (userModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));
            if (userModel.get().getDeletedAt() != null)
                return ResponseEntity.ok(ResponseWrapper.failure("Deleted User."));

            ResponseWrapper<List<WorkspaceTitleDto>> workspaces = workspaceInterface.getWorkspaceTitleByUser(userId);
            if (!workspaces.isSuccess())
                return ResponseEntity.ok(ResponseWrapper.failure(workspaces.getError()));

            ResponseWrapper<List<BoardTitleDto>> boards = boardInterface.getBoardTitleByUser(userId);
            if (!boards.isSuccess())
                return ResponseEntity.ok(ResponseWrapper.failure(boards.getError()));

            WorkspaceAndBoardByUserDto res = new WorkspaceAndBoardByUserDto(workspaces.getData(), boards.getData());

            return ResponseEntity.ok(ResponseWrapper.success(res));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<List<WorkspaceInvitationDto>>> getWorkspaceInvitations(Long userId) {
        try {
            Boolean isValidUser = userRepository.isUserValid(userId);
            if (!isValidUser)
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            ResponseWrapper<List<WorkspaceInvitationDto>> invitations = workspaceInterface.getInvitationsByUser(userId);

            return ResponseEntity.ok(invitations);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<List<BoardInvitationDto>>> getBoardInvitations(Long userId) {
        try {
            Boolean isValidUser = userRepository.isUserValid(userId);
            if (!isValidUser)
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            ResponseWrapper<List<BoardInvitationDto>> invitations = boardInterface.getInvitationsByUser(userId);

            return ResponseEntity.ok(invitations);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<List<WorkspaceTitleDto>>> getWorkspaceTitles(Long userId) {
        try {
            Optional<UserModel> userModel = userRepository.findById(userId);

            if (userModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));
            if (userModel.get().getDeletedAt() != null)
                return ResponseEntity.ok(ResponseWrapper.failure("Deleted User."));

            ResponseWrapper<List<WorkspaceTitleDto>> workspaces = workspaceInterface.getWorkspaceTitleByUser(userId);
            if (!workspaces.isSuccess())
                return ResponseEntity.ok(ResponseWrapper.failure(workspaces.getError()));

            return ResponseEntity.ok(ResponseWrapper.success(workspaces.getData()));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<List<BoardTitleDto>>> getBoardsTitle(Long userId) {
        try {
            Optional<UserModel> userModel = userRepository.findById(userId);

            if (userModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));
            if (userModel.get().getDeletedAt() != null)
                return ResponseEntity.ok(ResponseWrapper.failure("Deleted User."));

            ResponseWrapper<List<BoardTitleDto>> boards = boardInterface.getBoardTitleByUser(userId);
            if (!boards.isSuccess())
                return ResponseEntity.ok(ResponseWrapper.failure(boards.getError()));

            return ResponseEntity.ok(ResponseWrapper.success(boards.getData()));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<Boolean>> isUserValid(Long userId) {
        try {
            Boolean isValidOptional = userRepository.isUserValid(userId);
            return ResponseEntity.ok(ResponseWrapper.success(isValidOptional));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(ResponseWrapper.failure("Internal Server Error."));
        }
    }


    public ResponseEntity<ResponseWrapper<UserModel>> createAccount(String firstName, String middleName, String lastName, String email) {
        try {
            Optional<UserModel> userOptional = userRepository.findByEmailNotDeleted(email);

            if (userOptional.isPresent())
                return ResponseEntity.ok(ResponseWrapper.failure("User Already exist."));

            UserModel user = userRepository.save(
                    new UserModel(
                            email,
                            firstName,
                            middleName,
                            lastName
                    )
            );

            return ResponseEntity.ok(ResponseWrapper.success(user));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

    public ResponseEntity<ResponseWrapper<String>> decideWorkspaceInvitation(WorkspaceInvitationDecisionDto body) {
        try {
            Boolean isValid = userRepository.isUserValid(body.getInvitedUser());
            if (!isValid)
                return ResponseEntity.ok(ResponseWrapper.success("Invalid User."));

            ResponseWrapper<String> res = workspaceInterface.decideInvite(body);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

    public ResponseEntity<ResponseWrapper<String>> decideBoardInvitation(BoardInvitationDecisionDto body) {
        try {
            Boolean isValid = userRepository.isUserValid(body.getInvitedUser());
            if (!isValid)
                return ResponseEntity.ok(ResponseWrapper.success("Invalid User."));

            ResponseWrapper<String> res = boardInterface.decideInvite(body);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

    public ResponseEntity<ResponseWrapper<String>> deleteAccount(Long userId) {
        try {
            Boolean isValid = userRepository.isUserValid(userId);
            if (!isValid)
                return ResponseEntity.ok(ResponseWrapper.success("Invalid User."));

            ResponseWrapper<String> boardRes = boardInterface.deleteUserAccount(userId);
            if (!boardRes.isSuccess())
                return ResponseEntity.ok(boardRes);

            ResponseWrapper<String> workspaceRes = workspaceInterface.deleteUser(userId);
            if (!workspaceRes.isSuccess())
                return ResponseEntity.ok(workspaceRes);

            userRepository.deleteUser(userId, LocalDateTime.now());

            return ResponseEntity.ok(ResponseWrapper.success("Account Deleted."));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(ResponseWrapper.failure("Internal Server Error."));
        }
    }
}
