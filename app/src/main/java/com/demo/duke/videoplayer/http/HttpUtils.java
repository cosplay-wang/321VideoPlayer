package com.demo.duke.videoplayer.http;

import android.content.Context;
import android.util.Log;

import com.demo.duke.videoplayer.AppLication;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static okhttp3.Protocol.HTTP_2;

/**
 * Created by zhiwei.wang on 2018/1/2.
 * wechat 760560322
 * 作用：
 */

public class HttpUtils {
    private static class HttpUtilsSingle {
        private static final OkHttpClient single = new OkHttpClient.Builder().cache(cache)
                .addInterceptor(new RetryIntercepter(3))//重连
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor) //网络请求的日志
                .addNetworkInterceptor(new StethoInterceptor())//chrome 调试
                .addInterceptor(headerInterceptor)
                .build();
    }

    private HttpUtils() {
    }

    public static OkHttpClient okHttpCilent() {
        return HttpUtilsSingle.single;
    }


    public static final int noNetworkCode = 10010;
    public static final String noNetworkMessage = "无网无数据";
    static long cacheSize = 10 * 1024 * 1024;
    static Cache cache = new Cache(new File("dd"), cacheSize);
    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);
    private static Interceptor headerInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();
            HttpUrl url = null;
            String surl = originalHttpUrl.url().toString();

            url = originalHttpUrl;

            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            requestBuilder.addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");
            //   requestBuilder.addHeader("User-Agent", SharedPrefencesUtils.getDeviceInfo(getContext(), "DEVICETYPE"));
            requestBuilder.addHeader("Accept", "application/json");
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    };


    public static Response okHttpRequest(String url, RequestBody body, List<OkhttpHeader> okhttpHeaderList){
        boolean netIsWork = NetWorkUtils.isNetworkConnected(AppLication.context);
        if (netIsWork) {
            OkHttpClient client = HttpUtils.okHttpCilent();

            Request.Builder builder = new Request.Builder();
            if (okhttpHeaderList != null && okhttpHeaderList.size() > 0) {
                for (OkhttpHeader okhttpHeader : okhttpHeaderList) {
                    builder.addHeader(okhttpHeader.getName(), okhttpHeader.getValue());
                }
            }
            if (body != null) {
                builder.method("POST", body);
            }
            Log.e("ddddddddddaaas",url);
            Call call = client.newCall(builder.url(url).build());

            Response response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        } else {
            Response noNetResponse = new Response.Builder()
                    .request(new Request.Builder().url(url).build())
                    .message(noNetworkMessage)
                    .protocol(HTTP_2).code(noNetworkCode).build();
            return noNetResponse;
        }
    }


}
