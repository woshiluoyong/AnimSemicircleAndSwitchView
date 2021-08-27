package com.example.mytestapplication.source;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class DubDialogUtils {

    /**
     * 确定 要执行的对话框
     */
    public static AlertDialog showOklDialog(Context context, String message) {
        return showOklDialog(context, message, null);
    }

    public static AlertDialog showOklDialog(Context context, String message, DialogInterface.OnClickListener okListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        //builder.setMessage(Html.fromHtml(message));
        builder.setCancelable(false);//外部不可点击
        builder.setPositiveButton("确定", okListener);
        AlertDialog alertDialog = builder.create();
        try{
            alertDialog.show();
        }catch (Exception e){
            Log.e("=======","Dialog被捕获了");
        }
        return alertDialog;
    }

    /**
     * 确定要执行的对话框
     * or
     * 取消要执行的对话框
     */
    public static AlertDialog showNormalDialog(Context context, String message, DialogInterface.OnClickListener listener) {
        return showNormalDialog(context, message, listener, null);
    }

    public static AlertDialog showNormalDialog(Context context, String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        //builder.setMessage(Html.fromHtml(message));
        builder.setCancelable(true);
        builder.setPositiveButton("确定", okListener);
        builder.setNegativeButton("取消", cancelListener);
        AlertDialog alertDialog = builder.create();
        try{
            alertDialog.show();
        }catch (Exception e){
            Log.e("=======","Dialog被捕获了");
        }
        return alertDialog;
    }

    public static AlertDialog showNormalDialog(Context context, boolean cancleable,String message, String positiveText,DialogInterface.OnClickListener okListener, String NegativeText, DialogInterface.OnClickListener cancelListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        //builder.setMessage(Html.fromHtml(message));
        builder.setCancelable(cancleable);
        builder.setPositiveButton(positiveText, okListener);
        builder.setNegativeButton(NegativeText, cancelListener);
        AlertDialog alertDialog = builder.create();
        try{
            alertDialog.show();
        }catch (Exception e){
            Log.e("=======","Dialog被捕获了");
        }
        return alertDialog;
    }

    public static AlertDialog showNormalDialog(Context context,boolean cancleable, String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        //builder.setMessage(Html.fromHtml(message));
        builder.setCancelable(cancleable);
        builder.setPositiveButton("确定", okListener);
        builder.setNegativeButton("取消", cancelListener);
        AlertDialog alertDialog = builder.create();
        try{
            alertDialog.show();
        }catch (Exception e){
            Log.e("=======","Dialog被捕获了");
        }
        return alertDialog;
    }

    public static AlertDialog showNormalDialog(Context context,boolean cancleable, String message, DialogInterface.OnClickListener okListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        //builder.setMessage(Html.fromHtml(message));
        builder.setCancelable(cancleable);
        builder.setPositiveButton("确定", okListener);
        AlertDialog alertDialog = builder.create();
        try{
            alertDialog.show();
        }catch (Exception e){
            Log.e("=======","Dialog被捕获了");
        }
        return alertDialog;
    }

    public static AlertDialog showOnlyOkDialog(Context context, String message, DialogInterface.OnClickListener okListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        //builder.setMessage(Html.fromHtml(message));
        builder.setCancelable(false);
        builder.setPositiveButton("确定", okListener);
        AlertDialog alertDialog = builder.create();
        try{
            alertDialog.show();
        }catch (Exception e){
            Log.e("=======","Dialog被捕获了");
        }
        return alertDialog;
    }

    /**
     * 自定义文字
     */
    public static AlertDialog showCustomTextlDialog(Context context, String message,String okText, DialogInterface.OnClickListener okListener,String cancelText, DialogInterface.OnClickListener cancelListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        //builder.setMessage(Html.fromHtml(message));
        builder.setCancelable(true);
        builder.setPositiveButton(okText, okListener);
        builder.setNegativeButton(cancelText, cancelListener);
        AlertDialog alertDialog = builder.create();
        try{
            alertDialog.show();
        }catch (Exception e){
            Log.e("=======","Dialog被捕获了");
        }
        return alertDialog;
    }

    //关毕APP
    public static AlertDialog showOnelDialogKillApp(Context context, String message, DialogInterface.OnClickListener okListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        //builder.setMessage(Html.fromHtml(message));
        builder.setCancelable(false);//外部不可点击
        builder.setPositiveButton("确定", okListener);
        AlertDialog alertDialog = builder.create();
        try{
            alertDialog.show();
        }catch (Exception e){
            Log.e("=======","Dialog有被捕获了");
        }
        return alertDialog;
    }

}


