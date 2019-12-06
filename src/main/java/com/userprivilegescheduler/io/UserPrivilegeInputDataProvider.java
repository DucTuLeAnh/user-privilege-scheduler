package com.userprivilegescheduler.io;

import com.userprivilegescheduler.models.UserPrivilege;
import com.userprivilegescheduler.models.UserPrivilegeInput;

import java.util.*;

public class UserPrivilegeInputDataProvider {

    public static final String USER_KEY = "users";
    public static final String PRIVILEGE_KEY = "privileges";

    /**
     * Reads the {@link UserPrivilegeInput} to determine the numbers of users and
     * numbers of privileges. The result will be returned in a map.
     * @param userInput the user input to read
     * @return numbers of users and privileges in a map
     */
    public static Map<String, Integer> getEntityNumbers(UserPrivilegeInput userInput){
        Set<Integer> setOfUsers = new HashSet<>(),  setOfPrivileges = new HashSet<>();

        fillEntitySets(setOfUsers, setOfPrivileges, userInput.getUserPrivileges());
        fillEntitySets(setOfUsers, setOfPrivileges, userInput.getForbiddenUserPrivileges());

        return createMapWithEntityNumbers(setOfUsers, setOfPrivileges);
    }

    private static HashMap<String, Integer> createMapWithEntityNumbers(Set<Integer> setOfUsers, Set<Integer> setOfPrivileges) {
        HashMap<String, Integer> entityNumbers = new HashMap<String, Integer>();
        entityNumbers.put(USER_KEY, setOfUsers.size());
        entityNumbers.put(PRIVILEGE_KEY, setOfPrivileges.size());
        return entityNumbers;
    }

    private static void fillEntitySets(Set<Integer> setOfUsers, Set<Integer> setOfPrivileges, List<UserPrivilege> input){
        input.forEach(userPrivilege -> {
            setOfUsers.add(userPrivilege.getUser());
            setOfPrivileges.add(userPrivilege.getPrivilege());
        });
    }
}
