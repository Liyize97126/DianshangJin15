package com.bawei.dianshangjin15;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        //选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }
}
