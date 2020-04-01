package com.hunterxi.lib.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * @author HunterXi
 * 创建日期：2019/4/24 15:33
 * 描述：Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(bindLayout());
        ButterKnife.bind(this);

        context = this;

        init();

        // 向用户获取危险权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions();
        }
    }

    /**
     * 向用户申请获取危险权限，带返回
     */
    protected abstract void requestPermissions();

    /**
     * 绑定布局的方法
     * @return
     */
    protected abstract int bindLayout();

    /**
     * 初始化方法，可以对数据进行初始化
     */
    protected abstract void init();

    /**
     * 启动相关的界面.
     */
    protected void startWithActivity(Context mContext, Class<?> clazz) {
        this.startWithActivity(mContext, clazz, null);
    }

    /**
     * 启动相关的界面.
     */
    protected void startWithActivity(Context mContext, Class<?> clazz,
                                     Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}
