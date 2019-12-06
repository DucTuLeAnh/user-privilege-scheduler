package com.userprivilegescheduler.models;

import java.util.LinkedList;
import java.util.List;

/**
 * This class contains the output representing the assignment of users and privileges in groups,
 * using {@link com.userprivilegescheduler.solver.UserPrivilegeSolver}.
 */
public class UserPrivilegeOutput {

    List<UserInGroup> userInGroupList;
    List<PrivilegeInGroup> privilegeInGroupList;

    public UserPrivilegeOutput(){
        userInGroupList = new LinkedList<>();
        privilegeInGroupList = new LinkedList<>();
    }

    public List<UserInGroup> getUserInGroupList() {
        return userInGroupList;
    }

    public List<PrivilegeInGroup> getPrivilegeInGroupList() {
        return privilegeInGroupList;
    }

}
