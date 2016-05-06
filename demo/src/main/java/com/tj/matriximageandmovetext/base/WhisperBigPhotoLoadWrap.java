package com.tj.matriximageandmovetext.base;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.tj.matriximageandmovetext.R;
import com.tj.matriximageandmovetext.widget.ProgressWheel;

public class WhisperBigPhotoLoadWrap {
	
	public WhisperBigPhotoLoadWrap(ImageView whisperPhoto, String picUrl,
			DisplayImageOptions whisperBigPhotoOptions,
			int viewPagerCurPostion,
			WhisperBigPhotoLoadWrapDelegate whisperBigPhotoLoadWrapDelegate) {
		super();
		this.whisperPhoto = whisperPhoto;
		this.picUrl = picUrl;
		this.whisperBigPhotoOptions = whisperBigPhotoOptions;
		this.viewPagerCurPostion = viewPagerCurPostion;
		this.whisperBigPhotoLoadWrapDelegate = whisperBigPhotoLoadWrapDelegate;
	}

	private ImageView whisperPhoto;
	private String picUrl;
	private DisplayImageOptions whisperBigPhotoOptions;
	private int viewPagerCurPostion;
	private WhisperBigPhotoLoadWrapDelegate whisperBigPhotoLoadWrapDelegate;
	
	public interface WhisperBigPhotoLoadWrapDelegate{
		
		void loadImgComplete(int viewPagerCurPostion);
		
	}
	
	private ProgressWheel progressWheel;
	
	public void initLoadProgressWheel(View containerView) {
		progressWheel = (ProgressWheel)containerView.findViewById(R.id.whisper_publish_photo_load_progress);
		progressWheel.setProgress(0);
		progressWheel.setText("0%");
	}

	public void loadWhisperBigPhoto() {
		
		ImageLoader.getInstance().displayImage(picUrl, whisperPhoto, whisperBigPhotoOptions, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				progressWheel.setText("0%");
				progressWheel.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				progressWheel.setVisibility(View.GONE);
				whisperBigPhotoLoadWrapDelegate.loadImgComplete(viewPagerCurPostion);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
			}
		}, new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view,
					int current, int total) {

				int picLoadPro = (int) (current * 360.0 / total);

				progressWheel.setProgress(picLoadPro);
				progressWheel.setText((int) (picLoadPro * 100.0 / 360.0)+ "%");

			}
		});
		
	}
	
}
