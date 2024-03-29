package ru.nsu.fit.markelov.interfaces.client;

import java.util.Date;

public interface User {

    enum UserType { Student, Teacher, Admin }

    /**
     * Returns user type.
     *
     * @return user type.
     */
    UserType getType();

    /**
     * Returns unique user name.
     *
     * @return unique user name.
     */
    String getName();

    /**
     * Returns user avatar address.
     *
     * @return user avatar address.
     */
    String getAvatarAddress();

    /**
     * Returns the date of last user activity.
     *
     * @return the date of last user activity.
     */
    Date getLastActive();

    /**
     * Returns whether user is blocked.
     *
     * @return whether user is blocked.
     */
    boolean isBlocked();
}
