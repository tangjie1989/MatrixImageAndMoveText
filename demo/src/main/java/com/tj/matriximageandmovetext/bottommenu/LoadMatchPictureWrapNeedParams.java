package com.tj.matriximageandmovetext.bottommenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.tj.matriximageandmovetext.vo.WhisperPublishNeedParams;

public class LoadMatchPictureWrapNeedParams {

	public LoadMatchPictureWrapNeedParams(
			WhisperPublishNeedParams whisperPublishNeedParams,
			RelativeLayout whipserImgPublishContainer, Context context,
			LayoutInflater inflater) {
		super();
		this.whisperPublishNeedParams = whisperPublishNeedParams;
		this.whipserImgPublishContainer = whipserImgPublishContainer;
		this.context = context;
		this.inflater = inflater;
	}

	private WhisperPublishNeedParams whisperPublishNeedParams;
	private RelativeLayout whipserImgPublishContainer;
	private Context context;
	private LayoutInflater inflater;

	public WhisperPublishNeedParams getWhisperPublishNeedParams() {
		return whisperPublishNeedParams;
	}

	public void setWhisperPublishNeedParams(
			WhisperPublishNeedParams whisperPublishNeedParams) {
		this.whisperPublishNeedParams = whisperPublishNeedParams;
	}

	public RelativeLayout getWhipserImgPublishContainer() {
		return whipserImgPublishContainer;
	}

	public void setWhipserImgPublishContainer(
			RelativeLayout whipserImgPublishContainer) {
		this.whipserImgPublishContainer = whipserImgPublishContainer;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	public void setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
	}

}
