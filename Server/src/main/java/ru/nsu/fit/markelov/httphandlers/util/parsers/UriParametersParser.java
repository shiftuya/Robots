package ru.nsu.fit.markelov.httphandlers.util.parsers;

import ru.nsu.fit.markelov.interfaces.ProcessingException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UriParametersParser {

    private String params;
    private Map<String, List<String>> paramsMap;

    public UriParametersParser(String uri) {
        try {
            params = uri.split("\\?")[1];
            paramsMap = splitQuery();
        } catch (UnsupportedEncodingException|ArrayIndexOutOfBoundsException e) {
            System.out.println("debug: " + e.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getParams() {
        return params;
    }

    public Integer getIntegerParameter(String parameterName) {
        List<String> parameterList = paramsMap.get(parameterName);

        try {
            if (parameterList.size() == 1) {
                return Integer.parseInt(parameterList.get(0));
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new ProcessingException(e.getClass().getSimpleName());
        }

        System.out.println("Bad int parameter: " + parameterList.get(0));
        return null;
    }

    public String getStringParameter(String parameterName) {
        try {
            List<String> parameterList = paramsMap.get(parameterName);

            if (parameterList.size() == 1) {
                return parameterList.get(0);
            }
        } catch (Exception e) {
            throw new ProcessingException(e.getClass().getSimpleName());
        }

        return null;
    }

    public Map<String, List<String>> splitQuery() throws UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
        final String[] pairs = params.split("&");

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
