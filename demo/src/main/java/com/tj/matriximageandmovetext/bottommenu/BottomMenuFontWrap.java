package com.tj.matriximageandmovetext.bottommenu;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.tj.matriximageandmovetext.R;

import java.io.File;

public class BottomMenuFontWrap {
	
	private BottomMenuFontNeedParams bottomMenuFontNeedParams;
	
	public BottomMenuFontWrap(BottomMenuFontNeedParams bottomMenuFontNeedParams) {
		super();
		this.bottomMenuFontNeedParams = bottomMenuFontNeedParams;
		setCoverTextColor(getWhiteColor());
		setCoverTextShadowLayerColor(getBlackColor());
	}

	public interface BottomMenuFontDelegate{
		
		public String getWhisperImgCoverTextString();
		
		public TextView getWhisperImgCoverText();
		
		public void updateWhisperCoverTextViewStatus();
		
	}
	
	private int defaultFontSize = 30;
	private int otherFontSize = 34;
	
	public int getDefaultFontSize() {
		return defaultFontSize;
	}

	private LinearLayout bottomMenuFont;
	private int bottomMenuFontHeight;
	
	private LinearLayout bottomMenuFontChangeTextColor;
	
	private LinearLayout bottomMenuFontTpfaceLe;
	private TextView bottomMenuFontLeText;
	private View bottomMenuFontLeSelectIndicator;
	
	private LinearLayout bottomMenuFontTpfaceDou;
	private TextView bottomMenuFontDouText;
	private View bottomMenuFontDouSelectIndicator;
	
	private LinearLayout bottomMenuFontTpfaceXue;
	private TextView bottomMenuFontXueText;
	private View bottomMenuFontXueSelectIndicator;
	
	private LinearLayout bottomMenuFontTpfaceDefault;
	private TextView bottomMenuFontDefaultText;
	private View bottomMenuFontDefaultSelectIndicator;
	
	public void init(){
		
		initBottomMenuFont();
		generateTypeFace();
		initModelTextTypeFace();
	}
	
	private void initBottomMenuFont(){
		
		bottomMenuFontHeight = bottomMenuFontNeedParams.getContext().getResources().getDimensionPixelSize(R.dimen.whisper_publish_bottom_font_menu_height);
		
		bottomMenuFont = (LinearLayout)bottomMenuFontNeedParams.getInflater().inflate(R.layout.activity_whisper_publish_font_change_layout, null);
		
		bottomMenuFontChangeTextColor = (LinearLayout)bottomMenuFont.findViewById(R.id.whisper_publish_change_text_color);
		
		bottomMenuFontChangeTextColor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(getCoverTextColor() == getWhiteColor()){
					setCoverTextColor(getBlackColor());
					setCoverTextShadowLayerColor(getWhiteColor());
				}else{
					setCoverTextColor(getWhiteColor());
					setCoverTextShadowLayerColor(getBlackColor());
				}
				
				updateCoverTextColor(bottomMenuFontNeedParams.getBottomMenuFontDelegate().getWhisperImgCoverText());
			}
		});
		
		bottomMenuFontTpfaceLe = (LinearLayout)bottomMenuFont.findViewById(R.id.whisper_publish_text_tpface_le);
		bottomMenuFontTpfaceLe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				responseFontChange(0);
			}
		});
		bottomMenuFontLeText = (TextView)bottomMenuFont.findViewById(R.id.whisper_publish_text_tpface_le_text_indicator);
		bottomMenuFontLeSelectIndicator = (View)bottomMenuFont.findViewById(R.id.whisper_publish_text_tpface_le_select_indicator);
		
		bottomMenuFontTpfaceDou = (LinearLayout)bottomMenuFont.findViewById(R.id.whisper_publish_text_tpface_dou);
		bottomMenuFontTpfaceDou.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				responseFontChange(1);
			}
		});
		bottomMenuFontDouText = (TextView)bottomMenuFont.findViewById(R.id.whisper_publish_text_tpface_dou_text_indicator);
		bottomMenuFontDouSelectIndicator = (View)bottomMenuFont.findViewById(R.id.whisper_publish_text_tpface_dou_select_indicator);
		
		bottomMenuFontTpfaceXue = (LinearLayout)bottomMenuFont.findViewById(R.id.whisper_publish_text_tpface_xue);
		bottomMenuFontTpfaceXue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				responseFontChange(2);
			}
		});
		bottomMenuFontXueText = (TextView)bottomMenuFont.findViewById(R.id.whisper_publish_text_tpface_xue_text_indicator);
		bottomMenuFontXueSelectIndicator = (View)bottomMenuFont.findViewById(R.id.whisper_publish_text_tpface_xue_select_indicator);
		
		bottomMenuFontTpfaceDefault = (LinearLayout)bottomMenuFont.findViewById(R.id.whisper_publish_text_tpface_default);
		bottomMenuFontTpfaceDefault.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				responseFontChange(3);
			}
		});
		
		bottomMenuFontDefaultText = (TextView)bottomMenuFont.findViewById(R.id.whisper_publish_text_tpface_default_text_indicator);
		bottomMenuFontDefaultSelectIndicator = (View)bottomMenuFont.findViewById(R.id.whisper_publish_text_tpface_default_select_indicator);
		
	}
	
	//隐藏或者显示
	public void operationBottomMenuFont(){
		
		if(bottomMenuFontNeedParams.getWhipserPublishContainer().findViewById(R.id.whisper_publish_bottom_menu_font) != null){
			bottomMenuFontNeedParams.getWhipserPublishContainer().removeView(bottomMenuFont);
		}else{
			
			String coverStr = bottomMenuFontNeedParams.getBottomMenuFontDelegate().getWhisperImgCoverTextString();
			if(TextUtils.isEmpty(coverStr)){
				Toast.makeText(bottomMenuFontNeedParams.getContext(), "文字内容不能为空", Toast.LENGTH_SHORT).show();
			}else{
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,bottomMenuFontHeight);
				lp.addRule(RelativeLayout.ABOVE,R.id.whisper_publish_bottom_menu);
				bottomMenuFont.setLayoutParams(lp);
				bottomMenuFontNeedParams.getWhipserPublishContainer().addView(bottomMenuFont);
			}
			
		}
	}
	
	public void hideBottomMenuFont(){
		if(bottomMenuFontNeedParams.getWhipserPublishContainer().findViewById(R.id.whisper_publish_bottom_menu_font) != null){
			bottomMenuFontNeedParams.getWhipserPublishContainer().removeView(bottomMenuFont);
		}
	}
	
	private int coverTextColor;
	private int coverTextShadowLayerColor;
	
	private int getCoverTextColor() {
		return coverTextColor;
	}

	private void setCoverTextColor(int coverTextColor) {
		this.coverTextColor = coverTextColor;
	}

	private int getCoverTextShadowLayerColor() {
		return coverTextShadowLayerColor;
	}

	private void setCoverTextShadowLayerColor(int coverTextShadowLayerColor) {
		this.coverTextShadowLayerColor = coverTextShadowLayerColor;
	}
	
	private int getWhiteColor(){
		return bottomMenuFontNeedParams.getContext().getResources().getColor(R.color.whipser_publish_white);
	}
	
	private int getBlackColor(){
		return bottomMenuFontNeedParams.getContext().getResources().getColor(R.color.whipser_publish_black);
	}

	private void updateCoverTextColor(TextView coverText){
		coverText.setTextColor(getCoverTextColor());
		coverText.setShadowLayer(5, 0, 0, getCoverTextShadowLayerColor());
	}
	
	private void textBold(TextView textView){
		TextPaint tpaint = textView.getPaint();
        tpaint.setFakeBoldText(true);
	}
	
	private void textUnbold(TextView textView){
		TextPaint tpaint = textView.getPaint();
        tpaint.setFakeBoldText(false);
	}
	
	//设置匹配图上面的文字属性(颜色 字体类型等)
	public void updateMatchWhisperCoverTextAttribute(TextView coverText){
		
		updateCoverTextColor(coverText);
		
		String str = bottomMenuFontNeedParams.getBottomMenuFontDelegate().getWhisperImgCoverTextString();
		coverText.setText(str);
		
		coverText.setTypeface(getCurSelectedTypeFace());
		
		if(getCurSelectedTypeFace() == typeFaceDefault){
			textBold(coverText);
		}else{
			textUnbold(coverText);
		}
	}
	
	//设置发布文字默认状态 加粗 大小 字体
	public void setWhisperCoverTextDefaultStatus(){
		
		TextView whisperImgCoverText = bottomMenuFontNeedParams.getBottomMenuFontDelegate().getWhisperImgCoverText();
		
		textBold(whisperImgCoverText);
		whisperImgCoverText.setTypeface(typeFaceDefault);
		whisperImgCoverText.setTextSize(defaultFontSize);
	}
	
	private void responseFontChange(int type){
		
		setAllBottomMenuFontSelectIndicatorInvisible();
		
		switch (type) {
		case 0:
			setCurSelectedTypeFace(typeFaceLe);
			setBottomMenuOneFontSelectIndicatorInvisible(bottomMenuFontLeSelectIndicator);
			break;
		case 1:
			setCurSelectedTypeFace(typeFaceDou);
			setBottomMenuOneFontSelectIndicatorInvisible(bottomMenuFontDouSelectIndicator);
			break;
		case 2:
			setCurSelectedTypeFace(typeFaceXue);
			setBottomMenuOneFontSelectIndicatorInvisible(bottomMenuFontXueSelectIndicator);
			break;
		case 3:
			setCurSelectedTypeFace(typeFaceDefault);
			setBottomMenuOneFontSelectIndicatorInvisible(bottomMenuFontDefaultSelectIndicator);
			break;
		default:
			break;
		}
		
		updateWhisperImgCoverTextTypeFace();
		
		bottomMenuFontNeedParams.getBottomMenuFontDelegate().updateWhisperCoverTextViewStatus();
	}
	
	private void setAllBottomMenuFontSelectIndicatorInvisible(){
		bottomMenuFontLeSelectIndicator.setVisibility(View.INVISIBLE);
		bottomMenuFontDouSelectIndicator.setVisibility(View.INVISIBLE);
		bottomMenuFontXueSelectIndicator.setVisibility(View.INVISIBLE);
		bottomMenuFontDefaultSelectIndicator.setVisibility(View.INVISIBLE);
	}
	
	private void setBottomMenuOneFontSelectIndicatorInvisible(View view){
		view.setVisibility(View.VISIBLE);
	}
	
	private void updateWhisperImgCoverTextTypeFace(){
		
		TextView whisperImgCoverText = bottomMenuFontNeedParams.getBottomMenuFontDelegate().getWhisperImgCoverText();
		
		whisperImgCoverText.setTypeface(getCurSelectedTypeFace());
		
		if(getCurSelectedTypeFace() == typeFaceDefault){
			textBold(whisperImgCoverText);
			whisperImgCoverText.setTextSize(defaultFontSize);
		}else{
			textUnbold(whisperImgCoverText);
			whisperImgCoverText.setTextSize(otherFontSize);
		}
		
	}

	private Typeface typeFaceLe;
	private Typeface typeFaceDou;
	private Typeface typeFaceXue;
	private Typeface typeFaceDefault;
	
	private Typeface curSelectedTypeFace;
	
	private Typeface getCurSelectedTypeFace() {
		return curSelectedTypeFace;
	}

	private void setCurSelectedTypeFace(Typeface curSelectedTypeFace) {
		this.curSelectedTypeFace = curSelectedTypeFace;
	}

	//字体生成
	private void generateTypeFace(){
		
		String ttfLocalSavePath = bottomMenuFontNeedParams.getWhisperPublishNeedParams().getTtfLocalSavePath();
		
//		typeFaceDou = Typeface.createFromFile(ttfLocalSavePath + File.separator + SaveTTFToLocalUtil.convertedTtf[0]);
//		typeFaceLe = Typeface.createFromFile(ttfLocalSavePath + File.separator + SaveTTFToLocalUtil.convertedTtf[1]);
//		typeFaceXue = Typeface.createFromFile(ttfLocalSavePath + File.separator + SaveTTFToLocalUtil.convertedTtf[2]);
		typeFaceDefault = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
	}
	
	private void initModelTextTypeFace(){
		bottomMenuFontLeText.setTypeface(typeFaceLe);
		bottomMenuFontDouText.setTypeface(typeFaceDou);
		bottomMenuFontXueText.setTypeface(typeFaceXue);
		bottomMenuFontDefaultText.setTypeface(typeFaceDefault);
	}

}
