package com.userprivilegescheduler;

import com.userprivilegescheduler.models.UserPrivilegeInput;
import com.userprivilegescheduler.models.UserPrivilegeOutput;
import com.userprivilegescheduler.solver.UserPrivilegeSolver;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

/**
 * Controller providing the end points for accessing the solver.
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {

    /**
     * Takes in the input as json and calculates the solution. If a solution exists it will be
     * returned in json format, otherwise an empty json document will be returned.
     * @param input input in json format
     * @return the solution in json
     */
    @PostMapping("/solve")
    UserPrivilegeOutput solve(@RequestBody UserPrivilegeInput input){
        UserPrivilegeSolver solver = new UserPrivilegeSolver();
        Optional<UserPrivilegeOutput> solution = solver.findSolution(input);
        return solution.orElse(new UserPrivilegeOutput());
    }
}
