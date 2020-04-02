package ru.nsu.fit.markelov.httphandlers.inputs;

import ru.nsu.fit.markelov.httphandlers.util.FileResource;

public class UserInput {

    private String name;
    private String password;
    private String repeatPassword;
    private String type;

    private FileResource avatarResource;

    public UserInput() {
        avatarResource = new FileResource();
    }

    public void prepare() {
        if (avatarResource.getBytes().length == 0) {
            avatarResource = null;
        }
    }

    public String getError() {
        String error = "";

        if (name.isEmpty()) {
            error += "Name field cannot be empty.\n";
        }

        if (password.isEmpty()) {
            error += "password field cannot be empty.\n";
        }

        if (repeatPassword.isEmpty()) {
            error += "repeatPassword field cannot be empty.\n";
        }

        if (!password.equals(repeatPassword)) {
            error += "Passwords do not match.\n";
        }

        if (type.isEmpty()) {
            error += "type field cannot be empty.\n";
        }

        return error;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FileResource getAvatarResource() {
        return avatarResource;
    }

    public void setAvatarResource(FileResource avatarResource) {
        this.avatarResource = avatarResource;
    }
}
