package com.tj.matriximageandmovetext;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

public class MartixApplication extends Application {
	
	private static MartixApplication instance;
	
	private MartixLocalStorageUtil miYouQuanLocalStorageUtil;

	public MartixLocalStorageUtil getMartixLocalStorageUtil() {
		return miYouQuanLocalStorageUtil;
	}

	public static MartixApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		
		super.onCreate();
		
		instance = this;
		
		miYouQuanLocalStorageUtil = new MartixLocalStorageUtil();
		miYouQuanLocalStorageUtil.initLocalDir(this);

		initImageLoader(getApplicationContext());
	}

	public void initImageLoader(Context context) {
		
		File cacheDir = new File(miYouQuanLocalStorageUtil.getImageCacheAbsoluteDir());
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCache(new UnlimitedDiskCache(cacheDir))
//				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}

}
