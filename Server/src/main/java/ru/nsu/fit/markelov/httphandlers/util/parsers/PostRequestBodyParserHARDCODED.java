package ru.nsu.fit.markelov.httphandlers.util.parsers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class PostRequestBodyParserHARDCODED {

    private static final String CHARSET = StandardCharsets.UTF_8.name();

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

    public static String decode(InputStream iStream) {
        try (ByteArrayOutputStream oStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = iStream.read(buffer)) != -1) {
                oStream.write(buffer, 0, bytesRead);
            }

            return java.net.URLDecoder.decode(oStream.toString(CHARSET), CHARSET);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Bad post request body!");
        return null;
    }
}
