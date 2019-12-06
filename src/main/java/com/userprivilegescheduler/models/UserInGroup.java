package com.userprivilegescheduler.models;

/**
 * Contains a tuple of id's representing the user which is assigned to a group.
 * This tuple is part of the {@link UserPrivilegeOutput} data structure, which can be parsed
 * to JSON.
 */
public class UserInGroup {

    private int user;
    private int group;

    public UserInGroup(int user, int group){
        this.user = user;
        this.group = group;
    }

    public int getUser() {
        return user;
    }

    public int getGroup() {
        return group;
    }

}
