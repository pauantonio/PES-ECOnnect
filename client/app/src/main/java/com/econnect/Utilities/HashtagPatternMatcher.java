package com.econnect.Utilities;

import java.util.regex.Pattern;

public class HashtagPatternMatcher extends PatternMatcher {
    private static final Pattern hashtagPattern = Pattern.compile(
            "(?<=[\\s>]|^)#(\\w*[A-Za-z_]+\\w*)");

    @Override
    protected Pattern getPattern() {
        return hashtagPattern;
    }
}
