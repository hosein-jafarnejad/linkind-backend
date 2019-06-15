package com.hosmos.linkind.utils;

import com.hosmos.linkind.models.IpDetail;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.Version;
import org.springframework.web.client.RestTemplate;

public class ExtractorUtils {
    private static RestTemplate restTemplate = new RestTemplate();

    public static String getBrowserName(String userAgent) {
        Browser browser = Browser.parseUserAgentString(userAgent);
        return browser.getName();
    }

    public static String getBrowserVersion(String userAgent) {
        Version version = Browser.parseUserAgentString(userAgent).getVersion(userAgent);
        return version.getVersion();
    }

    public static String getOs(String userAgent) {
        return OperatingSystem.parseUserAgentString(userAgent).getName();
    }

    public static IpDetail findCountry (String ip) {
        String connection = String.format("http://ip-api.com/json/%s?fields=status,country,countryCode", ip);

        return restTemplate.getForObject(connection, IpDetail.class);
    }

}
