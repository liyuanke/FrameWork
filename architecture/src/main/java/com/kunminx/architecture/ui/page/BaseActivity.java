/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunminx.architecture.ui.page;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.kunminx.architecture.BaseApplication;
import com.kunminx.architecture.data.response.manager.NetworkStateManager;
import com.kunminx.architecture.ui.state.BaseViewModel;
import com.kunminx.architecture.utils.AdaptScreenUtils;
import com.kunminx.architecture.utils.BarUtils;
import com.kunminx.architecture.utils.LogUtils;
import com.kunminx.architecture.utils.ScreenUtils;
import com.kunminx.architecture.widget.LoaddingDialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Create by KunMinX at 19/8/1
 */
public abstract class BaseActivity<T extends BaseViewModel> extends DataBindingActivity {

    private ViewModelProvider mActivityProvider;
    private ViewModelProvider mApplicationProvider;
    protected T mViewModel;
    private LoaddingDialog mLoaddingDialog;
    protected Handler mHandler;

    @CallSuper
    @Override
    protected void initViewModel() {
        mViewModel = getActivityScopeViewModel(deSerializable());
    }

    public abstract int getVariableId();

    @LayoutRes
    public abstract int getLayoutId();

    public abstract void addBindingParam(DataBindingConfig dataBindingConfig);

    public void initView() {

    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO tip 1: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。

        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
        DataBindingConfig dataBindingConfig = new DataBindingConfig(getLayoutId(), getVariableId(), mViewModel);
        addBindingParam(dataBindingConfig);
        return dataBindingConfig;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(isTransparentStatusBar()) {
            BarUtils.transparentStatusBar(this);
        }
        BarUtils.setStatusBarColor(this, getStatusBarColor());
        BarUtils.setStatusBarLightMode(this, getStatusBarLightMode());
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(NetworkStateManager.getInstance());
        mHandler = new Handler(getMainLooper());
        mLoaddingDialog = new LoaddingDialog(this);
        //TODO tip 1: DataBinding 严格模式（详见 DataBindingActivity - - - - - ）：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
        mViewModel.getNetLoadChangeObservable().maskLoad.observe(this, (val) -> {
            LogUtils.INFO(val);
            if (val.isShow()) {
                showLoading();
            } else {
                finishLoading();
            }
        });
        mViewModel.getNetLoadChangeObservable().pageLoad.observe(this, (val) -> {
            if (val.isShow()) {
                pageLoading();
            } else {
                finishPageLoading();
            }
        });
        initView();
    }


    private Class<T> deSerializable() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<T>) parameterizedType.getActualTypeArguments()[0];
        }
        throw new RuntimeException();
    }

    private int loadMaskCount = 0;

    /**
     * 显示加载遮罩
     */
    protected void showLoading() {
        if (loadMaskCount < 0) {
            loadMaskCount = 0;
        }
        loadMaskCount++;
        post(showRunnable);
    }

    private Runnable dismissRunnable = new Runnable() {
        @Override
        public void run() {
            if (mLoaddingDialog != null && loadMaskCount <= 0) {
                loadMaskCount = 0;
                mLoaddingDialog.dissmiss();
            }
        }
    };

    private Runnable showRunnable = new Runnable() {
        @Override
        public void run() {
            if (mLoaddingDialog != null && !mLoaddingDialog.isShowing()) {
                mLoaddingDialog.show();
            }
        }
    };

    protected void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    protected void postDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }

    /**
     * 关闭加载遮罩
     */
    protected void finishLoading() {
        loadMaskCount--;
        postDelayed(dismissRunnable, 100);
    }

    /**
     * 分页时显示加载遮罩
     */
    protected void pageLoading() {

    }

    /**
     * 分页时关闭加载遮罩
     */
    protected void finishPageLoading() {

    }

    public boolean getStatusBarLightMode() {
        return true;
    }

    public int getStatusBarColor() {
        return Color.TRANSPARENT;
    }

    //TODO tip 2: Jetpack 通过 "工厂模式" 来实现 ViewModel 的作用域可控，
    //目前我们在项目中提供了 Application、Activity、Fragment 三个级别的作用域，
    //值得注意的是，通过不同作用域的 Provider 获得的 ViewModel 实例不是同一个，
    //所以如果 ViewModel 对状态信息的保留不符合预期，可以从这个角度出发去排查 是否眼前的 ViewModel 实例不是目标实例所致。

    //如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/6257931840

    protected <V extends ViewModel> V getActivityScopeViewModel(@NonNull Class<V> modelClass) {
        if (mActivityProvider == null) {
            mActivityProvider = new ViewModelProvider(this);
        }
        return mActivityProvider.get(modelClass);
    }

    protected <V extends ViewModel> V getApplicationScopeViewModel(@NonNull Class<V> modelClass) {
        if (mApplicationProvider == null) {
            mApplicationProvider = new ViewModelProvider((BaseApplication) this.getApplicationContext(),
                    getAppFactory(this));
        }
        return mApplicationProvider.get(modelClass);
    }

    private ViewModelProvider.Factory getAppFactory(Activity activity) {
        Application application = checkApplication(activity);
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application);
    }

    private Application checkApplication(Activity activity) {
        Application application = activity.getApplication();
        if (application == null) {
            throw new IllegalStateException("Your activity/fragment is not yet attached to "
                    + "Application. You can't request ViewModel before onCreate call.");
        }
        return application;
    }

    @Override
    public Resources getResources() {
        if (ScreenUtils.isPortrait()) {
            return AdaptScreenUtils.adaptWidth(super.getResources(), 360);
        } else {
            return AdaptScreenUtils.adaptHeight(super.getResources(), 640);
        }
    }

    protected void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected void openUrlInBrowser(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    protected void showLongToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    protected void showShortToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(int stringRes) {
        showLongToast(getApplicationContext().getString(stringRes));
    }

    protected void showShortToast(int stringRes) {
        showShortToast(getApplicationContext().getString(stringRes));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoaddingDialog != null && mLoaddingDialog.isShowing()) {
            loadMaskCount = 0;
            mLoaddingDialog.dissmiss();
        }
        mLoaddingDialog = null;
        showRunnable = null;
        dismissRunnable = null;
        mHandler = null;
        mViewModel = null;
        mActivityProvider = null;
        mApplicationProvider = null;
    }

    /**
     * 是否全屏应用
     * @return
     */
    public boolean isTransparentStatusBar() {
        return false;
    }
}
