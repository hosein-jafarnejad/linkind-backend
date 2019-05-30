package com.hosmos.linkind.utils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;

public class UserAgentExtractor {

    public static String getBrowserName (String userAgent) {
        Browser browser = Browser.parseUserAgentString(userAgent);
        return browser.getName();
    }

    public static String getBrowserVersion (String userAgent) {
        Version version = Browser.parseUserAgentString(userAgent).getVersion(userAgent);
        return version.getVersion();
    }

    public static String getOs (String userAgent) {
        return OperatingSystem.parseUserAgentString(userAgent).getName();
    }

}
