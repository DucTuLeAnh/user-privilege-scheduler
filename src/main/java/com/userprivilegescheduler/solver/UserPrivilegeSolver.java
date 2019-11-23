package com.userprivilegescheduler.solver;

import com.userprivilegescheduler.models.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.util.Optional;

import static org.chocosolver.solver.search.strategy.Search.activityBasedSearch;

/**
 * This class takes an {@link UserPrivilegeInput} object and generates an assignment of users
 * and privileges in struct units, using the CHOCO-Solver.
 */
public class UserPrivilegeSolver {
    private static final String SEARCH_DURATION = "10s";

    private Model model;
    private UserPrivilegeInput userInput;
    private int numberOfUsers;
    private int numberOfPrivileges;
    private int numberOfStructs;
    private int maxStructUnits;

    private IntVar[][] userPrivileges;
    private IntVar[][] forbiddenUserPrivileges;
    private IntVar[][] userInStruct;
    private IntVar[][] privilegeInStruct;

    /**
     * Returns an assignment of users a privileges in struct units, if a feasible solution can be found.
     * Otherwise an empty {@link Optional}
     * @param input input data
     * @return an assignment or empty {@link Optional}
     */
    public Optional<UserPrivilegeOutput> findSolution(UserPrivilegeInput input) {
        Solution solution = null;
        UserPrivilegeOutput out = null;
        numberOfStructs = 0;
        userInput = input;

        while (solution == null && numberOfStructs <= maxStructUnits) {
            numberOfStructs++;
            solution = solve();
        }

        if(solution != null){
            out = getOutputForSolution(solution);
        }

        return Optional.ofNullable(out);
    }

    private Solution solve() {
        initModelValues();
        prepareAllConstraints();
        initSolverParameter();
        return model.getSolver().findSolution();
    }

    private void initModelValues() {
        model = new Model("user privileges");

        numberOfUsers = userInput.getNumberOfUsers();
        numberOfPrivileges = userInput.getNumberOfPrivileges();
        maxStructUnits = userInput.getMaxStructUnits();

        userPrivileges = model.intVarMatrix(numberOfUsers, numberOfPrivileges, 0, 1);
        forbiddenUserPrivileges = model.intVarMatrix(numberOfUsers, numberOfPrivileges, 0, 1);
        userInStruct = model.intVarMatrix(numberOfUsers, numberOfStructs, 0, 1);
        privilegeInStruct = model.intVarMatrix(numberOfPrivileges, numberOfStructs, 0, 1);
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
     * not allowed to be in the same struct unit as the forbidden privilege.
     */
    private void prepareConstraint3() {
        for (int i = 0; i < numberOfUsers; i++) {
            for (int j = 0; j < numberOfPrivileges; j++) {
                for (int k = 0; k < numberOfStructs; k++) {
                    model.ifThen(
                            model.arithm(forbiddenUserPrivileges[i][j], "=", 1),
                            model.not(
                                    model.and(
                                            model.arithm(userInStruct[i][k], "=", 1),
                                            model.arithm(privilegeInStruct[j][k], "=", 1)
                                    ))
                    );
                }
            }
        }
    }

    /**
     * This constraint describes, that each user has to be in exactly one struct unit.
     */
    private void prepareConstraint4() {
        for (int i = 0; i < numberOfUsers; i++) {
            model.sum(userInStruct[i], "=", 1).post();
        }
    }

    /**
     * If a user has a privilege. The user has to be at least in one struct unit
     * as it's privilege.
     */
    private void prepareConstraint5() {

        for (int i = 0; i < numberOfUsers; i++) {
            for (int j = 0; j < numberOfPrivileges; j++) {

                Constraint[] atLeastOneMatch = new Constraint[numberOfStructs];
                for (int k = 0; k < numberOfStructs; k++) {
                    atLeastOneMatch[k] = model.and(model.arithm(userInStruct[i][k], "=", 1), model.arithm(privilegeInStruct[j][k], "=", 1));
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
            for (int j = 0; j < numberOfStructs; j++) {

                if(solution.getIntVal(userInStruct[i][j])== 1){
                    out.getUserInStructList().add(new UserInStruct(i,j));
                }
            }
        }

        for (int i = 0; i < numberOfPrivileges; i++) {
            for (int j = 0; j < numberOfStructs; j++) {
                if(solution.getIntVal(privilegeInStruct[i][j]) == 1){
                    out.getPrivilegeInStructList().add(new PrivilegeInStruct(i,j));
                }
            }
        }
        return out;
    }

}
