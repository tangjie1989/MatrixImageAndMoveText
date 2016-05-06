package com.tj.matriximageandmovetext;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class MartixLocalStorageUtil {

	// SD卡文件根目录
	private static String BASE_DIR = "Martix";
	
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

		long availableSDCardSpace = getExternalStorageSpace();// 获取SD卡可用空间
		
		String sdcardBasePath;
		
		if (availableSDCardSpace != -1L) {// 如果存在SD卡
			sdcardBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + BASE_DIR;
		} else if (getInternalStorageSpace() != -1L) {
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
			basePath.mkdir();
		}

		// 图片缓存目录
		File imgCachePath = new File(getImageCacheAbsoluteDir());
		if (!imgCachePath.exists()) {
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

	/**
	 * 获取SD卡可用空间
	 *
	 * @return availableSDCardSpace 可用空间(MB)。-1L:没有SD卡
	 */
	private long getExternalStorageSpace() {
		long availableSDCardSpace = -1L;
		// 存在SD卡
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			StatFs sf = new StatFs(Environment.getExternalStorageDirectory()
					.getPath());
			long blockSize = sf.getBlockSize();// 块大小,单位byte
			long availCount = sf.getAvailableBlocks();// 可用块数量
			availableSDCardSpace = availCount * blockSize / 1024 / 1024;// 可用SD卡空间，单位MB
		}

		return availableSDCardSpace;
	}

	/**
	 * 获取机器内部可用空间
	 *
	 * @return availableSDCardSpace 可用空间(MB)。-1L:没有SD卡
	 */
	private long getInternalStorageSpace() {
		long availableInternalSpace = -1L;

		StatFs sf = new StatFs(Environment.getDataDirectory().getPath());
		long blockSize = sf.getBlockSize();// 块大小,单位byte
		long availCount = sf.getAvailableBlocks();// 可用块数量
		availableInternalSpace = availCount * blockSize / 1024 / 1024;// 可用SD卡空间，单位MB

		return availableInternalSpace;
	}
	
}
