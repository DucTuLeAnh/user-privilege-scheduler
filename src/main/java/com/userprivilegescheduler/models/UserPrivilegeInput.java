package com.userprivilegescheduler.models;

import java.util.List;

/**
 * This class contains the input which will be used for scheduling users and privileges in groups,
 * using {@link com.userprivilegescheduler.solver.UserPrivilegeSolver}.
 */
public class UserPrivilegeInput {

    private Integer maxGroups;
    private List<UserPrivilege> userPrivileges;
    private List<UserPrivilege> forbiddenUserPrivileges;

    public UserPrivilegeInput(List<UserPrivilege> userPrivileges, List<UserPrivilege> forbiddenUserPrivileges, Integer maxGroups){
        this.userPrivileges = userPrivileges;
        this.forbiddenUserPrivileges = forbiddenUserPrivileges;
        this.maxGroups = maxGroups;
    }

    public Integer getMaxGroups() {
        return maxGroups;
    }

    public List<UserPrivilege> getUserPrivileges() {
        return userPrivileges;
    }

    public List<UserPrivilege> getForbiddenUserPrivileges() {
        return forbiddenUserPrivileges;
    }

}
