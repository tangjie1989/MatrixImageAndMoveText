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

	public RelativeLayout getWhipserImgPublishContainer() {
		return whipserImgPublishContainer;
	}

	public Context getContext() {
		return context;
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

}
