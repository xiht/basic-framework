package com.hunterxi.lib.model;

/**
 * @author HunterXi
 * 创建日期：2019/7/9 11:23
 * 描述：文件下载实体类
 */
public class DownloadBean {
    private long total;
    private long bytesReaded;

    public DownloadBean(long total, long bytesReaded) {
        this.total = total;
        this.bytesReaded = bytesReaded;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getBytesReaded() {
        return bytesReaded;
    }

    public void setBytesReaded(long bytesReaded) {
        this.bytesReaded = bytesReaded;
    }
}
