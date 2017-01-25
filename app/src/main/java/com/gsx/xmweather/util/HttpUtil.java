package com.gsx.xmweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 封装了okHttp的工具类
 */
public class HttpUtil {

    public static void  sendRequest(String url, Callback callback){

        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
