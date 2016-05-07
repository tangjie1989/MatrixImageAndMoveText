package com.tj.matriximageandmovetext.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.UUID;

public class SaveImageToPhotoAlbumUtil {

    /**
     *
     * 版本低于6.0直接调用系统api
     * 版本大于或等于6.0需先手动保存图片到camera目录 然后调用SCAN_FILE intent
     */
    public static String insertImage(Context context, String sourcePath) {
        if (!TextUtils.isEmpty(sourcePath)) {
            File sourceFile = new File(sourcePath);
            if (sourceFile.exists()) {

                if (android.os.Build.VERSION.SDK_INT < 23){
                    return insertImageBySystemApi(context, sourcePath);
                }else{

                    String desFileSaveDirectoryPath = "";

                    File dcimDire = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    if (dcimDire != null && dcimDire.exists()){
                        File[] files = dcimDire.listFiles();
                        if (files != null && files.length > 0){
                            for (File file : files){
                                if (file != null && file.isDirectory()){
                                    String fileName = file.getName();
                                    if (!TextUtils.isEmpty(fileName)){
                                        if (fileName.equalsIgnoreCase("Camera")){
                                            desFileSaveDirectoryPath = file.getAbsolutePath();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //存在 dcim/camera
                    if (!TextUtils.isEmpty(desFileSaveDirectoryPath)){
                        String desFilePath = desFileSaveDirectoryPath + File.separator + UUID.randomUUID().toString() + ".jpg";

                        File desFile = new File(desFilePath);

                        if (generateDestinationFileDirectory(desFile)){
                            fileChannelCopy(sourceFile, desFile);
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + desFilePath)));
                            return desFilePath;
                        }
                    }else{
                        return insertImageBySystemApi(context, sourcePath);
                    }
                }
            }
        }
        return "";
    }

    /**
     * 通过系统api保存图片到本地
     * 6.0以下保存到 dcim/camera
     * 6.0或以上保存到 pictures
     */
    private static String insertImageBySystemApi(Context context, String sourcePath){
        try {
            Uri saveUri = Uri.parse(Images.Media.insertImage(context.getContentResolver(), sourcePath, "", ""));
            return getRealPathFromURI(context, saveUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取相册数据库新建照片真实保存路径
     */
    private static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null){
                int columnIndex = cursor.getColumnIndex(Images.Media.DATA);
                if (columnIndex >= 0) {
                    cursor.moveToFirst();
                    return cursor.getString(columnIndex);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }

    /**
     * 确保图片保存的目录存在
     */
    private static boolean generateDestinationFileDirectory(File desFile){
        File direc = desFile.getParentFile();
        return direc != null && (direc.exists() || (!direc.isDirectory() && direc.mkdirs()));
    }

    /**
     * 文件复制
     */
    private static void fileChannelCopy(File s, File t) {

        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);

            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道

            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fi != null){
                    fi.close();
                }
                if (in != null){
                    in.close();
                }
                if (fo != null){
                    fo.close();
                }
                if (out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
