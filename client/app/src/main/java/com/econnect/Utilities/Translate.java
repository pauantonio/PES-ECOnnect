package com.econnect.Utilities;

import android.annotation.SuppressLint;

import com.econnect.client.R;
import com.econnect.client.StartupActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Translate {

    private static TreeMap<Integer, String> id2NameMapping = null;
    private static TreeMap<String, String> name2StringMapping = null;

    public static String id(int id) {
        try {
            return StartupActivity.globalContext().getString(id);
        }
        catch (NullPointerException e) {
            // In tests, globalContext is never initialized, mock the strings
            if (id2NameMapping == null || name2StringMapping == null)
                loadStringsUnitTest();

            String name = id2NameMapping.get(id);
            return name2StringMapping.get(name);
        }
    }
    public static String id(int id, Object... formatArgs) {
        try {
            return StartupActivity.globalContext().getString(id, formatArgs);
        }
        catch (NullPointerException e) {
            // In tests, globalContext is never initialized, mock the strings
            if (id2NameMapping == null || name2StringMapping == null)
                loadStringsUnitTest();

            String name = id2NameMapping.get(id);
            String format = name2StringMapping.get(name);
            assert format != null;
            return String.format(format, formatArgs);
        }
    }

    public static String getMappingsDebug() {
        if (id2NameMapping == null || name2StringMapping == null)
            loadStringsUnitTest();
        return convertMap(id2NameMapping) + "\n\n" + convertMap(name2StringMapping);
    }
    @SuppressLint("NewApi")
    private static String convertMap(TreeMap<?, ?> map) {
        String mapAsString = map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
        return mapAsString;
    }




    private static synchronized void loadStringsUnitTest() {
        // Only load once
        if (id2NameMapping == null) id2NameMapping = getId2NameMapping();
        if (name2StringMapping == null) name2StringMapping = getName2StringMapping();
    }

    private static TreeMap<Integer, String> getId2NameMapping() {
        final InputStream id2nameStream = Translate.class.getResourceAsStream("/R.txt");
        final BufferedReader id2name = new BufferedReader(new InputStreamReader(id2nameStream, StandardCharsets.UTF_8));

        String line;
        try {
            TreeMap<Integer, String> mapping = new TreeMap<>();
            while ((line = id2name.readLine()) != null) {
                String[] words = line.split(" ");
                // Ignore lines with more than 4 tokens
                if(words.length != 4) continue;
                // Ignore lines that don't start with "int string"
                if (!words[0].equals("int") || !words[1].equals("string")) continue;
                // Remove leading 0x
                Integer id = Integer.parseInt(words[3].substring(2), 16);
                mapping.put(id, words[2]);
            }
            return mapping;
        }
        catch (IOException e) {
            throw new RuntimeException("Error while reading strings", e);
        }
    }

    private static TreeMap<String, String> getName2StringMapping() {
        InputStream name2stringStream = Translate.class.getResourceAsStream("/values/strings.xml");
        Scanner sc = new Scanner(name2stringStream);

        Pattern p = Pattern.compile("<string name=\"(.*?)\">(.*?)</string>");
        TreeMap<String, String> mapping = new TreeMap<>();
        while (sc.findWithinHorizon(p, 0) != null) {
            String name = sc.match().group(1);
            String value = sc.match().group(2);

            // If string is not translatable, name is incorrect
            int index = name.indexOf('"');
            if (index != -1) name = name.substring(0, index);

            // Value may be surrounded by ""
            if (value.length() >= 2 && value.charAt(0) == '"') value = value.substring(1, value.length()-1);
            // Value may contain escape characters. We know for sure that value does not contain '<'
            value = value.replace("\\\\", "<");
            value = value.replace("\\", "");
            value = value.replace('<', '\\');

            mapping.put(name, value);
        }

        return mapping;
    }
}
