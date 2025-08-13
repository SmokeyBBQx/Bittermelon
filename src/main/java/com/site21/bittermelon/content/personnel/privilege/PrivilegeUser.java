package com.site21.bittermelon.content.personnel.privilege;

import java.util.Map;

public interface PrivilegeUser {
    Map<String, Boolean> getPrivileges();
    String getName();
}
