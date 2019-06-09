package com.hosmos.linkind.utils;

public class StringUtils {

    public static String arrayToInAcceptableString(String[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }

        if (arr.length == 1) {
            return "'" + arr[0] + "'";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("'");
            sb.append(arr[i]);
            sb.append("'");
        }
        return sb.toString();
    }

}
