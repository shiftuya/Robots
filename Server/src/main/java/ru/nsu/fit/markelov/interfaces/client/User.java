package ru.nsu.fit.markelov.interfaces.client;

import java.util.Date;

public interface User {

    enum UserType { STUDENT, TEACHER, ADMIN }

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
}
