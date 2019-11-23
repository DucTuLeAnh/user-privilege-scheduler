package com.userprivilegescheduler.models;

import java.util.List;

/**
 * This class contains the input which will be used for scheduling users and privileges in struct units,
 * using {@link com.userprivilegescheduler.solver.UserPrivilegeSolver}.
 */
public class UserPrivilegeInput {

    private Integer numberOfUsers;
    private Integer numberOfPrivileges;
    private Integer maxStructUnits;
    private List<UserPrivilege> userPrivileges;
    private List<UserPrivilege> forbiddenUserPrivileges;

    public Integer getNumberOfUsers() {
        return numberOfUsers;
    }

    public Integer getNumberOfPrivileges() {
        return numberOfPrivileges;
    }

    public Integer getMaxStructUnits() {
        return maxStructUnits;
    }

    public List<UserPrivilege> getUserPrivileges() {
        return userPrivileges;
    }

    public List<UserPrivilege> getForbiddenUserPrivileges() {
        return forbiddenUserPrivileges;
    }

}
