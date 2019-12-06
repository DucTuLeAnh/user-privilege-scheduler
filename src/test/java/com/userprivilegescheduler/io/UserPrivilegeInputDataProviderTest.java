package com.userprivilegescheduler.io;

import com.userprivilegescheduler.models.UserPrivilege;
import com.userprivilegescheduler.models.UserPrivilegeInput;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * Test class for {@link UserPrivilegeInputDataProvider}
 */
public class UserPrivilegeInputDataProviderTest {

    @Test
    public void getEntityNumbers(){
        List<UserPrivilege> userPrivileges = createUserPrivilegeTestList(Arrays.asList(1,2,3), Collections.singletonList(1));
        List<UserPrivilege> forbiddenUserPrivileges = createUserPrivilegeTestList(Arrays.asList(1,2,3), Collections.singletonList(2));
        UserPrivilegeInput input = new UserPrivilegeInput(userPrivileges, forbiddenUserPrivileges, 10);

        Map<String, Integer> entityNumbers = UserPrivilegeInputDataProvider.getEntityNumbers(input);

        Assertions.assertThat(entityNumbers.get(UserPrivilegeInputDataProvider.USER_KEY)).isEqualTo(3);
        Assertions.assertThat(entityNumbers.get(UserPrivilegeInputDataProvider.PRIVILEGE_KEY)).isEqualTo(2);

    }


    private List<UserPrivilege> createUserPrivilegeTestList(List<Integer> users, List<Integer> privileges){
        List<UserPrivilege> list = new LinkedList<>();
        for(Integer u : users){
            for(Integer p : privileges){
                list.add(new UserPrivilege(u,p));
            }
        }
        return list;
    }

}