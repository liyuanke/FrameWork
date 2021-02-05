package com.kunminx.puremusic.domain.request;

import com.kunminx.puremusic.data.retrofit.Http;
import com.kunminx.architecture.livedata.UIChangeObservable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class Request<T> {
    protected T mService;
    protected UIChangeObservable netLoadChangeObservable;

    public Request(UIChangeObservable netLoadChangeObservable) {
        this.mService = Http.getInstance().create(deSerializable());
        this.netLoadChangeObservable = netLoadChangeObservable;
    }

    private Class<T> deSerializable() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<T>) parameterizedType.getActualTypeArguments()[0];
        }
        throw new RuntimeException();
    }

    public void onDestroy() {
        mService = null;
        if (netLoadChangeObservable != null) {
            netLoadChangeObservable.maskLoad = null;
            netLoadChangeObservable.pageLoad = null;
            netLoadChangeObservable = null;
        }
    }
}
