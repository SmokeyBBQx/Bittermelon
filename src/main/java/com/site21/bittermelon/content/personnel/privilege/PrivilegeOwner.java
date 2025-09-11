package com.site21.bittermelon.content.personnel.privilege;

import java.util.Map;

public interface PrivilegeOwner {
    Map<String, Boolean> getPrivileges();
    String getName();
    default boolean canAccess() {
        return true;
    }
}
