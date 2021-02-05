package com.kunminx.puremusic.domain.subscriber;

import com.kunminx.architecture.bean.Mask;
import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.livedata.UIChangeObservable;
import com.kunminx.puremusic.AppManager;
import com.kunminx.puremusic.data.retrofit.ResponseThrowable;
import com.kunminx.architecture.utils.ToastUtils;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;


/**
 * 用于在Http请求开始时，自动回调开始请求
 * 在Http请求结束是，自动回调请求结束
 * 调用者自己对请求数据进行处理
 */
public abstract class CallSubscriber<T> extends DisposableObserver<T> {

    private UIChangeObservable uiChangeObservable;

    public CallSubscriber(UIChangeObservable uiChangeObservable) {
        this.uiChangeObservable = uiChangeObservable;
    }

    @Override
    public void onNext(@NonNull T t) {
        if (t != null && t instanceof DataResult) {
            if (((DataResult) t).isSuccess()) {
                onResult(t);
            } else {
                ToastUtils.showShort(((DataResult) t).getMessage());
            }
        } else {
            onResult(t);
        }
    }

    public abstract void onResult(@NonNull T t);

    @Override
    protected void onStart() {
        super.onStart();
        if (uiChangeObservable != null) {
            uiChangeObservable.maskLoad.setValue(new Mask(true));
        }
    }

    @Override
    public void onComplete() {
        if (uiChangeObservable != null) {
            uiChangeObservable.maskLoad.setValue(new Mask(false));
        }
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param throwable
     */
    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof ResponseThrowable) {
            ResponseThrowable ex = (ResponseThrowable) throwable;
            if (ex.code == 100) {//需要重新登录
                AppManager.getAppManager().toLogin();
            } else {
                ToastUtils.showShort(((ResponseThrowable) throwable).message);
            }
        }
        if (uiChangeObservable != null) {
            uiChangeObservable.maskLoad.setValue(new Mask(false));
        }
    }

}