package com.hunterxi.lib.http;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author HunterXi
 * 创建日期：2019/4/19 15:24
 * 描述：
 * 请求拦截器
 * 在拦截器里面拦截请求，为每个请求都添加相同的公共参数。
 */
public class HttpCommonInterceptor implements Interceptor {

    private Map<String,String> mHeaderParamsMap = new HashMap<>();

    public HttpCommonInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Logger.d("add common params");
        Request oldRequest = chain.request();

        // 新请求
        Request.Builder requstBuilder = oldRequest.newBuilder();
        requstBuilder.method(oldRequest.method(), oldRequest.body());

        // 添加公共参数到Header中
        if (mHeaderParamsMap.size() > 0) {
            for (Map.Entry<String, String> param : mHeaderParamsMap.entrySet()) {
                requstBuilder.addHeader(param.getKey(), param.getValue());
            }
        }
        Request newRequest = requstBuilder.build();
        Logger.d("请求头：" + newRequest.headers());
        Logger.d("请求体：" + newRequest.url());
        return chain.proceed(newRequest);
    }

    public static class Builder {
        HttpCommonInterceptor mHttpCommonInterceptor;
        public Builder(){
            mHttpCommonInterceptor = new HttpCommonInterceptor();
        }

        public Builder addHeaderParams(String key, String value){
            mHttpCommonInterceptor.mHeaderParamsMap.put(key,value);
            return this;
        }

        public Builder  addHeaderParams(String key, int value){
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder  addHeaderParams(String key, float value){
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder  addHeaderParams(String key, long value){
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder  addHeaderParams(String key, double value){
            return addHeaderParams(key, String.valueOf(value));
        }

        public HttpCommonInterceptor build(){
            return mHttpCommonInterceptor;
        }
    }
}
