package com.hunterxi.lib.http;

import android.accounts.NetworkErrorException;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author HunterXi
 * 创建日期：2019/4/19 14:39
 * 描述：Observer 返回的请求结果统一处理类
 */
public abstract class BaseObserver<T> implements Observer<T> {

    // 订阅器
    protected Disposable disposable;

    // 开始
    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
        // 请求开始
        onRequestStart();
    }

    // 获取数据
    @Override
    public void onNext(T t) {
        try {
            // 请求成功,获取数据
            onSuccess(t);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 失败
    @Override
    public void onError(Throwable e) {
        unSubscribe();
        onRequestEnd();
        try {
            if (e instanceof ConnectException
                    || e instanceof TimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException) {
                onFailure(e, true);  //网络错误
            } else {
                onFailure(e, false);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // 结束
    @Override
    public void onComplete() {
        unSubscribe();
        onRequestEnd();
    }

    /**
     * 请求失败或者请求结束之后第一件事就是取消订阅
     */
    protected void unSubscribe() {
        //取消订阅
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * 请求开始 外部类要继承实现的
     */
    protected abstract void onRequestStart();

    /**
     * 请求结束 外部类要继承实现的
     */
    protected abstract void onRequestEnd();

    /**
     * 返回成功 外部类继承实现
     * @param t
     * @throws Exception
     */
    protected abstract void onSuccess(T t) throws Exception;

    /**
     * 返回失败 外部类要继承实现的
     * @param e
     * @param isNetWorkError 是否是网络错误
     * @throws Exception
     */
    protected abstract void onFailure(Throwable e, boolean isNetWorkError) throws Exception;
}
