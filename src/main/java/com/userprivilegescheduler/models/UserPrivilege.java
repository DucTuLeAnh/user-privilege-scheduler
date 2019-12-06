package com.userprivilegescheduler.models;

/**
 * Contains a tuple of id's representing the user which is assigned to a privilege.
 * This tuple is part of the {@link UserPrivilegeInput} data structure, which can be parsed to JSON.
 */
public class UserPrivilege {

    private Integer user;
    private Integer privilege;

    public UserPrivilege(Integer user, Integer privilege){
        this.user = user;
        this.privilege = privilege;
    }

    public Integer getUser() {
        return user;
    }

    public Integer getPrivilege() {
        return privilege;
    }

}
