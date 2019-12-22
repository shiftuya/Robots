package ru.nsu.fit.markelov.objects_hardcoded;

import ru.nsu.fit.markelov.interfaces.Player;

public class PlayerHardcoded implements Player {

    private String avatarAddress;
    private String name;
    private boolean submitted;

    @Override
    public String getAvatarAddress() {
        return avatarAddress;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isSubmitted() {
        return submitted;
    }

    public void setAvatarAddress(String avatarAddress) {
        this.avatarAddress = avatarAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }
}
