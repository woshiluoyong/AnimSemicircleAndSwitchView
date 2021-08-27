package com.example.mytestapplication.source;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 编辑框控件工具类
 */
public class DubEditTextUtils {

    //设置编辑框失去焦点和获取焦点
    public static void initEditText(final EditText editText) {
        if (editText == null)
            return;
        editText.setFocusable(false);//失去焦点
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setFocusable(true);//设置输入框可聚集
                editText.setFocusableInTouchMode(true);//设置触摸聚焦
                editText.requestFocus();//请求焦点
                editText.findFocus();//获取焦点
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    //设置输入框不可聚焦，即失去焦点和光标
                    editText.setFocusable(false);
                }
                return false;
            }
        });
    }

    //设置编辑框失去焦点和获取焦点
    public static void initEditTextShow(final EditText editText) {
        if (editText == null)
            return;
        editText.setFocusable(true);//得到焦点
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setFocusable(true);//设置输入框可聚集
                editText.setFocusableInTouchMode(true);//设置触摸聚焦
                editText.requestFocus();//请求焦点
                editText.findFocus();//获取焦点
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    //设置输入框不可聚焦，即失去焦点和光标
                    editText.setFocusable(false);
                }
                return false;
            }
        });
    }

}
