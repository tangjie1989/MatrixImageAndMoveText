package com.tj.matriximageandmovetext.util;

import android.graphics.Bitmap;

public class ImageInfo {

    private int imageHeight;
    private int imageWidth;

    public ImageInfo() {}

    public ImageInfo(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "imageHeight=" + imageHeight +
                ", imageWidth=" + imageWidth +
                '}';
    }

    /**
     * 获取最接近屏幕宽高的图片尺寸
     */
    public ImageInfo getBitmapScaleInfo(Bitmap sourceBitmap) {

        //获取图片宽高，如果宽度或者高度比需求小 则进行放大
        ImageInfo resizeImageInfo = new ImageInfo();

        int sourceBitmapHeight = sourceBitmap.getHeight();
        int sourceBitmapWidth = sourceBitmap.getWidth();

        float heightRatio = imageHeight * 1.0f / sourceBitmapHeight;
        float widthRatio = imageWidth * 1.0f / sourceBitmapWidth;

        float maxRatio = heightRatio > widthRatio ? heightRatio : widthRatio;
        resizeImageInfo.setImageHeight((int) (sourceBitmapHeight * maxRatio));
        resizeImageInfo.setImageWidth((int) (sourceBitmapWidth * maxRatio));

        return resizeImageInfo;
    }

}
