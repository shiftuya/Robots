package ru.nsu.fit.markelov.util;

public class DebugUtil {

    public static void printCookieUserName(String cookieUserName) {
        if (cookieUserName != null) {
            System.out.println("cookieUserName: " + cookieUserName);
        } else {
            System.out.println("cookieUserName: null");
        }
    }
}
