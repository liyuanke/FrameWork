package com.kunminx.puremusic.data.retrofit;

import android.net.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;
import com.kunminx.puremusic.data.bean.ApiError;
import com.kunminx.puremusic.utils.StringUtils;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;

import retrofit2.HttpException;
import retrofit2.Response;


/**
 * Created by goldze on 2017/5/11.
 */
public class ExceptionHandle {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final Gson gson = new Gson();

    public static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
        if (e != null && e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, ERROR.HTTP_ERROR);
            Response response = httpException.response();
            if (response != null && response.errorBody() != null) {
                try {
                    String errorMsg = response.errorBody().string();
                    if (StringUtils.isJson(errorMsg)) {
                        ApiError error = gson.fromJson(errorMsg, ApiError.class);
                        ex.message = error.getMsg();
                        ex.code = error.getCode();
                    } else {
                        ex.message = errorMsg;
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (Exception c) {
                    c.printStackTrace();
                }
            }
            if (ex.message == null) {
                switch (httpException.code()) {
                    case UNAUTHORIZED:
                        ex.message = "操作未授权";
                        break;
                    case FORBIDDEN:
                        ex.message = "请求被拒绝";
                        break;
                    case NOT_FOUND:
                        ex.message = "资源不存在";
                        break;
                    case REQUEST_TIMEOUT:
                        ex.message = "服务器执行超时";
                        break;
                    case INTERNAL_SERVER_ERROR:
                        ex.message = "服务器内部错误";
                        break;
                    case SERVICE_UNAVAILABLE:
                        ex.message = "服务器不可用";
                        break;
                    default:
                        ex.message = "网络错误";
                        break;
                }
            }
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException || e instanceof MalformedJsonException) {
            ex = new ResponseThrowable(e, ERROR.PARSE_ERROR);
            ex.message = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseThrowable(e, ERROR.NETWORD_ERROR);
            ex.message = "连接失败";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLException) {
            ex = new ResponseThrowable(e, ERROR.SSL_ERROR);
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "连接超时";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "连接超时";
            return ex;
        } else if (e instanceof java.net.UnknownHostException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "主机地址未知";
            return ex;
        } else {
            ex = new ResponseThrowable(e, ERROR.UNKNOWN);
            ex.message = "未知错误";
            return ex;
        }
    }


    /**
     * 约定异常 这个具体规则需要与服务端或者领导商讨定义
     */
    class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;
    }

}

