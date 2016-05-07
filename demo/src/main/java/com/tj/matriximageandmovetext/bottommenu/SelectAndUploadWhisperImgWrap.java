package com.tj.matriximageandmovetext.bottommenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.tj.matriximageandmovetext.R;
import com.tj.matriximageandmovetext.util.SaveImageToPhotoAlbumUtil;
import com.tj.matriximageandmovetext.vo.WhisperPublishNeedParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SelectAndUploadWhisperImgWrap {
	
	public SelectAndUploadWhisperImgWrap(
			WhisperPublishNeedParams whisperPublishNeedParams,
			Activity activity,
			SelectAndUploadWhisperImgWrapDelegate selectAndUploadWhisperImgWrapDelegate) {
		super();
		this.whisperPublishNeedParams = whisperPublishNeedParams;
		this.activity = activity;
		this.selectAndUploadWhisperImgWrapDelegate = selectAndUploadWhisperImgWrapDelegate;
	}

	private WhisperPublishNeedParams whisperPublishNeedParams;
	private Activity activity;
	private SelectAndUploadWhisperImgWrapDelegate selectAndUploadWhisperImgWrapDelegate;
	
	public interface SelectAndUploadWhisperImgWrapDelegate{
		
		void refreshWhisperImg();
		
		void setWhisperImgPath(String imgPath);
		
		Bitmap takeImageViewParentShot();
	}
	
	private Activity getContext(){
		return activity;
	}
	
	//---------------------------------------换图
	
	private AlertDialog dialog;
	
	private static final int REQUEST_CODE_PICK_PHOTO_FROM_CAMERA = 0;
	private static final int REQUEST_CODE_PICK_PHOTO_FROM_ALBUMS = 1;
	
	// show对话框
	public void showSelectDialog() {
		Builder builder = new Builder(getContext());

		View view = View.inflate(getContext(), R.layout.upload_whisper_photo_dialog, null);
		
		TextView tv_album = (TextView) view.findViewById(R.id.tv_album);
		TextView tv_take = (TextView) view.findViewById(R.id.tv_take);

		tv_album.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent pickPhoto = new Intent(Intent.ACTION_PICK, null);
				pickPhoto.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				getContext().startActivityForResult(pickPhoto, REQUEST_CODE_PICK_PHOTO_FROM_ALBUMS);
			}
		});

		tv_take.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				File newfile = new File(whisperPublishNeedParams.getSaveTempPicPath());// file
				try {
					if(!newfile.createNewFile()){
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				Uri outputFileUri = Uri.fromFile(newfile);

				Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

				getContext().startActivityForResult(cameraIntent, REQUEST_CODE_PICK_PHOTO_FROM_CAMERA);
			}
		});

		dialog = builder.create();
		dialog.show();
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		dialog.getWindow().setContentView(view);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_CODE_PICK_PHOTO_FROM_ALBUMS) {
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();

				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContext().getContentResolver().query(uri, proj, null, null, null);

				if(cursor != null){
					try {
						int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						
						selectAndUploadWhisperImgWrapDelegate.setWhisperImgPath(cursor.getString(actual_image_column_index));
						selectAndUploadWhisperImgWrapDelegate.refreshWhisperImg();

						cursor.close();

					} catch (Exception e) {
						Toast.makeText(getContext(), "请选择系统相册图片", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getContext(), "请选择系统相册图片", Toast.LENGTH_SHORT).show();
				}
				
			}
		} else if (requestCode == REQUEST_CODE_PICK_PHOTO_FROM_CAMERA) {
			if (resultCode == Activity.RESULT_OK) {
				selectAndUploadWhisperImgWrapDelegate.setWhisperImgPath(whisperPublishNeedParams.getSaveTempPicPath());
				selectAndUploadWhisperImgWrapDelegate.refreshWhisperImg();
			}
		} 
	}

	public void uploadUserWhisperPhoto(){
		
		Bitmap sourceBitmap = selectAndUploadWhisperImgWrapDelegate.takeImageViewParentShot();

		File file = new File(whisperPublishNeedParams.getSaveWaitUploadPicPath());
		if (file.exists()) {
			if (!file.delete()){
				return;
			}
		}

		try {
			FileOutputStream stream = new FileOutputStream(file);

			if(sourceBitmap != null && !sourceBitmap.isRecycled()){
				sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);//大小压缩
			}
			try {
				stream.flush();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String savePath = SaveImageToPhotoAlbumUtil.insertImage(activity, file.getAbsolutePath());
		if (!TextUtils.isEmpty(savePath)){
			Toast.makeText(getContext(), "save to " + savePath, Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(getContext(), "save failure...", Toast.LENGTH_LONG).show();
		}
	}

}
