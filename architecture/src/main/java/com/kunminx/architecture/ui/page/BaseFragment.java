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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.kunminx.architecture.BaseApplication;
import com.kunminx.architecture.ui.state.BaseViewModel;
import com.kunminx.architecture.widget.LoaddingDialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * Create by KunMinX at 19/7/11
 */
public abstract class BaseFragment<T extends BaseViewModel> extends DataBindingFragment {

    private ViewModelProvider mFragmentProvider;
    private ViewModelProvider mActivityProvider;
    private ViewModelProvider mApplicationProvider;

    private static final Handler HANDLER = new Handler();
    protected boolean mAnimationLoaded;
    protected T mViewModel;
    private LoaddingDialog mLoaddingDialog;
    protected Handler mHandler;

    public void initView() {

    }

    @CallSuper
    @Override
    protected void initViewModel() {
        mViewModel = getActivityScopeViewModel(deSerializable());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(getActivity().getMainLooper());
        mLoaddingDialog = new LoaddingDialog(getContext());
        //TODO tip 1: DataBinding 严格模式（详见 DataBindingActivity - - - - - ）：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
        mViewModel.getNetLoadChangeObservable().maskLoad.observe(this, (val) -> {
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
    }

    private Class<T> deSerializable() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<T>) parameterizedType.getActualTypeArguments()[0];
        }
        throw new RuntimeException();
    }

    public abstract int getVariableId();

    @LayoutRes
    public abstract int getLayoutId();

    public abstract void addBindingParam(DataBindingConfig dataBindingConfig);

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

    private int loadMaskCount = 0;

    private void showLoading() {
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

    private void finishLoading() {
        loadMaskCount--;
        postDelayed(dismissRunnable, 100);
    }

    private void pageLoading() {

    }

    private void finishPageLoading() {

    }


    //TODO tip 1: DataBinding 严格模式（详见 DataBindingFragment - - - - - ）：
    // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
    // 通过这样的方式，来彻底解决 视图调用的一致性问题，
    // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。

    // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

    //TODO tip 2: Jetpack 通过 "工厂模式" 来实现 ViewModel 的作用域可控，
    //目前我们在项目中提供了 Application、Activity、Fragment 三个级别的作用域，
    //值得注意的是，通过不同作用域的 Provider 获得的 ViewModel 实例不是同一个，
    //所以如果 ViewModel 对状态信息的保留不符合预期，可以从这个角度出发去排查 是否眼前的 ViewModel 实例不是目标实例所致。

    //如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/6257931840

    protected <T extends ViewModel> T
    getFragmentScopeViewModel(@NonNull Class<T> modelClass) {
        if (mFragmentProvider == null) {
            mFragmentProvider = new ViewModelProvider(this);
        }
        return mFragmentProvider.get(modelClass);
    }

    protected <T extends ViewModel> T
    getActivityScopeViewModel(@NonNull Class<T> modelClass) {
        if (mActivityProvider == null) {
            mActivityProvider = new ViewModelProvider(mActivity);
        }
        return mActivityProvider.get(modelClass);
    }

    protected <T extends ViewModel> T
    getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
        if (mApplicationProvider == null) {
            mApplicationProvider = new ViewModelProvider(
                    (BaseApplication) mActivity.getApplicationContext(), getApplicationFactory(mActivity));
        }
        return mApplicationProvider.get(modelClass);
    }

    private ViewModelProvider.Factory getApplicationFactory(Activity activity) {
        checkActivity(this);
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

    private void checkActivity(Fragment fragment) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            throw new IllegalStateException("Can't create ViewModelProvider for detached fragment");
        }
    }

    protected NavController nav() {
        return NavHostFragment.findNavController(this);
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //TODO 错开动画转场与 UI 刷新的时机，避免掉帧卡顿的现象
        HANDLER.postDelayed(() -> {
            if (!mAnimationLoaded) {
                mAnimationLoaded = true;
                loadInitData();
            }
        }, 280);
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    protected void loadInitData() {

    }

    protected void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected void openUrlInBrowser(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    protected void showLongToast(String text) {
        Toast.makeText(mActivity.getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    protected void showShortToast(String text) {
        Toast.makeText(mActivity.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(int stringRes) {
        showLongToast(mActivity.getApplicationContext().getString(stringRes));
    }

    protected void showShortToast(int stringRes) {
        showShortToast(mActivity.getApplicationContext().getString(stringRes));
    }


    /**
     * 控件是否初始化完成
     */
    protected boolean isViewCreated;
    /**
     * 是否已获取数据
     */
    protected boolean isLoadData;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isViewCreated && !isLoadData) {
            isLoadData = true;
            initView();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && !isLoadData) {
            isLoadData = true;
            initView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoaddingDialog != null && mLoaddingDialog.isShowing()) {
            loadMaskCount = 0;
            mLoaddingDialog.dissmiss();
        }
        mLoaddingDialog = null;
    }
}
