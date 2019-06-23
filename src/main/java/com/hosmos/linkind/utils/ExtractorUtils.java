package com.hosmos.linkind.utils;

import com.hosmos.linkind.models.IpDetail;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.Version;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class ExtractorUtils {
    private static RestTemplate restTemplate = new RestTemplate();
    private static JacksonJsonObjectMarshaller<List<String>> jacksonMarshaller = new JacksonJsonObjectMarshaller<>();
    private static HttpHeaders httpHeaders = new HttpHeaders();

    static {
        httpHeaders.set("Content-Type", "application/json");
    }

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

    public static List<IpDetail> fetchIpsDetail(List<String> ips) throws JSONException {
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://ip-api.com/batch?fields=24579", new HttpEntity<String>(convertIpsToJson(ips).toString(), httpHeaders), String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return convertToIpDetailList(responseEntity.getBody());
            }
        } catch (ResourceAccessException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private static List<IpDetail> convertToIpDetailList(String responseBody) {
        List<IpDetail> result = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(responseBody);

            for (int i = 0; i < jsonArray.length(); i++) {
                result.add(makeIpDetailObject(jsonArray.getJSONObject(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static IpDetail makeIpDetailObject(JSONObject jsonObject) throws JSONException {
        IpDetail ipDetail = new IpDetail();
        ipDetail.setQuery(jsonObject.getString("query"));
        ipDetail.setStatus(jsonObject.getString("status"));

        if (ipDetail.getStatus().equals("success")) {
            ipDetail.setCountry(jsonObject.getString("country"));
            ipDetail.setCountryCode(jsonObject.getString("countryCode"));
        }

        return ipDetail;
    }

    private static JSONArray convertIpsToJson(List<String> ips) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (String ip : ips) {
            jsonArray.put(makeJsonObject(ip.trim()));
        }

        return jsonArray;
    }

    private static JSONObject makeJsonObject(String ip) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", ip);
        return jsonObject;
    }

    public static void main(String[] args) throws JSONException {
        List<String> ips = new ArrayList<>();
        ips.add("112.11.23.45");
        ips.add("112.11.23.41");
        ips.add("112.11.23.42");
        ips.add("112.11.23.87");

        System.out.println(fetchIpsDetail(ips));
    }

}
