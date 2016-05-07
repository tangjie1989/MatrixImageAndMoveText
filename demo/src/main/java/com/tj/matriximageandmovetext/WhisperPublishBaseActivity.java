package com.tj.matriximageandmovetext;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.tj.matriximageandmovetext.base.ImageLoadingConfig;
import com.tj.matriximageandmovetext.bottommenu.SelectAndUploadWhisperImgWrap;
import com.tj.matriximageandmovetext.bottommenu.WhisperCoverTextViewStatusUpdate;
import com.tj.matriximageandmovetext.bottommenu.WhisperCoverTextViewStatusUpdateNeedParams;
import com.tj.matriximageandmovetext.util.ImageInfo;
import com.tj.matriximageandmovetext.util.SoftInputUtils;
import com.tj.matriximageandmovetext.vo.WhisperPublishNeedParams;

import java.lang.reflect.Field;

/**
 * 主题和悠悠话发布base
 * 
 * @author tangjie
 * 
 */

public abstract class WhisperPublishBaseActivity extends Activity implements WhisperCoverTextViewStatusUpdate.WhisperCoverTextViewStatusUpdateDelegate,
		SelectAndUploadWhisperImgWrap.SelectAndUploadWhisperImgWrapDelegate {

	private RelativeLayout whipserImgContainer;
	private ImageView whisperImg;
	private ProgressBar whisperProcessImgLoadingBar;
	private EditText whisperImgCoverTextEdit;
	private TextView whisperImgCoverText;
	
	public LayoutInflater getInflater() {
		return inflater;
	}

	private LayoutInflater inflater;
	
	private int imgShowWidth;//图片显示宽度
	private int imgShowHeight;//图片显示高度
	
	private int textTopPadding;//文字距离顶部高度
	private int textBottomPading;//文字距离底部高度
	
	private String imgPath;
	
	public String getImgPath() {
		return imgPath;
	}
	
	public abstract boolean isNeedLoadMatchPicture();
	
	private WhisperPublishNeedParams whisperPublishNeedParams;
	
	public WhisperPublishNeedParams getWhisperPublishNeedParams() {
		return whisperPublishNeedParams;
	}

	public abstract int getLayoutId();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(getLayoutId());

//		if (getIntent() != null){
//			whisperPublishNeedParams = getIntent().getParcelableExtra(WhisperPublishNeedParams.INTENT_KEY);
//		}
		whisperPublishNeedParams = new WhisperPublishNeedParams();
		whisperPublishNeedParams.setSaveTempPicPath(MartixApplication.getInstance().getMartixLocalStorageUtil().getUploadUserPhotoTempFilePath());
		whisperPublishNeedParams.setSaveWaitUploadPicPath(MartixApplication.getInstance().getMartixLocalStorageUtil().getUploadUserPhotoHandledFilePath());

		inflater = LayoutInflater.from(this);
		
		initUploadSize();
		
	}
	
	private void initUploadSize(){
		
		Resources re = getResources();
        DisplayMetrics metrics = re.getDisplayMetrics();
        imgShowWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        
        int statusBarHeight = 38;//状态栏高度
        
        //获取手机系统状态栏高度
        try {  
              Class<?> clazz = Class.forName("com.android.internal.R$dimen");  
              Object object = clazz.newInstance();  
              Field field = clazz.getField("status_bar_height");  
              int height = Integer.parseInt(field.get(object).toString());  
              statusBarHeight = re.getDimensionPixelSize(height);
              
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        imgShowHeight = screenHeight - statusBarHeight;
        
        textTopPadding = getResources().getDimensionPixelSize(R.dimen.whipser_publish_top);
        textBottomPading = getResources().getDimensionPixelSize(R.dimen.whipser_publish_bottom);
	}
	
	protected void initView(){
		
		whipserImgContainer = (RelativeLayout)findViewById(R.id.whisper_img_container);
		whisperImg = (ImageView)findViewById(R.id.whisper_img);
		whisperProcessImgLoadingBar = (ProgressBar)findViewById(R.id.whisper_img_process_loading_bar);
		whisperImgCoverTextEdit = (EditText)findViewById(R.id.whisper_img_cover_text_edit);
		whisperImgCoverText = (TextView)findViewById(R.id.whisper_img_cover_text);
		
		whisperCoverTextViewStatusUpdate = new WhisperCoverTextViewStatusUpdate(
				new WhisperCoverTextViewStatusUpdateNeedParams(this,
						imgShowWidth, imgShowHeight, textTopPadding,
						textBottomPading), this);
		
		selectAndUploadWhisperImgWrap = new SelectAndUploadWhisperImgWrap(whisperPublishNeedParams,this,this);
		
		whisperImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(whisperImgCoverText.getVisibility() == View.VISIBLE){
					whisperImgCoverText.setVisibility(View.GONE);
					whisperImgCoverTextEdit.setVisibility(View.VISIBLE);
					SoftInputUtils.openInput(WhisperPublishBaseActivity.this);
				}else{
					mesureTextViewSizeAndPostion();
				}
			}
		});

		ImageButton whisperBackText = (ImageButton)findViewById(R.id.whisper_publish_back_text);
		whisperBackText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ImageButton whisperSubmitText = (ImageButton)findViewById(R.id.whisper_publish_submit_text);
		whisperSubmitText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(whisperImgCoverTextEdit.getVisibility() == View.VISIBLE){
					mesureTextViewSizeAndPostion();
					return;
				}
				
				String coverStr = getWhisperImgCoverTextEditString();
				if(TextUtils.isEmpty(coverStr)){
					Toast.makeText(WhisperPublishBaseActivity.this, "文字内容不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				
				selectAndUploadWhisperImgWrap.uploadUserWhisperPhoto();
			}
		});

		View whisperChangeImg = findViewById(R.id.whisper_publish_change_img);
		whisperChangeImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				hiddeMatchPictureList();
				
				if(!isNeedLoadMatchPicture()){//isNeedLoadMatchPicture
					selectAndUploadWhisperImgWrap.showSelectDialog();
				}
			}
		});
		
		whisperCoverTextViewStatusUpdate.initTextMove();
	}
	
	protected ImageView getWhisperImageView(){
		return whisperImg;
	}
	
	protected void setWhisperImageViewScaleType(ScaleType scaleType){
		whisperImg.setScaleType(scaleType);
	}
	
	protected void updateWhisperImageView(Bitmap img){
		whisperImg.setImageBitmap(img);
	}
	
	private String getWhisperImgCoverTextEditString(){
		String str = whisperImgCoverTextEdit.getText().toString();
		if(!TextUtils.isEmpty(str)){
			return str.trim();
		}else{
			return "";
		}
	}

	private class RefreshWhisperImageViewTask extends AsyncTask<Void, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... params) {
			ImageSize targetSize = new ImageSize(imgShowWidth, imgShowHeight); // result Bitmap will be fit to this size
			return ImageLoader.getInstance().loadImageSync("file://"+ getImgPath(), targetSize, ImageLoadingConfig.generateDisplayImageOptionsNoCatchDisc());
		}

		@Override
		protected void onPostExecute(Bitmap showImg) {
			if(showImg != null && !showImg.isRecycled()){

				ImageInfo perfectImageShowSizeInfo = new ImageInfo(imgShowWidth,imgShowHeight);

				//3.将map等比例缩放到接近屏幕的宽和高
				ImageInfo realImageShowSizeInfo = perfectImageShowSizeInfo.getBitmapScaleInfo(showImg);

				setWhisperImageViewScaleType(ScaleType.MATRIX);

				updateWhisperImageView(showImg);

				Matrix matrix = new Matrix();

				//Get the image's rect
				RectF drawableRect = new RectF(0, 0, showImg.getWidth(), showImg.getHeight());

				int xOffSet = (whisperImg.getWidth() - realImageShowSizeInfo.getImageWidth())/2;
				int yOffSet = (whisperImg.getHeight() - realImageShowSizeInfo.getImageHeight())/2;

				//Get the image view's rect
				RectF viewRect = new RectF(xOffSet, yOffSet, realImageShowSizeInfo.getImageWidth(),realImageShowSizeInfo.getImageHeight());

				//draw the image in the view
				matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.START);

				whisperImg.setImageMatrix(matrix);
				whisperImg.invalidate();

				whisperProcessImgLoadingBar.setVisibility(View.GONE);

				whisperImg.setVisibility(View.VISIBLE);

				isShowSoftKeyBoard();
			}else{
				Toast.makeText(getApplicationContext(), "photo handle failure", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public abstract void isShowSoftKeyBoard();

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		selectAndUploadWhisperImgWrap.onActivityResult(requestCode, resultCode, data);
	}
	
	private SelectAndUploadWhisperImgWrap selectAndUploadWhisperImgWrap;
	
	@Override
	public void refreshWhisperImg() {
		updateWhisperImageView(null);
		whisperImg.setVisibility(View.INVISIBLE);
		whisperImgCoverTextEdit.setVisibility(View.GONE);
		whisperImgCoverText.setVisibility(View.GONE);
		
		whisperProcessImgLoadingBar.setVisibility(View.VISIBLE);
		
		new RefreshWhisperImageViewTask().execute();
	}
	
	@Override
	public void setWhisperImgPath(String imgPath){
		this.imgPath = imgPath;
	}
	
	@Override
	public Bitmap takeImageViewParentShot() {// 获取imageview父视图的截屏
		whipserImgContainer.setDrawingCacheEnabled(true);
		whipserImgContainer.buildDrawingCache();
		return whipserImgContainer.getDrawingCache();
	}
	
	@Override
	public String getWhisperImgCoverTextString() {
		return getWhisperImgCoverTextEditString();
	}

	@Override
	public TextView getWhisperImgCoverText() {
		return whisperImgCoverText;
	}

	protected void updateMatchWhisperCoverTextAttribute(TextView coverText){
		coverText.setText(getWhisperImgCoverTextEditString());
	}

	//------------------------coverTextView状态封装(移动，大小更新)
	
	private WhisperCoverTextViewStatusUpdate whisperCoverTextViewStatusUpdate;
	
	//WhisperCoverTextViewStatusUpdateDelegate
	
	@Override
	public EditText getWhisperImgCoverTextEdit() {
		return whisperImgCoverTextEdit;
	}

	private void mesureTextViewSizeAndPostion(){
		whisperCoverTextViewStatusUpdate.mesureTextViewSizeAndPostion();
		
		if (!TextUtils.isEmpty(getWhisperImgCoverTextEditString()) && isNeedLoadMatchPicture()) {//isNeedLoadMatchPicture
			loadMatchPictureUrls();
		}
	}

	//------------------------ 匹配图加载部分------------------
	
	//加载匹配图数据
	public abstract void loadMatchPictureUrls();
	
	public abstract void hiddeMatchPictureList();

}
