package com.tj.matriximageandmovetext.bottommenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.tj.matriximageandmovetext.R;
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
		
		String getPublishContent();
	}
	
	private Activity getContext(){
		return activity;
	}
	
	//---------------------------------------换图
	
	private TextView tv_album;
	private TextView tv_take;
	private TextView tv_cancel;
	private AlertDialog dialog;
	
	private static final int REQUEST_CODE_PICK_PHOTO_FROM_CAMERA = 0;
	private static final int REQUEST_CODE_PICK_PHOTO_FROM_ALBUMS = 1;
	
	// show对话框
	public void showSelectDialog() {
		Builder builder = new Builder(getContext());

		View view = View.inflate(getContext(), R.layout.upload_whisper_photo_dialog, null);
		
		TextView tvTitle = (TextView)view.findViewById(R.id.title);
		tvTitle.setText("选图");

		tv_album = (TextView) view.findViewById(R.id.tv_album);
		tv_take = (TextView) view.findViewById(R.id.tv_take);
		tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		
		tv_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

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
					newfile.createNewFile();
				} catch (IOException e) {
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
				Cursor cursor = getContext().managedQuery(uri, proj, null, null, null);
				
				if(cursor != null){
					try {
						int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						
						selectAndUploadWhisperImgWrapDelegate.setWhisperImgPath(cursor.getString(actual_image_column_index));
						selectAndUploadWhisperImgWrapDelegate.refreshWhisperImg();
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

	// 提交whisper图片
	public void uploadUserWhisperPhoto(String imgId){
		
		saveBitmapToLocalFile(whisperPublishNeedParams.getSaveWaitUploadPicPath(), selectAndUploadWhisperImgWrapDelegate.takeImageViewParentShot(), 80, false);
//		String whisperContent = selectAndUploadWhisperImgWrapDelegate.getPublishContent();//getWhisperImgCoverTextEditString();

		Toast.makeText(getContext(), whisperPublishNeedParams.getSaveWaitUploadPicPath(), Toast.LENGTH_LONG).show();
	}

	private void saveBitmapToLocalFile(String filePath , Bitmap sourceBitmap , int compressDegree , boolean isSourceBitmapRecycle){

		FileOutputStream stream = null;

		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}

		try {
			stream = new FileOutputStream(file);
			if(sourceBitmap != null && !sourceBitmap.isRecycled()){
				sourceBitmap.compress(Bitmap.CompressFormat.JPEG, compressDegree, stream);//大小压缩
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
				}
			} catch (IOException e2) {
			}
		}

	}

}
