package com.tj.matriximageandmovetext;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tj.matriximageandmovetext.bottommenu.LoadMatchPictureWrap;
import com.tj.matriximageandmovetext.bottommenu.LoadMatchPictureWrapNeedParams;

public class WhisperPublishActivityNew extends WhisperPublishBaseActivity implements LoadMatchPictureWrap.LoadMatchPictureWrapDelegate {

	private LoadMatchPictureWrap loadMatchPictureWrap;
	
	private View whisperChangeMatchImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		
		RelativeLayout whipserImgPublishContainer = (RelativeLayout)findViewById(R.id.whipser_img_publish_container);
		
		loadMatchPictureWrap = new LoadMatchPictureWrap(
				new LoadMatchPictureWrapNeedParams(getWhisperPublishNeedParams(),
						whipserImgPublishContainer, this, getInflater()), this);
		
		whisperChangeMatchImg = findViewById(R.id.whisper_publish_change_match_img);
		whisperChangeMatchImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(loadMatchPictureWrap.isMatchPictureListExits()){
				}
			}
		});
	}
	
	@Override
	public int getLayoutId() {
		return R.layout.activity_whisper_publish_new;
	}
	
	@Override
	public void isShowSoftKeyBoard(){
		getWhisperImgCoverText().setVisibility(View.VISIBLE);
	}
	
	@Override
	public boolean isNeedLoadMatchPicture(){
		return loadMatchPictureWrap.isNeedLoadMatchPicture();
	}
	
	@Override
	public void loadMatchPictureUrls(){
		loadMatchPictureWrap.loadMatchPictureUrls();
	}

	@Override
	public void hiddeMatchPictureList(){
		if(whisperChangeMatchImg != null){
			loadMatchPictureWrap.hiddeMatchPictureList();
		}
	}
	
	//-----------------LoadMatchPictureWrapDelegate
	
	@Override
	public ImageView getWhisperShowImageView() {
		return getWhisperImageView();
	}

	@Override
	public void updateMatchWhisperCoverText(TextView coverText) {
		updateMatchWhisperCoverTextAttribute(coverText);
	}

	@Override
	public void setWhisperShowImageViewScaleType(ScaleType scaleType){
		setWhisperImageViewScaleType(scaleType);
	}
	
	@Override
	public void updateWhisperShowImageView(Bitmap showImg){
		updateWhisperImageView(showImg);
	}

}
