package com.administration.bureau.http;


import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.administration.bureau.constant.Url;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by omyrobin on 2016/9/17.
 */
public class RetrofitManager {

    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_CONTENT_RANGE = "Content-Range";
    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ENCODING_GZIP = "gzip";
    public static final int DEFAULT_TIMEOUT = 15;
    public volatile static Retrofit retrofit;
    public volatile static OkHttpClient client;
    private static Map<String, String> headersMap;

    public static Retrofit getRetrofit(){
        if (retrofit == null) {
            synchronized (RetrofitManager.class) {
                initRetorfit(initClient());
                headersMap = new HashMap<>();
            }
        }
        return retrofit;
    }

    private static Retrofit initRetorfit(OkHttpClient client) {
        retrofit = new Retrofit.Builder()
                .baseUrl(Url.BUREAU_BASEURL)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //增加拦截器 支持header头
                .client(client)
                .build();
        return retrofit;
    }

    public static OkHttpClient initClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
//                                .addHeader(HEADER_CONTENT_TYPE, "application/x-www-from-urlencoded; charset=UTF-8")
                                .addHeader(HEADER_CONTENT_TYPE, "application/json; charset=UTF-8")
                                .addHeader(HEADER_ACCEPT, "application/json")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();
        return client;
    }
}
