package com.kunminx.puremusic.ui.page.test;

import androidx.lifecycle.Observer;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.utils.LogUtils;
import com.kunminx.puremusic.BR;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.ui.state.TestViewModel;
import com.kunminx.puremusic.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class TestActivity2 extends BaseActivity<TestViewModel> {

    @Override
    public int getVariableId() {
        return BR.vm;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test2;
    }

    @Override
    public void addBindingParam(DataBindingConfig dataBindingConfig) {
        dataBindingConfig.addBindingParam(BR.click, new ClickProxy());
    }

    @Override
    public void initView() {
        mViewModel.userRequest.getLoginLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                LogUtils.INFO(s+"aaaa");
                LogUtils.INFO(getBinding().getRoot());
            }
        });
    }

    public class ClickProxy {
        public void onClick() {
            Map<String,Object> map = new HashMap<>();
            map.put("userMobile","18589044543");
            map.put("password", StringUtils.shaEncrypt("123456"));
            mViewModel.userRequest.login(map);
        }
    }
}