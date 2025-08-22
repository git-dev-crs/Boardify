package com.boardify.workspace_service.service;

import com.boardify.workspace_service.dto.request.CreateWorkspaceDto;
import com.boardify.workspace_service.dto.ResponseWrapper;
import com.boardify.workspace_service.dto.response.*;
import com.boardify.workspace_service.dto.service.AddMemberFromWorkspaceDto;
import com.boardify.workspace_service.dto.service.BoardTitleDto;
import com.boardify.workspace_service.dto.service.UpdateMemberStatusFromWorkspace;
import com.boardify.workspace_service.feign.BoardInterface;
import com.boardify.workspace_service.feign.ProfileInterface;
import com.boardify.workspace_service.model.RoleEnum;
import com.boardify.workspace_service.model.StatusEnum;
import com.boardify.workspace_service.model.WorkspaceModel;
import com.boardify.workspace_service.model.invitation.InvitationDecisionEnum;
import com.boardify.workspace_service.model.invitation.InvitationModel;
import com.boardify.workspace_service.model.invitation.InvitationModelId;
import com.boardify.workspace_service.model.member.WorkspaceMemberId;
import com.boardify.workspace_service.model.member.WorkspaceMemberModel;
import com.boardify.workspace_service.repository.InvitationRepository;
import com.boardify.workspace_service.repository.WorkspaceMemberRepository;
import com.boardify.workspace_service.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WorkspaceService {
    @Autowired
    WorkspaceRepository workspaceRepository;
    @Autowired
    WorkspaceMemberRepository workspaceMemberRepository;
    @Autowired
    InvitationRepository invitationRepository;

    @Autowired
    ProfileInterface profileInterface;
    @Autowired
    BoardInterface boardInterface;

    @Autowired
    DeletionHelper deletionHelper;

    public ResponseEntity<ResponseWrapper<WorkspaceWithBoardDto>> getWorkspaceWithBoards(Long workspaceId, Long userId) {
        try {
            //check if user is valid.
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(userId);
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            Optional<WorkspaceModel> workspaceModelOptional = workspaceRepository.findById(workspaceId);
            if (workspaceModelOptional.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Workspace Does Not Exist"));

            WorkspaceModel workspaceModel = workspaceModelOptional.get();
            Optional<WorkspaceMemberModel> userModel = workspaceModel.getMembers().stream()
                    .filter(member -> Objects.equals(member.getId().getUserId(), userId))
                    .findFirst();
            if (userModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("No access to workspace"));

            WorkspaceWithBoardDto workspaceWithBoardDto = workspaceModel.toWorkspaceWithBoardDto();
            ResponseWrapper<List<BoardTitleDto>> boardsWrapper = boardInterface.getBoardTitleInWorkspace(workspaceId);
            if (!boardsWrapper.isSuccess())
                return ResponseEntity.ok(ResponseWrapper.failure(boardsWrapper.getError()));

            workspaceWithBoardDto.setBoards(boardsWrapper.getData());
            return ResponseEntity.ok(ResponseWrapper.success(workspaceWithBoardDto));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<List<WorkspaceTitleDto>>> getWorkspaceTitleByUser(Long userId) {
        try {
            List<Long> workspaceIds = workspaceMemberRepository.getWorkspaceIdsByUserId(userId);
            List<WorkspaceTitleDto> list = new ArrayList<>();
            for (Long workspaceId : workspaceIds) {
                WorkspaceTitleDto dto = workspaceRepository.getWorkspaceTitleDto(workspaceId);
                list.add(dto);
            }

            return ResponseEntity.ok(ResponseWrapper.success(list));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<WorkspaceModel>> createWorkspace(CreateWorkspaceDto workspaceDto) {
        try {
            String trimmedTitle = workspaceDto.getTitle().trim();
            if (trimmedTitle.isBlank())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid title"));

            //check if user is valid.
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(workspaceDto.getCreatedBy());
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            WorkspaceModel workspaceModel = workspaceRepository.save(
                    new WorkspaceModel(
                            trimmedTitle,
                            workspaceDto.getDescription(),
                            workspaceDto.getCreatedBy()
                    )
            );
            WorkspaceMemberModel adminMember = workspaceMemberRepository.save(
                    new WorkspaceMemberModel(
                            new WorkspaceMemberId(workspaceModel.getId(), workspaceDto.getCreatedBy()),
                            RoleEnum.ADMIN
                    )
            );
            List<WorkspaceMemberModel> members = new ArrayList<>();
            members.add(adminMember);
            workspaceModel.setMembers(members);

            return ResponseEntity.ok(new ResponseWrapper<>(workspaceModel));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(new ResponseWrapper<>("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<WorkspaceDto>> getWorkspace(Long workspaceId, Long userId) {
        try {
            //check if user is valid.
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(userId);
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            Optional<WorkspaceModel> workspaceModelOptional = workspaceRepository.findById(workspaceId);
            if (workspaceModelOptional.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Workspace Does Not Exist"));
            WorkspaceModel workspaceModel = workspaceModelOptional.get();
            Optional<WorkspaceMemberModel> userModel = workspaceModel.getMembers().stream()
                    .filter(member -> Objects.equals(member.getId().getUserId(), userId))
                    .findFirst();
            if (userModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("No access to workspace"));

            WorkspaceDto workspaceDto = workspaceModel.toWorkspaceDto();
            return ResponseEntity.ok(ResponseWrapper.success(workspaceDto));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> invite(Long workspaceId, Long invitedBy, Long invitedUser, RoleEnum role) {
        try {
            //check if inviting user is valid.
            ResponseWrapper<Boolean> isUserValid1 = profileInterface.isUserValid(invitedBy);
            if (isUserValid1.isSuccess() && !isUserValid1.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Inviting User does not exist."));
            //check if invited user is valid.
            ResponseWrapper<Boolean> isUserValid2 = profileInterface.isUserValid(invitedUser);
            if (isUserValid2.isSuccess() && !isUserValid2.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invited User does not exist."));

            Optional<WorkspaceModel> workspaceModel = workspaceRepository.findById(workspaceId);
            if (workspaceModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Workspace Does Not Exist"));

            Optional<InvitationModel> invitedUserModel = workspaceModel.get().getInvited()
                    .stream()
                    .filter(invited -> invited.getId().equals(new InvitationModelId(workspaceId, invitedUser)))
                    .findFirst();
            if (invitedUserModel.isPresent()) {
                switch (invitedUserModel.get().getDecision()) {
                    case UNDEF -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Invitation already sent to user."));
                    }
                    case ACCEPT -> {
                        Optional<WorkspaceMemberModel> memberModel = workspaceMemberRepository.findById(new WorkspaceMemberId(workspaceId, invitedUser));
                        if (memberModel.isEmpty())
                            return ResponseEntity.ok(ResponseWrapper.failure("Unknown semantic error."));

                        if (memberModel.get().getStatus() == StatusEnum.ACTIVE)
                            return ResponseEntity.ok(ResponseWrapper.failure("User already member of workspace."));
                    }
                    case REJECT -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User has already rejected the invitation."));
                    }
                }
            }

            Optional<WorkspaceMemberModel> adminMember = workspaceModel.get().getMembers()
                    .stream()
                    .filter(invited -> invited.getId().equals(new WorkspaceMemberId(workspaceId, invitedBy)))
                    .findFirst();
            if (adminMember.isPresent()) {
                if (adminMember.get().getRole() == RoleEnum.ADMIN) {
                    InvitationModel invitationModel = invitationRepository.save(
                            new InvitationModel(
                                    new InvitationModelId(workspaceId, invitedUser),
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
                return ResponseEntity.ok(ResponseWrapper.failure("Inviting User is not a member of workspace."));
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(new ResponseWrapper<>("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> decideInvite(Long workspaceId, Long invitedUser, InvitationDecisionEnum decision) {
        try {
            Optional<WorkspaceModel> workspaceModel = workspaceRepository.findById(workspaceId);
            if (workspaceModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Workspace Does Not Exist"));

            Optional<InvitationModel> invitedUserModel = workspaceModel.get().getInvited()
                    .stream()
                    .filter(invited -> invited.getId().equals(new InvitationModelId(workspaceId, invitedUser)))
                    .findFirst();
            if (invitedUserModel.isPresent()) {
                switch (invitedUserModel.get().getDecision()) {
                    case UNDEF -> {
                        invitationRepository.updateInvitationDecision(decision, LocalDateTime.now(), new InvitationModelId(workspaceId, invitedUser));
                        if (decision == InvitationDecisionEnum.ACCEPT) {
                            workspaceMemberRepository.save(
                                    new WorkspaceMemberModel(
                                            new WorkspaceMemberId(workspaceId, invitedUser),
                                            invitedUserModel.get().getRole()
                                    )
                            );

                            ResponseWrapper<Boolean> res = boardInterface.addMemberToBoardFromWorkspace(
                                    new AddMemberFromWorkspaceDto(
                                            workspaceId,
                                            invitedUser,
                                            invitedUserModel.get().getRole()
                                    )
                            );

                            if (res.isSuccess())
                                return ResponseEntity.ok(ResponseWrapper.success("Updated the response, User is now a member of workspace"));
                            else
                                return ResponseEntity.ok(ResponseWrapper.failure(res.getError()));
                        }
                        return ResponseEntity.ok(ResponseWrapper.success("Updated the response"));
                    }
                    case ACCEPT -> {
                        Optional<WorkspaceMemberModel> memberModel = workspaceMemberRepository.findById(new WorkspaceMemberId(workspaceId, invitedUser));
                        if (memberModel.isEmpty())
                            return ResponseEntity.ok(ResponseWrapper.failure("Unknown semantic error."));

                        if (memberModel.get().getStatus() == StatusEnum.ACTIVE)
                            return ResponseEntity.ok(ResponseWrapper.failure("User already member of workspace."));

                        ResponseWrapper<Boolean> res = boardInterface.updateMemberStatusFromWorkspace(
                                new UpdateMemberStatusFromWorkspace(
                                        workspaceId,
                                        invitedUser,
                                        invitedUserModel.get().getRole(),
                                        StatusEnum.ACTIVE
                                )
                        );
                        if (!res.isSuccess())
                            return ResponseEntity.ok(ResponseWrapper.failure(res.getError()));
                        workspaceMemberRepository.updateRoleAndStatus(
                                workspaceId,
                                invitedUser,
                                invitedUserModel.get().getRole(),
                                StatusEnum.ACTIVE,
                                LocalDateTime.now()
                        );


                        return ResponseEntity.ok(ResponseWrapper.success("Updated the response, User is now a member of workspace"));
                    }
                    case REJECT -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User has rejected the invitation."));
                    }
                }
            } else {
                return ResponseEntity.ok(ResponseWrapper.failure("User is not invited to workspace."));
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(new ResponseWrapper<>("Internal Server Error"));
        }
        return null;
    }

    public ResponseEntity<ResponseWrapper<Boolean>> isWorkspaceAndMemberValid(Long workspaceId, Long userId) {
        try {
            Optional<WorkspaceModel> workspaceModel = workspaceRepository.findById(workspaceId);
            if (workspaceModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("INVALID_WORKSPACE"));

            Boolean isUserMember = workspaceModel.get().getMembers().stream()
                    .anyMatch(member -> member.getId().equals(new WorkspaceMemberId(workspaceId, userId)));
            if (!isUserMember) return ResponseEntity.ok(ResponseWrapper.failure("INVALID_USER"));

            ResponseEntity<ResponseWrapper<Boolean>> res = ResponseEntity.ok(new ResponseWrapper<>(isUserMember));
            return res;
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
    public ResponseEntity<ResponseWrapper<List<WorkspaceMemberRoleDto>>> getWorkspaceMembersIdRole(Long workspaceId, Long userId) {
        try {
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(userId);
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            List<WorkspaceMemberRoleDto> workspaceMembersIds = workspaceMemberRepository.getMembersIdRoleByWorkspaceId(workspaceId);
            if (workspaceMembersIds.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Workspace does not exist."));

            if (workspaceMembersIds.stream().noneMatch(it -> Objects.equals(it.getUserId(), userId)))
                return ResponseEntity.ok(ResponseWrapper.failure("User is not a member of workspace."));

            return ResponseEntity.ok(ResponseWrapper.success(workspaceMembersIds));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> deleteWorkspace(Long workspaceId, Long userId) {
        try {
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(userId);
            if (!isUserValid.isSuccess() || !isUserValid.getData())
                ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            boolean doesWorkspaceExist = workspaceRepository.existsById(workspaceId);
            if (!doesWorkspaceExist)
                return ResponseEntity.ok(ResponseWrapper.failure("Workspace Does not Exist."));

            Optional<WorkspaceMemberModel> memberModel = workspaceMemberRepository.findById(new WorkspaceMemberId(workspaceId, userId));
            if (memberModel.isEmpty() || memberModel.get().getStatus() != StatusEnum.ACTIVE)
                return ResponseEntity.ok(ResponseWrapper.failure("User is not part of Workspace."));

            if (memberModel.get().getRole() != RoleEnum.ADMIN)
                return ResponseEntity.ok(ResponseWrapper.failure("User is not authorized to delete the workspace."));


            ResponseWrapper<String> res = boardInterface.deleteBoardFromWorkspace(workspaceId);
            if (!res.isSuccess())
                return ResponseEntity.ok(res);

            deletionHelper.deleteWorkspaceData(workspaceId);

            return ResponseEntity.ok(ResponseWrapper.success("Workspace deleted."));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(ResponseWrapper.failure("Internal Server Error."));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> removeUser(Long workspaceId, Long userId, Long removerId) {
        try {
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(userId);
            if (!isUserValid.isSuccess() || !isUserValid.getData())
                ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            ResponseWrapper<Boolean> isRemoverValid = profileInterface.isUserValid(removerId);
            if (!isRemoverValid.isSuccess() || !isRemoverValid.getData())
                ResponseEntity.ok(ResponseWrapper.failure("Invalid Remover User."));


            boolean doesWorkspaceExist = workspaceRepository.existsById(workspaceId);
            if (!doesWorkspaceExist)
                return ResponseEntity.ok(ResponseWrapper.failure("Workspace Does not Exist."));

            Optional<WorkspaceMemberModel> memberModel = workspaceMemberRepository.findById(new WorkspaceMemberId(workspaceId, userId));
            if (memberModel.isEmpty() || memberModel.get().getStatus() != StatusEnum.ACTIVE)
                return ResponseEntity.ok(ResponseWrapper.failure("User is not part of Workspace."));

            Optional<WorkspaceMemberModel> removerModel = workspaceMemberRepository.findById(new WorkspaceMemberId(workspaceId, removerId));
            if (removerModel.isEmpty() || removerModel.get().getStatus() != StatusEnum.ACTIVE)
                return ResponseEntity.ok(ResponseWrapper.failure("Remover is not part of Workspace."));

            if (removerModel.get().getRole() != RoleEnum.ADMIN)
                return ResponseEntity.ok(ResponseWrapper.failure("User is not authorized to remove user."));

            ResponseWrapper<String> res = boardInterface.removeUserFromWorkspace(workspaceId, userId);
            if (!res.isSuccess())
                return ResponseEntity.ok(res);

            workspaceMemberRepository.updateStatus(
                    workspaceId,
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

    public ResponseEntity<ResponseWrapper<String>> deleteUser(Long userId) {
        try {
            List<Long> workspaceIds = workspaceMemberRepository.getWorkspaceIdsByUserId(userId);
            for(Long workspaceId: workspaceIds){
                workspaceMemberRepository.updateStatus(workspaceId, userId, StatusEnum.DELETED, LocalDateTime.now());

                Integer activeMembers = workspaceMemberRepository.getActiveCount(workspaceId, StatusEnum.ACTIVE);
                if(activeMembers == 0)
                    deletionHelper.deleteWorkspaceData(workspaceId);
            }

            return ResponseEntity.ok(ResponseWrapper.success("DATA_DELETED"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(ResponseWrapper.failure("Internal Server Error."));
        }
    }
}
