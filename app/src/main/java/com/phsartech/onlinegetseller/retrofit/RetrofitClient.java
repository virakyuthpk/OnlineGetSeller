package com.phsartech.onlinegetseller.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitClient {
    private static Retrofit mInstance;

    public static synchronized Retrofit getClient(String baseUrl) {
        if (null == mInstance) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            if (ApiHelper.isShowLog())
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(2, TimeUnit.MINUTES);
            httpClient.writeTimeout(2, TimeUnit.MINUTES);
            httpClient.connectTimeout(5, TimeUnit.SECONDS);
            httpClient.addInterceptor(logging);
            httpClient.cache(null);
            mInstance = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mInstance;
    }
}
