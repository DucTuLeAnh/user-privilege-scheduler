package com.userprivilegescheduler.io;

import com.userprivilegescheduler.models.UserPrivilege;
import com.userprivilegescheduler.models.UserPrivilegeInput;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides meta data which is not explicitly given in the {@link UserPrivilegeInput}. E.g number of users and
 * number of privileges.
 */
public class UserPrivilegeInputDataProvider {

    public static final String USER_KEY = "users";
    public static final String PRIVILEGE_KEY = "privileges";

    /**
     * Reads the {@link UserPrivilegeInput} to determine the number of users and
     * number of privileges. The result will be returned in a map.
     *
     * @param userInput the user input to read
     * @return number of users and privileges in a map
     */
    public static Map<String, Integer> getEntityNumbers(UserPrivilegeInput userInput) {
        HashMap<String, Integer> entityNumbers = new HashMap<>();
        entityNumbers.put(USER_KEY, getAllUsers(userInput).size());
        entityNumbers.put(PRIVILEGE_KEY, getAllPrivileges(userInput).size());
        return entityNumbers;
    }

    private static Set<Integer> getAllPrivileges(UserPrivilegeInput userInput) {
        Set<Integer> allPrivilegesSet = userInput.getUserPrivileges().stream()
                .map(UserPrivilege::getPrivilege)
                .collect(Collectors.toSet());
        allPrivilegesSet.addAll(
                userInput.getForbiddenUserPrivileges().stream()
                        .map(UserPrivilege::getPrivilege)
                        .collect(Collectors.toSet()));
        return allPrivilegesSet;
    }

    private static Set<Integer> getAllUsers(UserPrivilegeInput userInput) {
        Set<Integer> allUsersSet = userInput.getUserPrivileges().stream()
                .map(UserPrivilege::getUser)
                .collect(Collectors.toSet());
        allUsersSet.addAll(
                userInput.getForbiddenUserPrivileges().stream()
                        .map(UserPrivilege::getUser)
                        .collect(Collectors.toSet()));
        return allUsersSet;
    }
}
