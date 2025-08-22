package com.boardify.workspace_service.service;

import com.boardify.workspace_service.repository.InvitationRepository;
import com.boardify.workspace_service.repository.WorkspaceMemberRepository;
import com.boardify.workspace_service.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeletionHelper {
    @Autowired
    WorkspaceRepository workspaceRepository;
    @Autowired
    WorkspaceMemberRepository workspaceMemberRepository;
    @Autowired
    InvitationRepository invitationRepository;

    void deleteWorkspaceData(Long workspaceId){
        invitationRepository.deleteByWorkspaceId(workspaceId);
        workspaceMemberRepository.deleteByWorkspaceId(workspaceId);
        workspaceRepository.deleteById(workspaceId);
    }
}
