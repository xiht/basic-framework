package com.hunterxi.lib.http;

import com.hunterxi.lib.base.RxBus;
import com.hunterxi.lib.model.DownloadBean;
import com.hunterxi.lib.model.RxbusEvent;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/**
 * @author HunterXi
 * 创建日期：2019/7/9 10:53
 * 描述：文件下载
 */
public class DownloadResponseBody extends ResponseBody {

    private Response originalResponse; // 原结果

    public DownloadResponseBody(Response originalResponse) {
        this.originalResponse = originalResponse;
    }

    //返回内容类型
    @Override
    public MediaType contentType() {
        Logger.d("返回类型：" + originalResponse.body().contentType());
        return originalResponse.body().contentType();
    }

    //返回内容长度，没有则返回-1
    @Override
    public long contentLength() {
        Logger.d("返回长度：" + originalResponse.body().contentLength());
        return originalResponse.body().contentLength();
    }

    //返回缓存源，类似于io中的BufferedReader
    @Override
    public BufferedSource source() {
        return Okio.buffer(new ForwardingSource(originalResponse.body().source()) {
            long bytesReaded = 0;

            // 返回读取到的长度
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                bytesReaded += bytesRead == -1 ? 0 : bytesRead;
                // 通过RxBus发布进度信息
                RxBus.getInstance().post(new DownloadBean(contentLength(), bytesReaded));
                return bytesRead;
            }
        });
    }
}
