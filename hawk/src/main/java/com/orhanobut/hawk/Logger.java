package com.orhanobut.hawk;

import android.util.Log;

/**
 * @author Orhan Obut
 */
@SuppressWarnings("unused")
final class Logger {
    private LogLevel logLevel;

    Logger(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    private static final int CHUNK_SIZE = 4000;

    private static final String TAG = "Hawk";

    void d(String message) {
        log(Log.DEBUG, message);
    }

    void e(String message) {
        log(Log.ERROR, message);
    }

    void w(String message) {
        log(Log.WARN, message);
    }

    void i(String message) {
        log(Log.INFO, message);
    }

    void v(String message) {
        log(Log.VERBOSE, message);
    }

    void wtf(String message) {
        log(Log.ASSERT, message);
    }

    private void log(int logType, String message) {
        if (logLevel == LogLevel.NONE) {
            return;
        }
        int length = message.length();
        if (length <= CHUNK_SIZE) {
            logChunk(logType, message);
            return;
        }

        for (int i = 0; i < length; i += CHUNK_SIZE) {
            int end = Math.min(length, i + CHUNK_SIZE);
            logChunk(logType, message.substring(i, end));
        }
    }

    private void logChunk(int logType, String chunk) {
        switch (logType) {
            case Log.ERROR:
                Log.e(TAG, chunk);
                break;
            case Log.INFO:
                Log.i(TAG, chunk);
                break;
            case Log.VERBOSE:
                Log.v(TAG, chunk);
                break;
            case Log.WARN:
                Log.w(TAG, chunk);
                break;
            case Log.ASSERT:
                Log.wtf(TAG, chunk);
                break;
            case Log.DEBUG:
                // Fall through, log debug by default
            default:
                Log.d(TAG, chunk);
                break;
        }
    }
}
