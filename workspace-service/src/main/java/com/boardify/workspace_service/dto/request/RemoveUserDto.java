package com.boardify.workspace_service.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveUserDto {
    Long workspaceId;
    Long userId;
    Long removerId;
}
