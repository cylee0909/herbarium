package com.cylee.androidlib.net;

public class Config {
    public static String HTTP_URL_HOST = "http://www.baidu.com";
    public static String getHost() {
        return HTTP_URL_HOST;
    }
    public static void initHost(String host) {
        HTTP_URL_HOST = host;
    }
}
