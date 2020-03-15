package com.userprivilegescheduler.solver;

import com.userprivilegescheduler.io.UserPrivilegeInputDataProvider;
import com.userprivilegescheduler.models.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.util.*;

import static org.chocosolver.solver.search.strategy.Search.activityBasedSearch;

/**
 * This class takes an {@link UserPrivilegeInput} object and generates an assignment of users
 * and privileges in groups, using the CHOCO-Solver.
 */
public class UserPrivilegeSolver {
    private static final String SEARCH_DURATION = "10s";

    private Model model;
    private UserPrivilegeInput userInput;
    private int numberOfUsers;
    private int numberOfPrivileges;
    private int numberOfGroups;
    private int maxGroups;

    private IntVar[][] userPrivileges;
    private IntVar[][] forbiddenUserPrivileges;
    private IntVar[][] userInGroup;
    private IntVar[][] privilegeInGroup;

    /**
     * Returns an assignment of users a privileges in groups, if a feasible solution can be found.
     * Otherwise an empty {@link Optional}
     *
     * @param input input data
     * @return an assignment or empty {@link Optional}
     */
    public Optional<UserPrivilegeOutput> findSolution(UserPrivilegeInput input) {
        userInput = input;
        Solution solution = searchSolutionUntilMaxGroupNumberReached();
        return Optional.ofNullable(solution).map(this::getOutputForSolution);
    }

    private Solution searchSolutionUntilMaxGroupNumberReached(){
        Solution solution = null;
        numberOfGroups = 0;
        while (solution == null && numberOfGroups <= maxGroups) {
            numberOfGroups++;
            solution = solve();
        }
        return solution;
    }

    private Solution solve() {
        initModelValues();
        prepareAllConstraints();
        initSolverParameter();
        return model.getSolver().findSolution();
    }

    private void initModelValues() {
        model = new Model("user privileges");
        Map<String, Integer> entityNumbers = UserPrivilegeInputDataProvider.getEntityNumbers(userInput);
        numberOfUsers = entityNumbers.get(UserPrivilegeInputDataProvider.USER_KEY);
        numberOfPrivileges = entityNumbers.get(UserPrivilegeInputDataProvider.PRIVILEGE_KEY);
        maxGroups = userInput.getMaxGroups();

        userPrivileges = model.intVarMatrix(numberOfUsers, numberOfPrivileges, 0, 1);
        forbiddenUserPrivileges = model.intVarMatrix(numberOfUsers, numberOfPrivileges, 0, 1);
        userInGroup = model.intVarMatrix(numberOfUsers, numberOfGroups, 0, 1);
        privilegeInGroup = model.intVarMatrix(numberOfPrivileges, numberOfGroups, 0, 1);
    }

    private void prepareAllConstraints() {
        prepareConstraint1();
        prepareConstraint2();
        prepareConstraint3();
        prepareConstraint4();
        prepareConstraint5();
    }

    private void initSolverParameter() {
        model.getSolver().limitTime(SEARCH_DURATION);
        model.getSolver().setSearch(activityBasedSearch(model.retrieveIntVars(true)));
    }

    /**
     * This constraint describes, that each user has the privileges assigned as declared in the input data.
     */
    private void prepareConstraint1() {
        for (UserPrivilege privilege : userInput.getUserPrivileges()) {
            model.arithm(userPrivileges[privilege.getUser()][privilege.getPrivilege()], "=", 1).post();
        }
    }

    /**
     * This constraint describes, that each user has the forbidden privileges assigned as declared in the input data.
     */
    private void prepareConstraint2() {
        for (UserPrivilege privilege : userInput.getForbiddenUserPrivileges()) {
            model.arithm(forbiddenUserPrivileges[privilege.getUser()][privilege.getPrivilege()], "=", 1).post();
        }
    }

    /**
     * This constraint describes, that if an forbidden privilege is assigned to an user. That user is
     * not allowed to be in the same group as the forbidden privilege.
     */
    private void prepareConstraint3() {
        for (int i = 0; i < numberOfUsers; i++) {
            for (int j = 0; j < numberOfPrivileges; j++) {
                for (int k = 0; k < numberOfGroups; k++) {
                    model.ifThen(
                            model.arithm(forbiddenUserPrivileges[i][j], "=", 1),
                            model.not(
                                    model.and(
                                            model.arithm(userInGroup[i][k], "=", 1),
                                            model.arithm(privilegeInGroup[j][k], "=", 1)
                                    ))
                    );
                }
            }
        }
    }

    /**
     * This constraint describes, that each user has to be in exactly one group.
     */
    private void prepareConstraint4() {
        for (int i = 0; i < numberOfUsers; i++) {
            model.sum(userInGroup[i], "=", 1).post();
        }
    }

    /**
     * If a user has a privilege. The user has to be at least in one group
     * as it's privilege.
     */
    private void prepareConstraint5() {

        for (int i = 0; i < numberOfUsers; i++) {
            for (int j = 0; j < numberOfPrivileges; j++) {

                Constraint[] atLeastOneMatch = new Constraint[numberOfGroups];
                for (int k = 0; k < numberOfGroups; k++) {
                    atLeastOneMatch[k] = model.and(model.arithm(userInGroup[i][k], "=", 1), model.arithm(privilegeInGroup[j][k], "=", 1));
                }

                model.ifThen(model.arithm(userPrivileges[i][j], "=", 1),
                        model.or(atLeastOneMatch)
                );
            }
        }
    }

    private UserPrivilegeOutput getOutputForSolution(Solution solution){
        UserPrivilegeOutput out = new UserPrivilegeOutput();

        for (int i = 0; i < numberOfUsers; i++) {
            for (int j = 0; j < numberOfGroups; j++) {

                if(solution.getIntVal(userInGroup[i][j])== 1){
                    out.getUserInGroupList().add(new UserInGroup(i,j));
                }
            }
        }

        for (int i = 0; i < numberOfPrivileges; i++) {
            for (int j = 0; j < numberOfGroups; j++) {
                if(solution.getIntVal(privilegeInGroup[i][j]) == 1){
                    out.getPrivilegeInGroupList().add(new PrivilegeInGroup(i,j));
                }
            }
        }
        return out;
    }

}
