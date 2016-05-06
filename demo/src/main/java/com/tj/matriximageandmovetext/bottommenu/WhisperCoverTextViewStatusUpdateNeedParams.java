package com.tj.matriximageandmovetext.bottommenu;

import android.content.Context;

public class WhisperCoverTextViewStatusUpdateNeedParams {

	public WhisperCoverTextViewStatusUpdateNeedParams(Context context,
			int imgShowWidth, int imgShowHeight, int textTopPadding,
			int textBottomPading) {
		super();
		this.context = context;
		this.imgShowWidth = imgShowWidth;
		this.imgShowHeight = imgShowHeight;
		this.textTopPadding = textTopPadding;
		this.textBottomPading = textBottomPading;
	}

	private Context context;

	private int imgShowWidth;// 图片显示宽度
	private int imgShowHeight;// 图片显示高度

	private int textTopPadding;// 文字距离顶部高度
	private int textBottomPading;// 文字距离底部高度

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public int getImgShowWidth() {
		return imgShowWidth;
	}

	public void setImgShowWidth(int imgShowWidth) {
		this.imgShowWidth = imgShowWidth;
	}

	public int getImgShowHeight() {
		return imgShowHeight;
	}

	public void setImgShowHeight(int imgShowHeight) {
		this.imgShowHeight = imgShowHeight;
	}

	public int getTextTopPadding() {
		return textTopPadding;
	}

	public void setTextTopPadding(int textTopPadding) {
		this.textTopPadding = textTopPadding;
	}

	public int getTextBottomPading() {
		return textBottomPading;
	}

	public void setTextBottomPading(int textBottomPading) {
		this.textBottomPading = textBottomPading;
	}

}
