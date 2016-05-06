package com.tj.matriximageandmovetext.util;

import android.graphics.Bitmap;

public class ImageInfo {

	private int imageHeight;
	private int imageWidth;
	
	public ImageInfo(){}
	
	public ImageInfo(int imageWidth, int imageHeight){
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
	
	//获取图片画的高度
	public static ImageInfo getBitmapScaleInfo(Bitmap sourceBitmap, ImageInfo modelImgInfo){
		
		//获取图片宽高，如果宽度或者高度比需求小 则进行放大 
		ImageInfo resizeImageInfo = new ImageInfo();
		
        final int height = sourceBitmap.getHeight();
        final int width = sourceBitmap.getWidth();
        
        float scaleRate = 0.01f;//缩放比率
        
        if(height < modelImgInfo.getImageHeight() || width < modelImgInfo.getImageWidth()){ 
        	
        	int tempShowW;
        	int tempShowH;
        	float maxV = 1 + scaleRate;
        	
        	tempShowW = (int)(width*maxV);
    		tempShowH = (int)(height*maxV);
        	
        	while(tempShowW < modelImgInfo.getImageWidth() || tempShowH < modelImgInfo.getImageHeight()){
        		
				maxV += scaleRate;
        		tempShowW = (int)(width*maxV);
        		tempShowH = (int)(height*maxV);
        		
//		        	System.out.println("big while : "+tempShowW + " " + tempShowH);
	        	
        	}
        	
        	resizeImageInfo.setImageHeight(tempShowH);
        	resizeImageInfo.setImageWidth(tempShowW);
        	
        }else{//图片的宽高都大于屏幕的宽高
        	
        	int tempShowW;
        	int tempShowH;
        	
			float maxV = 1 - scaleRate;
        	
        	tempShowW = (int)(width*maxV);
    		tempShowH = (int)(height*maxV);
        	
        	while(tempShowW > modelImgInfo.getImageWidth() && tempShowH > modelImgInfo.getImageHeight()){
        		
				maxV -= scaleRate;
        		
        		tempShowW = (int)(width*maxV);
        		tempShowH = (int)(height*maxV);
        		
//		        	System.out.println("small while : "+tempShowW + " " + tempShowH);
	        	
        	}
        	
			resizeImageInfo.setImageHeight((int) (height * (maxV + scaleRate)));
			resizeImageInfo.setImageWidth((int) (width * (maxV + scaleRate)));
        	
        }
        
//			System.out.println("final : " + ili.getImageWidth() + " " + ili.getImageHeight());
        
        return resizeImageInfo;
	}
	
}
