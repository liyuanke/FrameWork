package com.kunminx.architecture.ui.state;

import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.livedata.UIChangeObservable;
import com.kunminx.architecture.utils.LogUtils;

public class BaseViewModel extends ViewModel {
    protected UIChangeObservable netLoadChangeObservable = new UIChangeObservable();

    @Override
    protected void onCleared() {
        super.onCleared();
        netLoadChangeObservable = null;
        LogUtils.INFO("onCleared");
    }

    public UIChangeObservable getNetLoadChangeObservable() {
        return netLoadChangeObservable;
    }
}
