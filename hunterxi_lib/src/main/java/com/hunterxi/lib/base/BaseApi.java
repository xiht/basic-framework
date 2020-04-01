package com.hunterxi.lib.base;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author HunterXi
 * 创建日期：2019/7/9 14:25
 * 描述：公共请求
 */
public interface BaseApi {

    @Streaming // 注明为流文件，防止retrofit将大文件读入内存
    @GET
    Observable<ResponseBody> downloadFile(@Url String url); //通过@Url覆盖baseurl

}
