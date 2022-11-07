package com.econnect.Utilities;

import java.util.regex.Pattern;

public class URLPatternMatcher extends PatternMatcher {
    private static final Pattern urlPattern = Pattern.compile("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)");

    @Override
    protected Pattern getPattern() {
        return urlPattern;
    }
}
