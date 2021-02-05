package com.kunminx.puremusic.data.retrofit;

import com.kunminx.puremusic.BuildConfig;
import com.kunminx.puremusic.data.api.APIs;
import com.kunminx.puremusic.data.retrofit.interceptor.HeaderInterceptor;
import com.kunminx.puremusic.data.retrofit.logging.Level;
import com.kunminx.puremusic.data.retrofit.logging.LoggingInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Http {

    private static final int DEFAULT_TIMEOUT = 10;
    private Retrofit retrofit;

    //构造方法私有
    private Http() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new LoggingInterceptor
                        .Builder()//构建者模式
                        .loggable(BuildConfig.DEBUG) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
//                        .request("Request") // request的Tag
//                        .response("Response")// Response的Tag
//                        //.addHeader("log-header", "I am the log request header.") // 添加打印头, 注意 key 和 value 都不能是中文
                        .build()
                );
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(APIs.BASE_URL)
                .build();
    }

    //构造方法私有
    private Http(String host, Map<String, String> headers) {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new HeaderInterceptor(headers))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(new LoggingInterceptor
                        .Builder()//构建者模式
                        .loggable(BuildConfig.DEBUG) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
//                        .request("Request") // request的Tag
//                        .response("Response")// Response的Tag
//                        //.addHeader("log-header", "I am the log request header.") // 添加打印头, 注意 key 和 value 都不能是中文
                        .build()
                );
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(host)
                .build();
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final Http INSTANCE = new Http();
    }

    //获取单例
    public static Http getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static Http getInstance(String host, Map<String, String> headers) {
        return new Http(host, headers);
    }
}
