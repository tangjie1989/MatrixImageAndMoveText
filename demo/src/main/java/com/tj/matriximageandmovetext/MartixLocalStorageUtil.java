package com.tj.matriximageandmovetext;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.tj.matriximageandmovetext.util.DeviceInfoUtil;

public class MartixLocalStorageUtil {

	// SD卡文件根目录
	private static String BASE_DIR = "MiYouQuan";
	
	// 缓存目录
	private static final String FILE_CACHE_DIR = "caches";
	private static final String IMAGE_CACHE_DIR = "temp";

	// sd卡根目录
	private String sdcardCacheBaseAbsolutePath;

	private String imageCacheAbsoluteDir;// 图片缓存目录
	private String fileCacheAbsoluteDir;// 其他文件缓存目录

	// 照片上传临时路径
	private String uploadUserPhotoTempFilePath;// 未处理的jpg
	private String uploadUserPhotoHandledFilePath;// 已处理待上传的jpg

	public String getSdcardCacheBaseAbsolutePath() {
		return sdcardCacheBaseAbsolutePath;
	}

	public void setSdcardCacheBaseAbsolutePath(String sdcardCacheBaseAbsolutePath) {
		this.sdcardCacheBaseAbsolutePath = sdcardCacheBaseAbsolutePath;
	}

	public String getImageCacheAbsoluteDir() {
		return imageCacheAbsoluteDir;
	}

	public void setImageCacheAbsoluteDir(String imageCacheAbsoluteDir) {
		this.imageCacheAbsoluteDir = imageCacheAbsoluteDir;
	}

	public String getFileCacheAbsoluteDir() {
		return fileCacheAbsoluteDir;
	}

	public void setFileCacheAbsoluteDir(String fileCacheAbsoluteDir) {
		this.fileCacheAbsoluteDir = fileCacheAbsoluteDir;
	}

	public String getUploadUserPhotoTempFilePath() {
		return uploadUserPhotoTempFilePath;
	}

	public void setUploadUserPhotoTempFilePath(
			String uploadUserPhotoTempFilePath) {
		this.uploadUserPhotoTempFilePath = uploadUserPhotoTempFilePath;
	}

	public String getUploadUserPhotoHandledFilePath() {
		return uploadUserPhotoHandledFilePath;
	}

	public void setUploadUserPhotoHandledFilePath(
			String uploadUserPhotoHandledFilePath) {
		this.uploadUserPhotoHandledFilePath = uploadUserPhotoHandledFilePath;
	}

	public void initLocalDir(Context context) {

		long availableSDCardSpace = DeviceInfoUtil.getExternalStorageSpace();// 获取SD卡可用空间
		
		String sdcardBasePath;
		
		if (availableSDCardSpace != -1L) {// 如果存在SD卡
			sdcardBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + BASE_DIR;
		} else if (DeviceInfoUtil.getInternalStorageSpace() != -1L) {
			sdcardBasePath = context.getFilesDir().getPath() + File.separator + BASE_DIR;
		} else {// sd卡不存在
			// 没有可写入位置
			return;
		}
		
		setSdcardCacheBaseAbsolutePath(sdcardBasePath);
		
		// 图片缓存目录
		setImageCacheAbsoluteDir(getSdcardCacheBaseAbsolutePath() + File.separator + IMAGE_CACHE_DIR);

		// 其他文件缓存目录
		String photoUploadCacheDire = getSdcardCacheBaseAbsolutePath() + File.separator + FILE_CACHE_DIR;
		
		setFileCacheAbsoluteDir(photoUploadCacheDire);
		
		// 初始化根目录
		File basePath = new File(getSdcardCacheBaseAbsolutePath());
		if (!basePath.exists()) {
			Log.d("Martix", "init basePath = " + getSdcardCacheBaseAbsolutePath());
			Log.d("Martix", "init status " + basePath.mkdir());
		}

		// 图片缓存目录
		File imgCachePath = new File(getImageCacheAbsoluteDir());
		if (!imgCachePath.exists()) {
			Log.d("Martix", "init imgCachePath = " + getImageCacheAbsoluteDir());
			imgCachePath.mkdir();
		}

		// 初始化照片上传缓存目录
		File fileCachePath = new File(photoUploadCacheDire);
		if (!fileCachePath.exists()) {
			fileCachePath.mkdir();
		}

		setUploadUserPhotoHandledFilePath(photoUploadCacheDire + File.separator + "handled.jpg");
		setUploadUserPhotoTempFilePath(photoUploadCacheDire + File.separator + "unhandled.jpg");
		
	}
	
}
