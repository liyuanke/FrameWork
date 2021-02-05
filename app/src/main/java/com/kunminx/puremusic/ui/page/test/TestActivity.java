package com.kunminx.puremusic.ui.page.test;

import androidx.lifecycle.Observer;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.utils.LogUtils;
import com.kunminx.puremusic.BR;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.ui.state.TestViewModel;

public class TestActivity extends BaseActivity<TestViewModel> {

    @Override
    public int getVariableId() {
        return BR.vm;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void addBindingParam(DataBindingConfig dataBindingConfig) {

    }

    @Override
    public void initView() {
        mViewModel.userRequest.getLoginLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                LogUtils.INFO(s);
            }
        });
    }

}