package com.userprivilegescheduler.solver;

import com.userprivilegescheduler.models.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Test class to check the validity of the solution which is returned by {@link UserPrivilegeSolver#findSolution(UserPrivilegeInput)}
 */
public class UserPrivilegeSolverTest {

    @Test
    public void findSolution_validInput_resultIsPresent(){
        UserPrivilegeSolver solver = new UserPrivilegeSolver();
        UserPrivilegeInput input = getValidInput();
        Optional<UserPrivilegeOutput> result = solver.findSolution(input);
        Assertions.assertThat(result.isPresent()).isEqualTo(true);
    }

    @Test
    public void findSolution_faultyInput_userPrivConflictsWithForbiddenPriv_noResult() {
        UserPrivilegeSolver solver = new UserPrivilegeSolver();
        Optional<UserPrivilegeOutput> result = solver.findSolution(getInputWhereUserPrivConflictsWithForbiddenPriv());
        Assertions.assertThat(result.isPresent()).isEqualTo(false);
    }

    @Test
    public void findSolution_userNotInSameGroupAsForbiddenPriv(){
        UserPrivilegeSolver solver = new UserPrivilegeSolver();
        UserPrivilegeInput input = getValidInput();
        Optional<UserPrivilegeOutput> result = solver.findSolution(input);

        List<UserInGroup> userInGroups = result.map(UserPrivilegeOutput::getUserInGroupList).orElse(new LinkedList<>());
        List<PrivilegeInGroup> privInGroups = result.map(UserPrivilegeOutput::getPrivilegeInGroupList).orElse(new LinkedList<>());

        for(UserPrivilege forbiddenPriv : input.getForbiddenUserPrivileges()){
            List<UserInGroup> groupsWithGivenUserId = userInGroups.stream().filter(usr -> usr.getUser() == forbiddenPriv.getUser()).collect(Collectors.toList());
            List<PrivilegeInGroup> groupsWithGivenPrivId = privInGroups.stream().filter(priv -> priv.getPrivilege() == forbiddenPriv.getPrivilege()).collect(Collectors.toList());
            Assertions.assertThat(isUserInAtLeastOneGroupAsPrivilege(groupsWithGivenUserId, groupsWithGivenPrivId)).isEqualTo(false);
        }
    }

    @Test
    public void findSolution_everyUserInExactlyOneGroup(){
        UserPrivilegeSolver solver = new UserPrivilegeSolver();
        UserPrivilegeInput input = getValidInput();
        Optional<UserPrivilegeOutput> result = solver.findSolution(input);

        List<UserInGroup> userInGroups = result.map(UserPrivilegeOutput::getUserInGroupList).orElse(new LinkedList<>());

        userInGroups.forEach(userInGroup -> {
            Assertions.assertThat(userInExactlyOneGroup(userInGroup.getUser(), userInGroups)).isEqualTo(true);
        });
    }

    @Test
    public void findSolution_everyUserGotRequestedPrivilege() {
        UserPrivilegeSolver solver = new UserPrivilegeSolver();
        UserPrivilegeInput input = getValidInput();
        Optional<UserPrivilegeOutput> result = solver.findSolution(input);

        List<UserInGroup> userInGroups = result.map(UserPrivilegeOutput::getUserInGroupList).orElse(new LinkedList<>());
        List<PrivilegeInGroup> privInGroups = result.map(UserPrivilegeOutput::getPrivilegeInGroupList).orElse(new LinkedList<>());

        for (UserPrivilege requestedPriv : input.getUserPrivileges()) {
            List<UserInGroup> groupsWithRequestedUserId = userInGroups.stream().filter(usr -> usr.getUser() == requestedPriv.getUser()).collect(Collectors.toList());
            List<PrivilegeInGroup> groupsWithRequestedPrivId = privInGroups.stream().filter(priv -> priv.getPrivilege() == requestedPriv.getPrivilege()).collect(Collectors.toList());
            Assertions.assertThat(isUserInAtLeastOneGroupAsPrivilege(groupsWithRequestedUserId,groupsWithRequestedPrivId)).isEqualTo(true);
        }
    }

    private boolean userInExactlyOneGroup(int userId, List<UserInGroup> userInGroups){
        return userInGroups.stream().filter(usrInGroup -> usrInGroup.getUser() == userId).count() == 1;
    }

    private boolean isUserInAtLeastOneGroupAsPrivilege(List<UserInGroup> userInGroups, List<PrivilegeInGroup> privilegeInGroups) {
        for (UserInGroup usrInGrp : userInGroups) {
            for (PrivilegeInGroup privInGrp : privilegeInGroups) {
                if (usrInGrp.getGroup() == privInGrp.getGroup()) {
                    return true;
                }
            }
        }
        return false;
    }

    private UserPrivilegeInput getInputWhereUserPrivConflictsWithForbiddenPriv() {
        List<UserPrivilege> userPrivileges = new LinkedList<>();
        userPrivileges.add(new UserPrivilege(0, 0));
        userPrivileges.add(new UserPrivilege(0, 1));
        List<UserPrivilege> forbiddenPrivileges = new LinkedList<>();
        forbiddenPrivileges.add(new UserPrivilege(0, 1));

        return new UserPrivilegeInput(userPrivileges, forbiddenPrivileges, 5);
    }

    private UserPrivilegeInput getValidInput() {
        List<UserPrivilege> userPrivileges = new LinkedList<>();
        userPrivileges.add(new UserPrivilege(0, 0));
        userPrivileges.add(new UserPrivilege(0, 1));
        userPrivileges.add(new UserPrivilege(1, 1));
        userPrivileges.add(new UserPrivilege(1, 2));
        List<UserPrivilege> forbiddenPrivileges = new LinkedList<>();
        forbiddenPrivileges.add(new UserPrivilege(1, 0));
        return new UserPrivilegeInput(userPrivileges, forbiddenPrivileges, 5);
    }

}