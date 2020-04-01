package com.hunterxi.lib.base;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.Observable;

/**
 * @author HunterXi
 * 创建日期：2019/5/31 14:50
 * 描述：RxBus 组件之间消息传递
 * 使用：
 * 发送消息：RxBus.getInstance().post(new EventOne("消息类型一"));
 * 接收消息：//订阅消息类型一的事件
 *         RxBus.getInstance().toObservable(EventOne.class)
 *                 .subscribe(new Consumer<EventOne>() {
 *                     @Override
 *                     public void accept(EventOne eventOne) throws Exception {
 *                         mTextView.setText("已接收" + eventOne.getMsg());
 *                     }
 *                 });
 */
public class RxBus {
    private final Relay<Object> mBus;

    public RxBus() {
        this.mBus = PublishRelay.create().toSerialized();
    }


    public static RxBus getInstance(){
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder{
        private static final RxBus INSTANCE = new RxBus();
    }

    /**
     * 发送消息
     * @param event
     */
    public void post(Object event) {
        mBus.accept(event);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }
}
