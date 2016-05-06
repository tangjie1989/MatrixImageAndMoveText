package com.tj.matriximageandmovetext.bottommenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.tj.matriximageandmovetext.vo.WhisperPublishNeedParams;

public class BottomMenuFontNeedParams {

	public BottomMenuFontNeedParams(Context context, LayoutInflater inflater,
			RelativeLayout whipserPublishContainer,
			WhisperPublishNeedParams whisperPublishNeedParams,
			BottomMenuFontWrap.BottomMenuFontDelegate bottomMenuFontDelegate) {
		super();
		this.context = context;
		this.inflater = inflater;
		this.whipserPublishContainer = whipserPublishContainer;
		this.whisperPublishNeedParams = whisperPublishNeedParams;
		this.bottomMenuFontDelegate = bottomMenuFontDelegate;
	}

	private Context context;
	private LayoutInflater inflater;
	private RelativeLayout whipserPublishContainer;
	private WhisperPublishNeedParams whisperPublishNeedParams;
	private BottomMenuFontWrap.BottomMenuFontDelegate bottomMenuFontDelegate;

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

	public RelativeLayout getWhipserPublishContainer() {
		return whipserPublishContainer;
	}

	public void setWhipserPublishContainer(
			RelativeLayout whipserPublishContainer) {
		this.whipserPublishContainer = whipserPublishContainer;
	}

	public WhisperPublishNeedParams getWhisperPublishNeedParams() {
		return whisperPublishNeedParams;
	}

	public void setWhisperPublishNeedParams(
			WhisperPublishNeedParams whisperPublishNeedParams) {
		this.whisperPublishNeedParams = whisperPublishNeedParams;
	}

	public BottomMenuFontWrap.BottomMenuFontDelegate getBottomMenuFontDelegate() {
		return bottomMenuFontDelegate;
	}

	public void setBottomMenuFontDelegate(
			BottomMenuFontWrap.BottomMenuFontDelegate bottomMenuFontDelegate) {
		this.bottomMenuFontDelegate = bottomMenuFontDelegate;
	}

}
