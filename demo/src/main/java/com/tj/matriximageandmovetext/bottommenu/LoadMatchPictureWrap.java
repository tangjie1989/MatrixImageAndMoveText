package com.tj.matriximageandmovetext.bottommenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.tj.matriximageandmovetext.R;
import com.tj.matriximageandmovetext.base.ImageLoadListenerAbastractImpl;
import com.tj.matriximageandmovetext.base.ImageLoadingConfig;
import com.tj.matriximageandmovetext.base.WhisperBigPhotoLoadWrap;
import com.tj.matriximageandmovetext.vo.WhisperMatchPictureVo;
import com.tj.matriximageandmovetext.widget.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

public class LoadMatchPictureWrap {
	
	public LoadMatchPictureWrap(
			LoadMatchPictureWrapNeedParams loadMatchPictureWrapNeedParams,
			LoadMatchPictureWrapDelegate loadMatchPictureWrapDelegate) {
		super();
		this.loadMatchPictureWrapNeedParams = loadMatchPictureWrapNeedParams;
		this.loadMatchPictureWrapDelegate = loadMatchPictureWrapDelegate;
		setNeedLoadMatchPicture(true);
	}

	private LoadMatchPictureWrapNeedParams loadMatchPictureWrapNeedParams;
	private LoadMatchPictureWrapDelegate loadMatchPictureWrapDelegate;
	
	public interface LoadMatchPictureWrapDelegate{
		
		public ImageView getWhisperShowImageView();
		
		public void updateMatchWhisperCoverText(TextView coverText);
		
		public void setWhisperShowImageViewScaleType(ScaleType scaleType);
		
		public void updateWhisperShowImageView(Bitmap showImg);
		
		public void updateWhisperShowImageId(String imgId);
		
	}
	
	private boolean isNeedLoadMatchPicture = true;//是否需要load匹配图标识 主题图片发布不需要/普通悠悠话发布需要
	
	public boolean isNeedLoadMatchPicture() {
		return isNeedLoadMatchPicture;
	}

	public void setNeedLoadMatchPicture(boolean isNeedLoadMatchPicture) {
		this.isNeedLoadMatchPicture = isNeedLoadMatchPicture;
	}
	
	private ImageView getWhisperImageView(){
		return loadMatchPictureWrapDelegate.getWhisperShowImageView();
	}

	//---------------------------loading view------------------
	
	private View loadingView;
	private ProgressWheel progressWheel;
	private Button cancelLoad;
	
	private void initLoadingView(){
		
		loadingView = loadMatchPictureWrapNeedParams.getInflater().inflate(R.layout.activity_whisper_publish_new_loading_layout, null);
		
		progressWheel = (ProgressWheel)loadingView.findViewById(R.id.whisper_load_match_picture_progress);
		cancelLoad = (Button)loadingView.findViewById(R.id.whisper_cancel_load_match_picture);
		
		cancelLoad.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageLoader.getInstance().cancelDisplayTask(getWhisperImageView());
				loadMatchPictureWrapNeedParams.getWhipserImgPublishContainer().removeView(loadingView);
			}
		});
		
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		loadMatchPictureWrapNeedParams.getWhipserImgPublishContainer().addView(loadingView, lp);
		
	}
	
	private ArrayList<WhisperMatchPictureVo> matchPics = new ArrayList<WhisperMatchPictureVo>();
	
	//加载匹配图数据
	public void loadMatchPictureUrls(String content, final Context cxt){
		
		isNeedLoadMatchPicture = false;
		
		initLoadingView();

		WhisperMatchPictureVo p1 = new WhisperMatchPictureVo("1", "http://scimg.jb51.net/allimg/160317/14-16031G13220936.jpg");
		WhisperMatchPictureVo p2 = new WhisperMatchPictureVo("2", "http://img15.3lian.com/2015/f2/50/d/72.jpg");
		WhisperMatchPictureVo p3 = new WhisperMatchPictureVo("3", "http://img15.3lian.com/2015/f2/50/d/71.jpg");
		WhisperMatchPictureVo p4 = new WhisperMatchPictureVo("4", "http://img15.3lian.com/2015/f2/50/d/75.jpg");
		WhisperMatchPictureVo p5 = new WhisperMatchPictureVo("5", "http://www.17sucai.com/upload/510819/2016-04-01/593fed2c895834e8908a0bce7c6af7b2.jpg");

		matchPics.add(p1);
		matchPics.add(p2);
		matchPics.add(p3);
		matchPics.add(p4);
		matchPics.add(p5);

		loadLastImg();

//			System.out.println("\r\nloadMatchPictureUrls : " + content);
		
//		HashMap<String, String> bodyMap = new HashMap<String, String>();
//
//		bodyMap.put("content", content);
//
//		StringEntity stringEntity = RequestPostJsonWrap
//				.generateCommonPostEntity(loadMatchPictureWrapNeedParams.getContext(),
//						loadMatchPictureWrapNeedParams.getWhisperPublishNeedParams().getLoadMatchPicRequestType(),
//						loadMatchPictureWrapNeedParams.getWhisperPublishNeedParams().getPostUrlParams().get(WhisperPublishNeedParams.USER_TOKEN), null, bodyMap);
//
//		if(stringEntity == null){
//			return;
//		}
//
//		WhisperPublishRequestClient.post(loadMatchPictureWrapNeedParams
//				.getContext(), loadMatchPictureWrapNeedParams
//				.getWhisperPublishNeedParams().getProjectDataInterfaceUrl(),
//				stringEntity, new TextHttpResponseHandler() {
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
////				System.out.println("\r\nprogress : " + (int)(360.0*25/100) + responseString);
//
//				progressWheel.setProgress((int)(360.0*25/100));
//				progressWheel.setText("25%");
//
//				WhisperMatchPictureBody resultBody = WhisperPublishJsonParser.getWhisperMatchPictureResultFromJson(responseString, cxt);
//
//				if(resultBody != null){
//
//					ResultHeaderVo resultVo = resultBody.getHeader();
//					WhisperMatchPictureListVo whisperMatchPictureListVo = resultBody.getBody();
//
//					if(resultVo != null){
//
//						String result = resultVo.getResult();
//
//						if("1".equals(result)){ //先判断返回结果
//
//							ArrayList<WhisperMatchPictureVo> matchPhotos = whisperMatchPictureListVo.getPhoto();
//							if(matchPhotos != null && matchPhotos.size() > 0){
//								matchPics.addAll(matchPhotos);
//
//								loadLastImg();
//							}
//							return;
//
//						}else{
//
//							Toast.makeText(loadMatchPictureWrapNeedParams.getContext(), result, Toast.LENGTH_SHORT).show();
//							return;
//						}
//
//					}
//
//				}
//
//				Toast.makeText(loadMatchPictureWrapNeedParams.getContext(), R.string.whipser_publish_request_failure, Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onFinish() {
//			}
//
//		});
		
	}

	
	//--------------------------load last img---------------------
	
	private void loadLastImg(){
		
		String url = matchPics.get(matchPics.size() - 1).getUrl();
		
		ImageLoader.getInstance().displayImage(url, getWhisperImageView(),null,
				new ImageLoadListenerAbastractImpl() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						if(loadingView != null && loadingView.getParent() != null){
							initMatchPictureList();
						}
						loadMatchPictureWrapDelegate.updateWhisperShowImageId(matchPics.get(matchPics.size() - 1).getId());
					}
				}, new ImageLoadingProgressListener() {

					@Override
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {

						int preProgress = 90;
						int picLoadPro = (int) (current * 270.0 / total);

						progressWheel.setProgress(picLoadPro + preProgress);
						progressWheel.setText((int) ((picLoadPro + preProgress) * 100.0 / 360.0)+ "%");

					}
				});
		
	}
	
	
	//--------------------------big photo browser------------------
	
	private RelativeLayout viewPagerContainer;
	private ViewPager viewPager;
	private MyPagerAdapter myPagerAdapter;
	
	private static int TOTAL_COUNT = 3;
	
	public boolean isMatchPictureListExits(){
		
		if(matchPics != null && matchPics.size() > 0 && viewPager != null){
			operationMatchPictureList();
			return true;
		}
		
		return false;
		
	}
	
	private void operationMatchPictureList(){
		
		if(isMatchListViewShowing()){
			hiddeMatchPictureList();
		}else{
			
			if(myPagerAdapter != null){
				myPagerAdapter.notifyDataSetChanged();
			}
			
			updateCurrentPageText(viewPager.getCurrentItem());
			
			showMatchPictureList();
		}
		
	}
	
	public void hiddeMatchPictureList(){
		if(!isMatchListViewNull() && isMatchListViewShowing()){
			viewPagerContainer.setVisibility(View.GONE);
		}
	}
	
	private void showMatchPictureList(){
		if(!isMatchListViewNull() && !isMatchListViewShowing()){
			viewPagerContainer.setVisibility(View.VISIBLE);
		}
	}
	
	private boolean isMatchListViewNull(){
		return viewPagerContainer == null ? true : false;
	}
	
	private boolean isMatchListViewShowing(){
		return (viewPagerContainer.getVisibility() == View.VISIBLE) ? true : false;
	}
	
	private void initMatchPictureList(){
		
		ViewGroup whipserImgPublishContainer = (ViewGroup)loadMatchPictureWrapNeedParams.getWhipserImgPublishContainer();
		
		viewPager = (ViewPager) whipserImgPublishContainer.findViewById(R.id.view_pager);
		viewPagerContainer = (RelativeLayout) whipserImgPublishContainer.findViewById(R.id.pager_layout);
		myPagerAdapter = new MyPagerAdapter();
		viewPager.setAdapter(myPagerAdapter);
		// to cache all page, or we will see the right item delayed
		viewPager.setOffscreenPageLimit(TOTAL_COUNT);
		viewPager.setPageMargin(loadMatchPictureWrapNeedParams.getContext().getResources().getDimensionPixelSize(R.dimen.whipser_publish_page_margin));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		viewPagerContainer.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// dispatch the events to the ViewPager, to solve the problem
				// that we can swipe only the middle view.
				return viewPager.dispatchTouchEvent(event);
			}
		});
		
		whipserImgPublishContainer.removeView(loadingView);
		
	}
	
	private class MyPagerAdapter extends PagerAdapter {
		
		@Override
		public int getCount() {
			return matchPics.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			
			WhisperMatchPictureVo vo = matchPics.get(position);
			
			RelativeLayout viewContainer = (RelativeLayout)loadMatchPictureWrapNeedParams.getInflater().inflate(R.layout.activity_whisper_publish_new_match_picture_list_item,null);
			
			viewContainer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					matchPictrueClick(position);
				}
			});
			
			ImageView imageView = (ImageView)viewContainer.findViewById(R.id.whisper_publish_photo);
			
			TextView coverText = (TextView)viewContainer.findViewById(R.id.whisper_img_cover_text);
			coverText.setTag("text" + position);
			
			WhisperBigPhotoLoadWrap whisperBigPhotoLoadWrap = new WhisperBigPhotoLoadWrap(
					imageView, vo.getUrl(),
					ImageLoadingConfig.generateDisplayImageOptions(), position,
					new WhisperBigPhotoLoadWrapDelegateImpl());
			
			whisperBigPhotoLoadWrap.initLoadProgressWheel(viewContainer);
			whisperBigPhotoLoadWrap.loadWhisperBigPhoto();
			
			((ViewPager) container).addView(viewContainer, 0);
			return viewContainer;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((RelativeLayout) object);
		}
	}
	 
	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			updateCurrentPageText(position);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			if (viewPagerContainer != null) {
				viewPagerContainer.invalidate();
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	private class WhisperBigPhotoLoadWrapDelegateImpl implements WhisperBigPhotoLoadWrap.WhisperBigPhotoLoadWrapDelegate {

		@Override
		public void loadImgComplete(int viewPagerCurPostion) {
			
			TextView coverText = (TextView)viewPager.findViewWithTag("text" + viewPagerCurPostion);
			if(coverText != null){
				coverText.setVisibility(View.VISIBLE);
			}
			
			updateCurrentPageText(viewPagerCurPostion);
		}
	}
	
	private void updateCurrentPageText(int position){
		
		TextView coverText = (TextView)viewPager.findViewWithTag("text" + position);
		
		if(coverText != null){
			
			loadMatchPictureWrapDelegate.updateMatchWhisperCoverText(coverText);
			
			WhisperMatchPictureVo vo = matchPics.get(position);
			if(isCacheContainersBitmap(vo.getUrl())){
				coverText.setVisibility(View.VISIBLE);
			}
		}
	}
	
	private void matchPictrueClick(int postion){
		
		//取消之前load
		ImageLoader.getInstance().cancelDisplayTask(getWhisperImageView());
		
		String url = matchPics.get(postion).getUrl();
		
		loadMatchPictureWrapDelegate.setWhisperShowImageViewScaleType(ScaleType.CENTER_CROP);
		
		List<Bitmap> cacheBitmaps = MemoryCacheUtils.findCachedBitmapsForImageUri(url, ImageLoader.getInstance().getMemoryCache());
		
		if(isCacheContainersBitmap(url)){
			hiddeMatchPictureList();
			loadMatchPictureWrapDelegate.updateWhisperShowImageView(cacheBitmaps.get(0));
			loadMatchPictureWrapDelegate.updateWhisperShowImageId(matchPics.get(postion).getId());
			return;
		}
		
		ImageLoader.getInstance().displayImage(url, getWhisperImageView(),
				ImageLoadingConfig.generateDisplayImageOptionsWithNotClear(),
				new ImageLoadListenerAbastractImpl() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						hiddeMatchPictureList();
					}
				});

	}
	
	private boolean isCacheContainersBitmap(String url){
		
		List<Bitmap> cacheBitmaps = MemoryCacheUtils.findCachedBitmapsForImageUri(url, ImageLoader.getInstance().getMemoryCache());
		
		if(cacheBitmaps != null && cacheBitmaps.size() > 0){
			Bitmap cacheMap = cacheBitmaps.get(0);
			if(cacheMap != null && !cacheMap.isRecycled()){
				return true;
			}
		}
		
		return false;
	}
	
}
