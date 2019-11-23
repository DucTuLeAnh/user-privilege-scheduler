package com.userprivilegescheduler.models;

/**
 * Contains a tuple of id's representing the user which is assigned to a struct.
 * This tuple is part of the {@link UserPrivilegeOutput} data structure, which can be parsed
 * to JSON.
 */
public class UserInStruct {

    private int user;
    private int struct;

    public UserInStruct(int user, int struct){
        this.user = user;
        this.struct = struct;
    }

    public int getUser() {
        return user;
    }

    public int getStruct() {
        return struct;
    }

}
