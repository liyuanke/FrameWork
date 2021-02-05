package com.kunminx.architecture.utils;

import com.kunminx.architecture.BuildConfig;

import java.util.logging.Level;

public class LogUtils {
    private static final String TAG = "LogUtils";
    private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TAG);

    public static void INFO(String messge) {
        if (messge != null && BuildConfig.DEBUG) {
            logger.log(Level.INFO, messge);
        }
    }

    public static void INFO(int messge) {
        INFO(String.valueOf(messge));
    }

    public static void INFO(Object messge) {
        if (messge != null) {
            INFO(messge.toString());
        }
    }

    public static void WARNING(String messge) {
        if (messge != null && BuildConfig.DEBUG) {
            logger.log(Level.WARNING, messge);
        }
    }

    public static void WARNING(int messge) {
        WARNING(String.valueOf(messge));
    }

    public static void WARNING(Object messge) {
        if (messge != null) {
            WARNING(messge.toString());
        }
    }
}
