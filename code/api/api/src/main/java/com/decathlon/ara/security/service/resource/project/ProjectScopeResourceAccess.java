package com.decathlon.ara.security.service.resource.project;

import com.decathlon.ara.security.dto.permission.ResourcePermission;
import com.decathlon.ara.security.service.UserSessionService;
import com.decathlon.ara.service.ProjectService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("projectScope")
public class ProjectScopeResourceAccess extends ProjectResourceAccess {

    protected ProjectScopeResourceAccess(UserSessionService userSessionService, ProjectService projectService) {
        super(userSessionService, projectService);
    }

    @Override
    protected List<ResourcePermission> getMaintainerPermissions() {
        return getReadOnlyPermissions();
    }

    @Override
    protected List<ResourcePermission> getMemberPermissions() {
        return getReadOnlyPermissions();
    }
}
