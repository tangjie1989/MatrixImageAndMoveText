package com.tj.matriximageandmovetext.base;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImageLoadingConfig {

	public static void displayImage(String url, ImageView imageView,
			int drawableId) {

		ImageLoader.getInstance().displayImage(url, imageView,
				generateDisplayImageOptions(drawableId));

	}

	// 列表图片加载option
	public static DisplayImageOptions generateDisplayImageOptions(int drawableId) {
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(drawableId).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		return displayImageOptions;
	}

	// 大图加载option
	public static DisplayImageOptions generateDisplayImageOptions() {
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.build();

		return displayImageOptions;

	}

	// 图片上传部分 图片加载option
	public static DisplayImageOptions generateDisplayImageOptionsNoCatchDisc() {
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(false).cacheOnDisc(false)
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		return displayImageOptions;
	}
	
	// 大图加载option (供WhisperPublishActivityNew调用)
	public static DisplayImageOptions generateDisplayImageOptionsWithDefaultImg(
			int drawableId) {
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(drawableId).resetViewBeforeLoading(true)
				.cacheOnDisc(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.build();

		return displayImageOptions;

	}
	
	// 大图加载option (供WhisperPublishActivityNew调用)
	public static DisplayImageOptions generateDisplayImageOptionsWithNotClear() {
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(false).cacheOnDisc(true)
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.build();

		return displayImageOptions;

	}
	
	//启动图加载option
	public static DisplayImageOptions generateDisplayStartupImageOptions() {
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisc(false)
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.build();

		return displayImageOptions;

	}
}
