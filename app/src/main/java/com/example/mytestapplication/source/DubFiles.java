package com.example.mytestapplication.source;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.Date;

/**
 * Created by dubo on 2017/11/6.
 */

public class DubFiles {
    /**
     * 获取拍照相片存储文件
     * @param context
     * @return
     */
    public static File createFile(Context context){
        File file;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(Environment.getExternalStorageDirectory() +File.separator + timeStamp+".jpg");
        }else{
            File cacheDir = context.getCacheDir();
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(cacheDir, timeStamp+".jpg");
        }
        return file;
    }

    public static File getSaveFile() {
        File sdCard = Environment.getExternalStorageDirectory();
        File cacheFolderPic = new File(sdCard, "CachePic");
        if(!cacheFolderPic.exists())cacheFolderPic.mkdirs();
        File cacheSavePic = new File(cacheFolderPic, "savePic"+System.currentTimeMillis()+".jpg");
        return cacheSavePic;
    }

    public static File getCropFile() {
        File sdCard = Environment.getExternalStorageDirectory();
        File cacheFolderPic = new File(sdCard, "CachePic");
        if(!cacheFolderPic.exists())cacheFolderPic.mkdirs();
        File cacheSavePic = new File(cacheFolderPic, "cropPic"+System.currentTimeMillis()+".jpg");
        return cacheSavePic;
    }

    /**
     * 打开相机
     * 兼容7.0
     */
    public static void startActionCapture(Activity activity, File file, int requestCode) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(activity, file));
        activity.startActivityForResult(intent, requestCode);
    }

    public static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.quality.cheching.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
