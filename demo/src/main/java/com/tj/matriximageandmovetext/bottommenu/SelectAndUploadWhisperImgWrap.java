package com.tj.matriximageandmovetext.bottommenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
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
import com.tj.matriximageandmovetext.util.UploadFileSaveToDiscCacheUtil;
import com.tj.matriximageandmovetext.vo.WhisperPublishNeedParams;

import java.io.File;
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
		
		uploadFileSaveToDiscCacheUtil = new UploadFileSaveToDiscCacheUtil(whisperPublishNeedParams.getSaveWaitUploadPicPath(), whisperPublishNeedParams.getSaveTempPicPath());
	}

	// 刷新个人界面悠悠话列表广播
	private static final String WHISPER_REFRESH_ACTION = "miyouquan.refresh.whisper";
	
	private WhisperPublishNeedParams whisperPublishNeedParams;
	private Activity activity;
	private SelectAndUploadWhisperImgWrapDelegate selectAndUploadWhisperImgWrapDelegate;
	
	private UploadFileSaveToDiscCacheUtil uploadFileSaveToDiscCacheUtil;
	
	public interface SelectAndUploadWhisperImgWrapDelegate{
		
		public void refreshWhisperImg();
		
		public void setWhisperImgPath(String imgPath);
		
		public Bitmap takeImageViewParentShot();
		
		public String getPublishContent();
		
	}
	
	private Activity getContext(){
		return activity;
	}
	
	private String getSubjectId(){
		return whisperPublishNeedParams.getPostUrlParams().get(WhisperPublishNeedParams.SUBJECT_ID);
	}
	
	private ProgressDialog uploadingDialog;
	
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
		if(TextUtils.isEmpty(getSubjectId())){
			tvTitle.setText("先选一张心情美图");
		}else{
			tvTitle.setText("选择一张符合主题的图片");
		}

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
						
//						imgPath = cursor.getString(actual_image_column_index);
//						refreshWhisperImg();
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
				
//				imgPath = whisperPublishNeedParams.getSaveTempPicPath();
//				refreshWhisperImg();
			}
		} 
	}

	// 提交whisper图片
	public void uploadUserWhisperPhoto(String imgId){
		
		uploadFileSaveToDiscCacheUtil.saveBitmapToLocalFile(whisperPublishNeedParams.getSaveWaitUploadPicPath(), selectAndUploadWhisperImgWrapDelegate.takeImageViewParentShot(), 80, false);
		
		String whisperContent = selectAndUploadWhisperImgWrapDelegate.getPublishContent();//getWhisperImgCoverTextEditString();
		
		File file = new File(whisperPublishNeedParams.getSaveWaitUploadPicPath());
//		RequestParams requestParams = new RequestParams();
//		try {
//			requestParams.put("upfile", file);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		HashMap<String, String> urlParams = whisperPublishNeedParams.getPostUrlParams();
//		urlParams.put("content", whisperContent);
//		urlParams.put("imei", StringUtil.getIMEI(activity));
//		if(!StringUtil.isEmpty(imgId)){
//			urlParams.put("imgid", imgId);
//		}else{
//			urlParams.remove("imgid");
//		}
//
//		String requestUrlStr = RequestPostJsonWrap.generateUploadPicRequestParams(urlParams);
//
////		System.out.println("\r\n\r\n post url : " + whisperPublishNeedParams.getProjectImgInterfaceUrl() + "?" + requestUrlStr);
//
//		WhisperPublishRequestClient.post(getContext() , requestParams , whisperPublishNeedParams.getProjectImgInterfaceUrl() + "?" + requestUrlStr ,  new TextHttpResponseHandler() {
//
//			@Override
//			public void onStart() {
//				uploadingDialog = new ProgressDialog(getContext());
//				uploadingDialog.setMessage(getContext().getResources().getString(R.string.whipser_publish_progress_dialog_tip_type));
//				uploadingDialog.setCancelable(false);
//				uploadingDialog.show();
//			}
//
//			@Override
//			public void onFailure(int statusCode, Header[] headers,
//					String responseString, Throwable throwable) {
//			}
//
//			@Override
//			public void onSuccess(int statusCode, Header[] headers,
//					String responseString) {
//
////				System.out.println("\r\nuploadUserWhisperPhoto responseString = "+responseString);
//
//				UploadWhisperPhotoResultVo resultVo = WhisperPublishJsonParser.getUploadUserPhotoResultFromJson(responseString);
//
////				System.out.println("\r\n\r\n" + resultVo);
//
//				if (resultVo != null) {
//
//					if(resultVo.getResult().equals("1") && !StringUtil.isEmpty(resultVo.getUrl())){
//
//						if(StringUtil.isEmpty(getSubjectId())){
//							Toast.makeText(getContext(), R.string.whipser_publish_success_tip, Toast.LENGTH_SHORT).show();
//						}else{
//							Toast.makeText(getContext(), R.string.whipser_theme_publish_success_tip, Toast.LENGTH_SHORT).show();
//						}
//
//						uploadFileSaveToDiscCacheUtil.generateBigWhisperToLocalDiscCache(resultVo.getUrl());
//						uploadFileSaveToDiscCacheUtil.generateSmallWhisperToLocalDiscCache(resultVo.getUrl());
//
//					}else if(resultVo.getResult().equals("-74")){
//
//						Toast.makeText(getContext(), R.string.whipser_publish_repeated_tip, Toast.LENGTH_SHORT).show();
//
//					}else{
//						if(StringUtil.isEmpty(getSubjectId())){
//							Toast.makeText(getContext(), R.string.whipser_publish_failure_tip, Toast.LENGTH_SHORT).show();
//						}else{
//							Toast.makeText(getContext(), R.string.whipser_theme_publish_failure_tip, Toast.LENGTH_SHORT).show();
//						}
//					}
//
//					Intent refreshIntent = new Intent();
//					refreshIntent.setAction(WHISPER_REFRESH_ACTION);
//					getContext().sendBroadcast(refreshIntent);
//					getContext().finish();
//				} else {
//					if(StringUtil.isEmpty(getSubjectId())){
//						Toast.makeText(getContext(), R.string.whipser_publish_failure_tip, Toast.LENGTH_SHORT).show();
//					}else{
//						Toast.makeText(getContext(), R.string.whipser_theme_publish_failure_tip, Toast.LENGTH_SHORT).show();
//					}
//				}
//
//			}
//
//			@Override
//			public void onFinish() {
//				uploadingDialog.dismiss();
//			}
//
//		});
	}
	
	

}
