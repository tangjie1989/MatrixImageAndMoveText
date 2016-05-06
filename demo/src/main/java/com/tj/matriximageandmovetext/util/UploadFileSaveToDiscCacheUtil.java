package com.tj.matriximageandmovetext.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.tj.matriximageandmovetext.base.ImageLoadingConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class UploadFileSaveToDiscCacheUtil {
	
	public UploadFileSaveToDiscCacheUtil(String bigImgPath, String smallImgPath) {
		super();
		this.bigImgPath = bigImgPath;
		this.smallImgPath = smallImgPath;
	}

	private String bigImgPath;
	private String smallImgPath;

	public String getBigImgPath() {
		return bigImgPath;
	}

	public void setBigImgPath(String bigImgPath) {
		this.bigImgPath = bigImgPath;
	}

	public String getSmallImgPath() {
		return smallImgPath;
	}

	public void setSmallImgPath(String smallImgPath) {
		this.smallImgPath = smallImgPath;
	}

	//保存本地
	public void saveBitmapToLocalFile(String filePath , Bitmap sourceBitmap , int compressDegree , boolean isSourceBitmapRecycle){
		
		FileOutputStream stream = null;
		
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		
		try {
			stream = new FileOutputStream(file);
			if(sourceBitmap != null && !sourceBitmap.isRecycled()){
				sourceBitmap.compress(CompressFormat.JPEG, compressDegree, stream);//大小压缩
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stream != null) {
					stream.flush();
					stream.close();
				}
				
				if(isSourceBitmapRecycle && sourceBitmap != null && !sourceBitmap.isRecycled()){
					sourceBitmap.recycle();
					sourceBitmap = null;
				}
			} catch (IOException e2) {
			}
		}
		
	}
	
	public void generateBigWhisperToLocalDiscCache(String url){
		
		if(!TextUtils.isEmpty(url)){
			if(url.contains("600_600")){
				String saveUrl = url.replace("600_600", "640_1136");
				copyFileToDiscCache(saveUrl, getBigImgPath());//whisperPublishNeedParams.getSaveWaitUploadPicPath()
			}
		}
	}
	
	public void generateSmallWhisperToLocalDiscCache(String url){
		
		if(!TextUtils.isEmpty(url)){
			if(url.contains("600_600")){
				
				String saveUrl = url.replace("600_600", "300_300");
				ImageInfo imageInfo = getSmallWhisperImageInfoFromFile(getBigImgPath());//whisperPublishNeedParams.getSaveWaitUploadPicPath()
				
				ImageSize targetSize = new ImageSize(imageInfo.getImageWidth(), imageInfo.getImageHeight()); // result Bitmap will be fit to this size
				Bitmap showImg = ImageLoader.getInstance().loadImageSync("file://"+ getBigImgPath(), targetSize, ImageLoadingConfig.generateDisplayImageOptionsNoCatchDisc());//whisperPublishNeedParams.getSaveWaitUploadPicPath()
				
				if(showImg != null && !showImg.isRecycled()){
					
					//生成压缩后的whisper小图
					saveBitmapToLocalFile(getSmallImgPath(), showImg, 90, true);//whisperPublishNeedParams.getSaveTempPicPath()
					
					copyFileToDiscCache(saveUrl, getSmallImgPath());//whisperPublishNeedParams.getSaveTempPicPath()
				}
			}
		}
		
	}
	
	public void generateBigUserPhotoToLocalDiscCache(String url){
		if(!TextUtils.isEmpty(url)){
			copyFileToDiscCache(url, getBigImgPath());
		}
	}
	
	public void generateSmallUserPhotoToLocalDiscCache(String url){
		
		if(!TextUtils.isEmpty(url)){
			
			ImageInfo imageInfo = getSmallUserPhotoInfoFromFile(getBigImgPath());
			
			ImageSize targetSize = new ImageSize(imageInfo.getImageWidth(), imageInfo.getImageHeight()); // result Bitmap will be fit to this size
			Bitmap showImg = ImageLoader.getInstance().loadImageSync("file://"+ getBigImgPath(), targetSize, ImageLoadingConfig.generateDisplayImageOptionsNoCatchDisc());
			
			if(showImg != null && !showImg.isRecycled()){
				
				//生成压缩后的whisper小图
				saveBitmapToLocalFile(getSmallImgPath(), showImg, 90, true);
				
				copyFileToDiscCache(url, getSmallImgPath());
			}
		}
	}
	
	private ImageInfo getSmallUserPhotoInfoFromFile(String filePath) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        
        return new ImageInfo(options.outWidth/3, options.outHeight/3);
    }

	private ImageInfo getSmallWhisperImageInfoFromFile(String filePath) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        
        return new ImageInfo(options.outWidth/3, options.outHeight/3);
    }
	
	private void copyFileToDiscCache(String url, String needSaveFilePath){
		
		File fileInDiscCache = ImageLoader.getInstance().getDiscCache().get(url);
		if(fileInDiscCache != null){
			
			if(fileInDiscCache.exists()){
				fileInDiscCache.delete();
			}
			
			try {
				copyFile(new File(needSaveFilePath),fileInDiscCache);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void copyFile(File sourceFile, File destFile) throws IOException {
		
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;
	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();

	        // previous code: destination.transferFrom(source, 0, source.size());
	        // to avoid infinite loops, should be:
	        long count = 0;
	        long size = source.size();              
	        while((count += destination.transferFrom(source, count, size-count))<size);
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}
	
}
