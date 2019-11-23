package com.userprivilegescheduler.models;

import java.util.LinkedList;
import java.util.List;

/**
 * This class contains the output representing the assignment of users and privileges in struct units,
 * using {@link com.userprivilegescheduler.solver.UserPrivilegeSolver}.
 */
public class UserPrivilegeOutput {

    List<UserInStruct> userInStructList;
    List<PrivilegeInStruct> privilegeInStructList;

    public UserPrivilegeOutput(){
        userInStructList = new LinkedList<>();
        privilegeInStructList = new LinkedList<>();
    }

    public List<UserInStruct> getUserInStructList() {
        return userInStructList;
    }

    public List<PrivilegeInStruct> getPrivilegeInStructList() {
        return privilegeInStructList;
    }

}
