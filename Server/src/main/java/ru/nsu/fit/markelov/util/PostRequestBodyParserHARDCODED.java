package ru.nsu.fit.markelov.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class PostRequestBodyParserHARDCODED {

    public static String getCodeHARDCODED(InputStream requestBodyInputStream) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            int i;
            while ((i = requestBodyInputStream.read()) != -1) {
                stringBuilder.append((char) i);
            }
            String encodedData = stringBuilder.toString().substring(5);

            return java.net.URLDecoder.decode(encodedData, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Bad post request body!");
        return null;
    }
}
