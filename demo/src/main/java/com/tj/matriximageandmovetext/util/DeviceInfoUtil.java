package com.tj.matriximageandmovetext.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

/**
 * 获取设备信息
 * 
 */

public class DeviceInfoUtil {

	/**
	 * 获取可用存储空间大小 若存在SD卡则返回SD卡剩余空间大小 否则返回手机内存剩余空间大小
	 */
	public static long getAvailableStorageSpace() {
		long externalSpace = getExternalStorageSpace();
		if (externalSpace == -1L) {
			return getInternalStorageSpace();
		}

		return externalSpace;
	}

	/**
	 * 获取SD卡可用空间
	 * 
	 * @return availableSDCardSpace 可用空间(MB)。-1L:没有SD卡
	 */
	public static long getExternalStorageSpace() {
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
	public static long getInternalStorageSpace() {
		long availableInternalSpace = -1L;

		StatFs sf = new StatFs(Environment.getDataDirectory().getPath());
		long blockSize = sf.getBlockSize();// 块大小,单位byte
		long availCount = sf.getAvailableBlocks();// 可用块数量
		availableInternalSpace = availCount * blockSize / 1024 / 1024;// 可用SD卡空间，单位MB

		return availableInternalSpace;
	}

	/**
	 * 获取SD卡总空间
	 * 
	 * @return availableSDCardSpace 总空间(MB)。-1L:没有SD卡
	 */
	public static long getExternalStorageTotalSpace() {
		long availableSDCardSpace = -1L;
		// 存在SD卡
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			StatFs sf = new StatFs(Environment.getExternalStorageDirectory()
					.getPath());
			long blockSize = sf.getBlockSize();// 块大小,单位byte
			long blockCount = sf.getBlockCount();// 块总数量
			availableSDCardSpace = blockCount * blockSize / 1024 / 1024;// 总SD卡空间，单位MB
		}

		return availableSDCardSpace;
	}

	/**
	 * 获得设备屏幕密度
	 */
	public static float getScreenDensity(Context context) {
		DisplayMetrics metrics = context.getApplicationContext().getResources()
				.getDisplayMetrics();
		return metrics.density;
	}

	private static DisplayMetrics getDisplayMetrics(Context ctx){
		Resources re = ctx.getResources();
		return re.getDisplayMetrics();
	}

	public static int getScreenWidth(Context ctx){
        return getDisplayMetrics(ctx).widthPixels;
	}

	public static int getScreenHeight(Context ctx){
        return getDisplayMetrics(ctx).heightPixels;
	}

	public static int getStatusBarHeight(Context ctx){
		int statusBarHeight = 38;
		try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int height = Integer.parseInt(field.get(object).toString());
            statusBarHeight = ctx.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return statusBarHeight;
	}


}
