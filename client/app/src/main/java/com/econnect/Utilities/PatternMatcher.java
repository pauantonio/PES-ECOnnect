package com.econnect.Utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PatternMatcher {
    public interface IPatternFoundCallback {
        void found(int startIndex, int endIndex);
    }

    public void find(String input, IPatternFoundCallback callback) {
        Matcher matcher = getPattern().matcher(input);
        while (matcher.find()) {
            int matchStart = matcher.start(0);
            int matchEnd = matcher.end();
            callback.found(matchStart, matchEnd);
        }
    }

    protected abstract Pattern getPattern();
}
