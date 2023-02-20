package com.fongmi.android.tv;

import android.text.TextUtils;

import com.fongmi.android.tv.net.OkHttp;

import java.io.IOException;

import okhttp3.Request;

public class Github {

    public static final String A = "https://raw.githubusercontent.com/";
    public static final String B = "https://gh-proxy.com/";
    public static final String C = "https://ghproxy.com/";
    public static final String D = "https://raw.iqiq.io/";
    public static final String REPO = "zww0019/TV/";
    public static final String RELEASE = "release";
    public static final String DEV = "dev";

    private String proxy;

    private static class Loader {
        static volatile Github INSTANCE = new Github();
    }

    public static Github get() {
        return Loader.INSTANCE;
    }

    public Github() {
        check(A);
        check(B);
        check(C);
        check(D);
    }

    private void check(String url) {
        try {
            if (getProxy().length() > 0) return;
            int code = OkHttp.client(Constant.TIMEOUT_GITHUB).newCall(new Request.Builder().url(url).build()).execute().code();
            if (code == 200) setProxy(url);
        } catch (IOException ignored) {
        }
    }

    private void setProxy(String url) {
        this.proxy = url.equals(B) || url.equals(C) ? url + A + REPO : url + REPO;
    }

    private String getProxy() {
        return TextUtils.isEmpty(proxy) ? "" : proxy;
    }

    public String getReleasePath(String path) {
        return getProxy() + RELEASE + path;
    }

    public String getBranchPath(String branch, String path) {
        return getProxy() + branch + path;
    }
}
