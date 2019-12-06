package com.userprivilegescheduler.models;

/**
 * Contains a tuple of id's representing the privilege which is assigned to a group.
 * This tuple is part of the {@link UserPrivilegeOutput} data structure, which can be parsed
 * to JSON.
 */
public class PrivilegeInGroup {

    private int privilege;
    private int group;

    public PrivilegeInGroup(int privilege, int group){
        this.privilege = privilege;
        this.group = group;
    }

    public int getPrivilege() {
        return privilege;
    }

    public int getGroup() {
        return group;
    }
}
