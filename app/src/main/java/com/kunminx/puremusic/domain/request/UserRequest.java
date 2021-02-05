package com.kunminx.puremusic.domain.request;

import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.puremusic.domain.service.UserService;
import com.kunminx.puremusic.domain.subscriber.CallSubscriber;
import com.kunminx.architecture.livedata.UIChangeObservable;
import com.kunminx.puremusic.utils.RxUtils;

import java.util.Map;

import io.reactivex.annotations.NonNull;

public class UserRequest extends Request<UserService> {
    private MutableLiveData<String> loginLiveData = new MutableLiveData<>();

    public MutableLiveData<String> getLoginLiveData() {
        return loginLiveData;
    }

    public UserRequest(UIChangeObservable netLoadChangeObservable) {
        super(netLoadChangeObservable);
    }

    public void login(Map params) {
        mService.login(params)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new CallSubscriber<DataResult<String>>(netLoadChangeObservable) {

                    @Override
                    public void onResult(@NonNull DataResult<String> result) {
                        loginLiveData.postValue(result.getData());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loginLiveData = null;
    }
}
