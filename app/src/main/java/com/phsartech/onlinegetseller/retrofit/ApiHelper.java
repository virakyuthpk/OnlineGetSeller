package com.phsartech.onlinegetseller.retrofit;

public class ApiHelper {

    private static boolean SHOW_LOG = true;
    //    private static String BASE_URL = "https://www.onlineget.com/api/";
    private static String BASE_URL = "http://192.168.0.43:8000/api/";

    public static APIClient getService() {
        return RetrofitClient.getClient(BASE_URL).create(APIClient.class);
    }

    public static boolean isShowLog() {
        return SHOW_LOG;
    }
}