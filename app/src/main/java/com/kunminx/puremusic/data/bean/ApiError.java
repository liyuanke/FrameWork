package com.kunminx.puremusic.data.bean;

public class ApiError {
    private int code;
    private String msg;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg != null ? msg : message;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
