package com.kunminx.puremusic.domain.service;

import com.kunminx.architecture.data.response.DataResult;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserService {
    @Headers("Content-Type: application/json")
    @POST("/sv-user/app/login")
    Observable<DataResult> login(@Body Map params);
}
