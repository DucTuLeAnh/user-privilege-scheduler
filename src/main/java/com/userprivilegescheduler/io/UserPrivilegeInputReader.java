package com.userprivilegescheduler.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userprivilegescheduler.models.UserPrivilegeInput;
import java.io.File;
import java.io.IOException;

/**
 * Reads json files and mapps them to {@link UserPrivilegeInput}
 * (for debug purpose)
 */
public class UserPrivilegeInputReader {

    private UserPrivilegeInput input;

    public void readFromFile(String pathToFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.input = objectMapper.readValue(new File(pathToFile), UserPrivilegeInput.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UserPrivilegeInput getInput() {
        return this.input;
    }
}
