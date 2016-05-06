package com.tj.matriximageandmovetext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.tj.matriximageandmovetext.base.ImageLoadingConfig;
import com.tj.matriximageandmovetext.bottommenu.BottomMenuFontNeedParams;
import com.tj.matriximageandmovetext.bottommenu.BottomMenuFontWrap;
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

public abstract class WhisperPublishBaseActivity extends Activity implements BottomMenuFontWrap.BottomMenuFontDelegate, WhisperCoverTextViewStatusUpdate.WhisperCoverTextViewStatusUpdateDelegate, SelectAndUploadWhisperImgWrap.SelectAndUploadWhisperImgWrapDelegate {

	private RelativeLayout whipserImgContainer;
	private ImageView whisperImg;
	private ProgressBar whisperProcessImgLoadingBar;
	private EditText whisperImgCoverTextEdit;
	private TextView whisperImgCoverText;
	
	private ImageButton whisperBackText;
	private ImageButton whisperSubmitText;
	
	private LinearLayout whisperChangeImg;
	private LinearLayout whisperChangeColor;
	
	private String imgId;//记录当前选中的匹配图img id
	
	public Handler getHandler() {
		return handler;
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	private Handler handler = new Handler();
	private LayoutInflater inflater;
	
	private int screenHeight;//屏幕高
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
		
//		SaveTTFToLocalUtil.saveTTFToLocal(getApplicationContext(),whisperPublishNeedParams.getTtfLocalSavePath());
	}
	
	private void initUploadSize(){
		
		Resources re = getResources();
        DisplayMetrics metrics = re.getDisplayMetrics();
        imgShowWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        
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
		
		RelativeLayout whipserPublishContainer = (RelativeLayout)findViewById(R.id.whipser_img_publish_container);
		
		whipserImgContainer = (RelativeLayout)findViewById(R.id.whisper_img_container);
		whisperImg = (ImageView)findViewById(R.id.whisper_img);
		whisperProcessImgLoadingBar = (ProgressBar)findViewById(R.id.whisper_img_process_loading_bar);
		whisperImgCoverTextEdit = (EditText)findViewById(R.id.whisper_img_cover_text_edit);
		whisperImgCoverText = (TextView)findViewById(R.id.whisper_img_cover_text);
		
		whisperCoverTextViewStatusUpdate = new WhisperCoverTextViewStatusUpdate(
				new WhisperCoverTextViewStatusUpdateNeedParams(this,
						imgShowWidth, imgShowHeight, textTopPadding,
						textBottomPading), this);
		
		bottomMenuFontWrap = new BottomMenuFontWrap(new BottomMenuFontNeedParams(this, inflater, whipserPublishContainer, whisperPublishNeedParams, this));
		
		selectAndUploadWhisperImgWrap = new SelectAndUploadWhisperImgWrap(whisperPublishNeedParams,this,this);
		
		whisperImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(whisperImgCoverText.getVisibility() == View.VISIBLE){
					whisperImgCoverText.setVisibility(View.GONE);
					whisperImgCoverTextEdit.setVisibility(View.VISIBLE);
					
					SoftInputUtils.openInput(WhisperPublishBaseActivity.this,whisperImgCoverTextEdit);
					
					hideBottomMenuFont();
				}else{
					
					mesureTextViewSizeAndPostion();
					
				}
			}
		});
		
		whisperBackText = (ImageButton)findViewById(R.id.whisper_publish_back_text);
		whisperBackText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				finish();
				showCancelDialog();
			}
		});
		
		whisperSubmitText = (ImageButton)findViewById(R.id.whisper_publish_submit_text);
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
				
				selectAndUploadWhisperImgWrap.uploadUserWhisperPhoto(getWhisperImageId());
			}
		});
		
		whisperChangeImg = (LinearLayout)findViewById(R.id.whisper_publish_change_img);
		whisperChangeImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				hiddeMatchPictureList();//隐藏匹配图片列表
				
				hideBottomMenuFont();//隐藏字体菜单
				
				if(!isNeedLoadMatchPicture()){//isNeedLoadMatchPicture
					selectAndUploadWhisperImgWrap.showSelectDialog();
				}
			}
		});
		
		whisperChangeColor = (LinearLayout)findViewById(R.id.whisper_publish_change_color);
		whisperChangeColor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				hiddeMatchPictureList();//隐藏匹配图片列表
				
				bottomMenuFontWrap.operationBottomMenuFont();
				
			}
		});
		
		whisperCoverTextViewStatusUpdate.initTextMove();
		
		bottomMenuFontWrap.init();
		
		setDefaultCoverTextStatus();
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
	
	protected void updateWhisperImageId(String img){
		this.imgId = img;
	}
	
	private String getWhisperImageId(){
		return imgId;
	}
	
	private String getWhisperImgCoverTextEditString(){
		String str = whisperImgCoverTextEdit.getText().toString();
		if(!TextUtils.isEmpty(str)){
			return str.trim();
		}else{
			return "";
		}
	}
	
	protected void showWhisperImg(){
		new generateWhisperPublishImgThread().start();
	}
	
	//显示发布图片线程
	private class generateWhisperPublishImgThread extends Thread{
		
		@Override
		public void run() {
			
			updateWhisperImageId(null);
			
			ImageSize targetSize = new ImageSize(imgShowWidth, imgShowHeight); // result Bitmap will be fit to this size
			
			final Bitmap showImg = ImageLoader.getInstance().loadImageSync("file://"+ getImgPath(), targetSize, ImageLoadingConfig.generateDisplayImageOptionsNoCatchDisc());
			
			handler.post(new Runnable() {
				@Override
				public void run() {
					
					if(showImg != null && !showImg.isRecycled()){
						
						//3.将map等比例缩放到接近屏幕的宽和高
						ImageInfo imgInfo = ImageInfo.getBitmapScaleInfo(showImg, new ImageInfo(imgShowWidth,imgShowHeight));
						
						setWhisperImageViewScaleType(ScaleType.MATRIX);
						
						updateWhisperImageView(showImg);
						
						Matrix matrix = new Matrix(); 
						
						//Get the image's rect
						RectF drawableRect = new RectF(0, 0, showImg.getWidth(), showImg.getHeight());
						       
						int xOffSet = (whisperImg.getWidth() - imgInfo.getImageWidth())/2;
						int yOffSet = (whisperImg.getHeight() - imgInfo.getImageHeight())/2;
						
						//Get the image view's rect
						RectF viewRect = new RectF(xOffSet, yOffSet, imgInfo.getImageWidth(),imgInfo.getImageHeight());
								
						//draw the image in the view
						matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.START);
						
						whisperImg.setImageMatrix(matrix);
						whisperImg.invalidate();
						
						whisperProcessImgLoadingBar.setVisibility(View.GONE);
						
						whisperImg.setVisibility(View.VISIBLE);
						
						isShowSoftKeyBoard();
					}else{
						Toast.makeText(getApplicationContext(), "图片处理失败", Toast.LENGTH_SHORT).show();
						finish();
					}
				}
			});
		}
	}
	
	public abstract void isShowSoftKeyBoard();
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		selectAndUploadWhisperImgWrap.onActivityResult(requestCode, resultCode, data);
	}
	
	//-----------------------------悠悠话图片相册选择和发布
	
	private SelectAndUploadWhisperImgWrap selectAndUploadWhisperImgWrap;
	
	@Override
	public void refreshWhisperImg() {// 刷新发布图片
		updateWhisperImageView(null);
		whisperImg.setVisibility(View.INVISIBLE);
		whisperImgCoverTextEdit.setVisibility(View.GONE);
		whisperImgCoverText.setVisibility(View.GONE);
		
		whisperProcessImgLoadingBar.setVisibility(View.VISIBLE);
		
		new generateWhisperPublishImgThread().start();
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
	public String getPublishContent(){
		return getWhisperImgCoverTextEditString();
	}
	
	//--------------------------字体部分
	
	private BottomMenuFontWrap bottomMenuFontWrap;
	
	private void setDefaultCoverTextStatus(){
		bottomMenuFontWrap.setWhisperCoverTextDefaultStatus();
	}
	
	protected void hideBottomMenuFont(){
		bottomMenuFontWrap.hideBottomMenuFont();
	}
	
	protected void updateMatchWhisperCoverTextAttribute(TextView coverText){
		bottomMenuFontWrap.updateMatchWhisperCoverTextAttribute(coverText);
	}
	
	//BottomMenuFontDelegate
	
	@Override
	public String getWhisperImgCoverTextString() {
		return getWhisperImgCoverTextEditString();
	}

	@Override
	public TextView getWhisperImgCoverText() {
		return whisperImgCoverText;
	}

	@Override
	public void updateWhisperCoverTextViewStatus() {
		mesureTextViewSizeAndPostion();
	}
	
	//------------------------coverTextView状态封装(移动，大小更新)
	
	private WhisperCoverTextViewStatusUpdate whisperCoverTextViewStatusUpdate;
	
	//WhisperCoverTextViewStatusUpdateDelegate
	
	@Override
	public EditText getWhisperImgCoverTextEdit() {
		return whisperImgCoverTextEdit;
	}

	@Override
	public void hiddenBottomMenuFontView() {
		hideBottomMenuFont();
	}
	
	private void mesureTextViewSizeAndPostion(){
		whisperCoverTextViewStatusUpdate.mesureTextViewSizeAndPostion();
		
		if (!TextUtils.isEmpty(getWhisperImgCoverTextEditString()) && isNeedLoadMatchPicture()) {//isNeedLoadMatchPicture
			loadMatchPictureUrls(getWhisperImgCoverTextEditString(), this);
		}
	}
	
	
	//------------------------ 匹配图加载部分------------------
	
	//加载匹配图数据
	public abstract void loadMatchPictureUrls(String content, Context cxt);
	
	public abstract void hiddeMatchPictureList();
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showCancelDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	//显示退出对话框
	public void showCancelDialog(){

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.activity_whisper_cancel_publish);
		builder.setNegativeButton(R.string.activity_whisper_cancel_publish_no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		builder.setPositiveButton(R.string.activity_whisper_cancel_publish_yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});
		builder.show();
	}

}
