package ru.nsu.fit.markelov.httphandlers.util.parsers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UriParametersParser {

    private Map<String, List<String>> params;

    public UriParametersParser(String uri) {
        try {
            params = splitQuery(uri.split("\\?")[1]);
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
    }

    public Integer getIntegerParameter(String parameterName) {
        List<String> parameterList = params.get(parameterName);

        try {
            if (parameterList.size() == 1) {
                return Integer.parseInt(parameterList.get(0));
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Bad int parameter: " + parameterList.get(0));
        return null;
    }

    public String getStringParameter(String parameterName) {
        List<String> parameterList = params.get(parameterName);

        if (parameterList.size() == 1) {
            return parameterList.get(0);
        }

        return null;
    }

    public static Map<String, List<String>> splitQuery(String uri) throws UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
        final String[] pairs = uri.split("&");

        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.get(key).add(value);
        }

        return query_pairs;
    }
}
