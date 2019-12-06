package com.userprivilegescheduler.io;

import com.userprivilegescheduler.models.PrivilegeInGroup;
import com.userprivilegescheduler.models.UserInGroup;
import com.userprivilegescheduler.models.UserPrivilegeOutput;

/**
 * Prints the content of {@link UserPrivilegeOutput} on System.out.
 * (for debug purpose)
 */
public class UserPrivilegeOutputPrinter {

    public void printOutput(UserPrivilegeOutput output) {

        for (UserInGroup u : output.getUserInGroupList()) {
            System.out.println("User in group: (" + u.getUser() + "," + u.getGroup() + ")");
        }
        System.out.println("\n");
        for (PrivilegeInGroup p : output.getPrivilegeInGroupList()) {
            System.out.println("Privilege in group: (" + p.getPrivilege() + "," + p.getGroup() + ")");
        }

    }
}
