package com.example.mytestapplication.source;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * Created by Bob on 2017/8/28.
 */

public class DubNoNetWork {
    /**
     * 判断网络是否可用
     * @param context
     * @return true 可以用，flase不可以用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            @SuppressLint("MissingPermission") NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {// 当前网络是连接的
                    return true;// 当前所连接的网络可用
                }
            }
        }
        return false;
    }

    //判断扫描的结果是不是网址链接
    public static boolean isValidURI(String uri) {
        if (uri == null || uri.indexOf(' ') >= 0 || uri.indexOf('\n') >= 0) {
            return false;
        }
        String scheme = Uri.parse(uri).getScheme();
        if (scheme == null) {
            return false;
        }
        int period = uri.indexOf('.');
        if (period >= uri.length() - 2) {
            return false;
        }
        int colon = uri.indexOf(':');
        if (period < 0 && colon < 0) {
            return false;
        }
        if (colon >= 0) {
            if (period < 0 || period > colon) {
                for (int i = 0; i < colon; i++) {
                    char c = uri.charAt(i);
                    if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
                        return false;
                    }
                }
            } else {
                if (colon >= uri.length() - 2) {
                    return false;
                }
                for (int i = colon + 1; i < colon + 3; i++) {
                    char c = uri.charAt(i);
                    if (c < '0' || c > '9') {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
