package com.boardify.workspace_service.model;

import com.boardify.workspace_service.dto.response.WorkspaceDto;
import com.boardify.workspace_service.dto.response.WorkspaceWithBoardDto;
import com.boardify.workspace_service.model.invitation.InvitationModel;
import com.boardify.workspace_service.model.member.WorkspaceMemberModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Table(name = "workspace")
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String description;
    Long createdBy;
    LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "workspaceId")
    List<WorkspaceMemberModel> members;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "workspaceId")
    List<InvitationModel> invited;

    public WorkspaceModel(String title, String description, Long created_by){
        this.title = title;
        this.description = description;
        this.createdBy = created_by;
        this.createdAt = LocalDateTime.now();
        this.members = Collections.emptyList();
        this.invited = Collections.emptyList();
    }


    public WorkspaceDto toWorkspaceDto(){
        return new WorkspaceDto(
                id,
                title,
                description,
                createdBy,
                createdAt,
                members != null
                        ? members.stream().map(WorkspaceMemberModel::toWorkspaceMemberDto).toList()
                        : List.of(),
                invited != null
                        ? invited.stream().map(InvitationModel::toInvitationDto).toList()
                        : List.of()
        );
    }

    public WorkspaceWithBoardDto toWorkspaceWithBoardDto(){
        return new WorkspaceWithBoardDto(
                id,
                title,
                description,
                createdBy,
                createdAt,
                members != null
                        ? members.stream().map(WorkspaceMemberModel::toWorkspaceMemberDto).toList()
                        : List.of(),
                invited != null
                        ? invited.stream().map(InvitationModel::toInvitationDto).toList()
                        : List.of(),
                List.of()
        );
    }
}
