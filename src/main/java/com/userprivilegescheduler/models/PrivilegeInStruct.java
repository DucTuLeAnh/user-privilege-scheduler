package com.userprivilegescheduler.models;

/**
 * Contains a tuple of id's representing the privilege which is assigned to a struct.
 * This tuple is part of the {@link UserPrivilegeOutput} data structure, which can be parsed
 * to JSON.
 */
public class PrivilegeInStruct {

    private int privilege;
    private int struct;

    public PrivilegeInStruct(int privilege, int struct){
        this.privilege = privilege;
        this.struct = struct;
    }

    public int getPrivilege() {
        return privilege;
    }

    public int getStruct() {
        return struct;
    }
}
