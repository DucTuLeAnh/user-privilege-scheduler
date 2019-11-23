package com.userprivilegescheduler.io;

import com.userprivilegescheduler.models.PrivilegeInStruct;
import com.userprivilegescheduler.models.UserInStruct;
import com.userprivilegescheduler.models.UserPrivilegeOutput;

/**
 * Prints the content of {@link UserPrivilegeOutput} on System.out.
 * (for debug purpose)
 */
public class UserPrivilegeOutputPrinter {

    public void printOutput(UserPrivilegeOutput output) {

        for (UserInStruct u : output.getUserInStructList()) {
            System.out.println("User in struct: (" + u.getUser() + "," + u.getStruct() + ")");
        }
        System.out.println("\n");
        for (PrivilegeInStruct p : output.getPrivilegeInStructList()) {
            System.out.println("Privilege in struct: (" + p.getPrivilege() + "," + p.getStruct() + ")");
        }

    }
}
