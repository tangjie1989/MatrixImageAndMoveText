package com.tj.matriximageandmovetext.base;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImageLoadingConfig {

	public static DisplayImageOptions generateDisplayImageOptions() {
		return new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.build();
	}

	public static DisplayImageOptions generateDisplayImageOptionsNoCatchDisc() {
		return new DisplayImageOptions.Builder()
				.cacheInMemory(false).cacheOnDisk(false)
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	public static DisplayImageOptions generateDisplayImageOptionsWithNotClear() {
		return new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(false).cacheOnDisk(true)
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.build();
	}
	
}
