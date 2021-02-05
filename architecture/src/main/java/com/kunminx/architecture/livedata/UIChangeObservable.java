package com.kunminx.architecture.livedata;

import com.kunminx.architecture.bean.Mask;

public class UIChangeObservable {
    //开始加载
    public SingleLiveEvent<Mask> maskLoad = new SingleLiveEvent<>();//遮罩监听
    //结束加载
    public SingleLiveEvent<Mask> pageLoad = new SingleLiveEvent<>();//分页监听

}
