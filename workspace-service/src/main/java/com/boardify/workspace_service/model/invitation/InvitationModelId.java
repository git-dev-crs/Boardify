package com.boardify.workspace_service.model.invitation;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class InvitationModelId implements Serializable {
    Long workspaceId;
    Long invitedUser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvitationModelId that = (InvitationModelId) o;
        return Objects.equals(workspaceId, that.workspaceId) &&
                Objects.equals(invitedUser, that.invitedUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workspaceId, invitedUser);
    }
}
