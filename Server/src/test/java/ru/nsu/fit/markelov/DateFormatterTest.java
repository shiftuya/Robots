package ru.nsu.fit.markelov;

import org.junit.Test;
import ru.nsu.fit.markelov.httphandlers.util.DateFormatter;

import java.util.Date;

public class DateFormatterTest {

    @Test
    public void test() {
        System.out.println(DateFormatter.formatLastActive(new Date().getTime()));
    }
}
