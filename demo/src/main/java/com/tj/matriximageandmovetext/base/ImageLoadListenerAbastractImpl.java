package com.tj.matriximageandmovetext.base;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageLoadListenerAbastractImpl implements ImageLoadingListener {

	@Override
	public void onLoadingStarted(String imageUri, View view) {

	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {

	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {

	}

}
