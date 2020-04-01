package com.hunterxi.lib.http;

import com.hunterxi.lib.utils.SSLSocketUtil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author HunterXi
 * 创建日期：2019/4/19 16:50
 * 描述：Retrofit单例初始化类
 */
public class RetrofitHelper {
    private static final int DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private static final int DEFAULT_WRITE_TIME_OUT = 10;

    private OkHttpClient.Builder builder;
    private HttpCommonInterceptor.Builder interceptorBuilder;
    private Retrofit.Builder retrofitBuilder;

    private RetrofitHelper() {
        // 创建 OKHttpClient
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS); //连接超时时间
        builder.writeTimeout(DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS); //写操作超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS); //读操作超时时间

        // 拦截器builder
        interceptorBuilder = new HttpCommonInterceptor.Builder();
        // Retrofit builder
        retrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //配置支持RJava2的 Observable
                .addConverterFactory(GsonConverterFactory.create()); //设置数据解析器，会将返回的数据自动转换为 对应的 class
        // 默认设置一个baseUrl，后面根据需求使用方法setBaseUrl(String url)进行自定义设置
        retrofitBuilder.baseUrl("https://www.baidu.com");
    }

    /**
     * 处理 https 请求
     * @param ignoreCertificate 是否忽略证书 true表示忽略
     */
    public RetrofitHelper handleHttps(boolean ignoreCertificate) {
        if (ignoreCertificate) {
            builder.sslSocketFactory(SSLSocketUtil.customSSLSocketFactory(), new SSLSocketUtil.TrustAllManager());
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            builder.hostnameVerifier(hostnameVerifier);
        }
        return this;
    }

    private static class SingletonHolder {
        private static final RetrofitHelper INSTANCE = new RetrofitHelper();
    }

    /**
     * 获取RetrofitServiceManager
     *
     * @return
     */
    public static RetrofitHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 添加请求头部信息
     * @param headerMap
     * @return
     */
    public RetrofitHelper addRequestHeader(Map<String, String> headerMap) {
        if (headerMap.size() > 0) {
            for (Map.Entry<String, String> param : headerMap.entrySet()) {
                interceptorBuilder.addHeaderParams(param.getKey(), param.getValue());
            }
        }
        return this;
    }

    /**
     * 为网络请求设置请求域名，该方法必需，不然会用默认域名，请求返回报错
     * @param baseUrl
     * @return
     */
    public RetrofitHelper setBaseUrl(String baseUrl) {
        retrofitBuilder.baseUrl(baseUrl);
        return this;
    }

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * 使用方法:   mMovieService = RetrofitHelper.getInstance()
     *                      .setBaseUrl(baseUrl)
     *                      .addRequestHeader(headerMap)
     *                      .create(MovieService.class);
     * @return
     */
    public <T> T create(Class<T> service) {
        // 拦截器
        HttpCommonInterceptor commonInterceptor = interceptorBuilder.build();
        builder.addInterceptor(commonInterceptor);
        // 添加文件下载拦截器，便于计算下载进度
        builder.addNetworkInterceptor(new DownloadInterceptor.Builder().build());
        // 创建Retrofit
        Retrofit mRetrofit = retrofitBuilder
                    .client(builder.build())
                    .build();
        return mRetrofit.create(service);
    }
}
