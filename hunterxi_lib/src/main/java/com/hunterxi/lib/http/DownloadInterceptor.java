package com.hunterxi.lib.http;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author HunterXi
 * 创建日期：2019/7/10 17:46
 * 描述：文件下载拦截器，计算下载进度
 */
public class DownloadInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Logger.d("请求：" + chain.request().body());
        Response response = chain.proceed(chain.request());
        Logger.d("返回：" + response.header("Content-Length"));
        return response.newBuilder()
                .body(new DownloadResponseBody(response))
                .build();
    }

    public static class Builder {
        DownloadInterceptor downloadInterceptor;

        public Builder(){
            downloadInterceptor = new DownloadInterceptor();
        }

        public DownloadInterceptor build() {
            return downloadInterceptor;
        }
    }
}
