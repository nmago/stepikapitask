package ru.nmago.stepikapitest.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RestMockServiceUtils {
    private static String getStrFromFile(String resPath) throws IOException{
        final InputStream stream = RestMockServiceUtils.class.getResourceAsStream(resPath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    /**
     * returns string content of JSON file from res
     * @param responseName - name of response
     * @return - json string
     * @throws IOException - if we cant get the resource file
     */
    public static String getResponseBodyStr(String responseName) throws IOException{
        return getStrFromFile("/mockResponses/" + responseName + ".json");
    }

    /**
     * returns list of top N courses IDs from ANSWER file from res
     * @param responseName - name of response
     * @return - list of IDs
     * @throws IOException - if we cant get the resource file
     */
    public static List<Long> getResponseAnswers(String responseName) throws IOException{
        String answers = getStrFromFile("/mockResponses/" + responseName + ".answer")
                .replace("\n","");
        List<Long> list = new ArrayList<>();
        for(String ans : answers.split(",")){
            list.add(Long.parseLong(ans));
        }
        return list;
    }
}
