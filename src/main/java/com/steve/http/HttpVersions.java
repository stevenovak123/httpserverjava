package com.steve.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// this is to set which HTTP versions are to be supported by the server.
public enum HttpVersions {
    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;

    HttpVersions(String LITERAL, int MAJOR, int MINOR) {
        this.LITERAL = LITERAL;
        this.MAJOR = MAJOR;
        this.MINOR = MINOR;
    }

    private static final Pattern httpVersionRegexPattern = Pattern.compile("^HTTP/(?<major>\\d+).(?<minor>\\d+)");

    public static HttpVersions getCompatibleVersion(String literalVersion) throws BadHttpVersionException {
        Matcher matcher = httpVersionRegexPattern.matcher(literalVersion);
        if (!matcher.find() || matcher.groupCount() != 2) {
            throw new BadHttpVersionException();
        }
        HttpVersions bestCompatible = null;
        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));

        for (HttpVersions version : HttpVersions.values()) {
            if (version.LITERAL.equals(literalVersion)) {
                return version;
            } else {
                if (version.MAJOR == major) {
                    if (version.MINOR < minor) {
                        bestCompatible = version;
                    }
                }
            }
        }
        return bestCompatible;
    }
}
