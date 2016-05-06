package com.tj.matriximageandmovetext.bottommenu;

import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.tj.matriximageandmovetext.util.SoftInputUtils;

public class WhisperCoverTextViewStatusUpdate {

	public WhisperCoverTextViewStatusUpdate(
			WhisperCoverTextViewStatusUpdateNeedParams whisperCoverTextViewStatusUpdateNeedParams,
			WhisperCoverTextViewStatusUpdateDelegate whisperCoverTextViewStatusUpdateDelegate) {
		super();
		this.whisperCoverTextViewStatusUpdateNeedParams = whisperCoverTextViewStatusUpdateNeedParams;
		this.whisperCoverTextViewStatusUpdateDelegate = whisperCoverTextViewStatusUpdateDelegate;
	}

	private WhisperCoverTextViewStatusUpdateNeedParams whisperCoverTextViewStatusUpdateNeedParams;
	private WhisperCoverTextViewStatusUpdateDelegate whisperCoverTextViewStatusUpdateDelegate;

	public interface WhisperCoverTextViewStatusUpdateDelegate {

		EditText getWhisperImgCoverTextEdit();

		String getWhisperImgCoverTextString();
		
		TextView getWhisperImgCoverText();
	}

	// --------------------------------------文字拖动和字体切换时，重新计算textview大小和位置

	public void mesureTextViewSizeAndPostion() {

		TextView whisperImgCoverText = whisperCoverTextViewStatusUpdateDelegate.getWhisperImgCoverText();
		EditText whisperImgCoverTextEdit = whisperCoverTextViewStatusUpdateDelegate.getWhisperImgCoverTextEdit();

		String coverStr = whisperCoverTextViewStatusUpdateDelegate.getWhisperImgCoverTextString();

		if (TextUtils.isEmpty(coverStr)) {
			Toast.makeText(whisperCoverTextViewStatusUpdateNeedParams.getContext(),"文字内容不能为空", Toast.LENGTH_SHORT).show();
		} else {
			
			SoftInputUtils.closeInput(whisperCoverTextViewStatusUpdateNeedParams.getContext(),whisperImgCoverTextEdit);

			whisperImgCoverText.setText(coverStr);

			whisperImgCoverText.measure(0, 0);

			int textWidth = whisperImgCoverText.getMeasuredWidth();
			int textHegiht = whisperImgCoverText.getMeasuredHeight();

			// System.out.println("\r\ntext width : " + textWidth + " height : " + textHegiht);

			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			if (lastTop != 0) {

				// 计算显示行数，并且 top + textheight < imgShowHeight

				// System.out.println("bottomMargin : " + bottomMargin);

				int rowCount = textWidth % getImgShowWidth() == 0 ? textWidth / getImgShowWidth() : textWidth / getImgShowWidth() + 1;
						

				int textViewHeight = textHegiht * rowCount;// 文字显示需要高度

				// System.out.println("r\nlastTextHeight : " + lastTextHeight + " textViewHeight : " + textViewHeight + " imgShowHeight : " + imgShowHeight);

				if (rowCount > 1 && lastTextHeight != textViewHeight) {

					if (lastTop + textViewHeight > getImgShowHeight() - getTextBottomPading()) {
							
						lastTop = lastTop - (textViewHeight - lastTextHeight);

						if (lastTop < getTextTopPadding()) {
							lastTop = getTextTopPadding();
						}
					}

				}

				lastTextHeight = textViewHeight;

				if ((textWidth + lastLeft) > getImgShowWidth()) {// 自动设置起始点x
					
					lastLeft = lastLeft - (textWidth + lastLeft - getImgShowWidth());
							
					if (lastLeft < 0) {
						lastLeft = 0;
					}
				}

				lp.leftMargin = lastLeft;
				lp.topMargin = lastTop;

				// System.out.println("lastLeft : " + lastLeft + " lastTop : " + lastTop);

				whisperImgCoverText.layout(0, 0, 0, 0);

			} else {
				lp.addRule(RelativeLayout.CENTER_IN_PARENT);
			}

			whisperImgCoverText.setLayoutParams(lp);

			whisperImgCoverText.setVisibility(View.VISIBLE);
			whisperImgCoverTextEdit.setVisibility(View.GONE);

		}
	}

	// --------------------------------------文字 move

	int lastLeft;
	int lastTop;
	int lastRight;
	int lastBottom;
	int lastTextHeight;

	public void initTextMove() {

		final TextView whisperImgCoverText = whisperCoverTextViewStatusUpdateDelegate
				.getWhisperImgCoverText();

		// 给这个TextView注册一个触摸的监听事件
		whisperImgCoverText.setOnTouchListener(new OnTouchListener() {

			// 记录手指在屏幕上的开始坐标
			int startX;
			int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				// 手指第一次触摸屏幕对应的事件
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();// 相对于屏幕
					startY = (int) event.getRawY();
					break;
				// 手指在屏幕上移动
				case MotionEvent.ACTION_MOVE:

					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					int dx = newX - startX;
					int dy = newY - startY;

					int newl = whisperImgCoverText.getLeft() + dx;
					int newt = whisperImgCoverText.getTop() + dy;
					int newr = whisperImgCoverText.getRight() + dx;
					int newb = whisperImgCoverText.getBottom() + dy;
					
					// 判断 位置是否合法。

					if (newl < 0) {
						newl = 0;
						newr = newl + whisperImgCoverText.getWidth();
					}

					if (newr > getImgShowWidth()) {
						newr = getImgShowWidth();
						newl = newr - v.getWidth();
					}

					if (newt < getTextTopPadding()) {
						newt = getTextTopPadding();
						newb = newt + whisperImgCoverText.getHeight();
					}

					if (newb > getImgShowHeight() - getTextBottomPading()) {
						newb = getImgShowHeight() - getTextBottomPading();
						newt = newb - whisperImgCoverText.getHeight();
					}

					// System.out.println("\r\nnewl : " + newl + " newt : " + newt + " newr : " + newr + " newb : " + newb);

					lastLeft = newl;
					lastTop = newt;
					lastRight = newr;
					lastBottom = newb;

					whisperImgCoverText.layout(newl, newt, newr, newb);

					// 重新计算手指开始坐标
					startX = (int) event.getRawX();// 相对于屏幕
					startY = (int) event.getRawY();

					break;
				// 手指离开屏幕的瞬间
				case MotionEvent.ACTION_UP:

					mesureTextViewSizeAndPostion();
					
					// System.out.println("\r\n\r\n ACTION_UP lastLeft : " + lastLeft + " lastTop : " + lastTop);

					break;
				}

				return false;
			}
		});
	}

	private int getImgShowWidth() {
		return whisperCoverTextViewStatusUpdateNeedParams.getImgShowWidth();
	}

	private int getImgShowHeight() {
		return whisperCoverTextViewStatusUpdateNeedParams.getImgShowHeight();
	}

	private int getTextTopPadding() {
		return whisperCoverTextViewStatusUpdateNeedParams.getTextTopPadding();
	}

	private int getTextBottomPading() {
		return whisperCoverTextViewStatusUpdateNeedParams.getTextBottomPading();
	}

}
