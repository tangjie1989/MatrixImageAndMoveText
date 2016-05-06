package com.tj.matriximageandmovetext.vo;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class WhisperPublishNeedParams implements Parcelable {
	
	//将此对象通过intent传过来时的key
	public static final String INTENT_KEY = "whisper_publish_need_params";
	
	//postUrlParams 所有参数对应的key
	public static final String USER_TOKEN = "token";//用户访问服务器的身份标识
	public static final String REQUEST_SRC = "src";//应用渠道标识
	public static final String REQUEST_VERSION = "version";//应用版本号
	public static final String PUBLISH_TYPE = "method";//图片上传类型，每个应用可能不一样
	public static final String PUBLISH_PROPERTY = "labelid";//图片所带label属性 1抒个情2求解忧3搞个笑
	
	public static final String SUBJECT_ID = "subjectid";//主题id，如果是上传主题悠悠话则带这个参数，否则忽略
	
	//悠悠话发布主题图片时，如果是从相册选完图片过来，则必须带上选择的图片地址。
	private String imgPath;

	private String ttfLocalSavePath;// 已转换完成的字体文件保存路径
	private String saveTempPicPath;// 保存未处理的jpg文件路径
	private String saveWaitUploadPicPath;// 保存已处理待上传的jpg文件路径
	private String projectDataInterfaceUrl;// 工程普通接口全局地址
	private String projectImgInterfaceUrl;// 工程图片接口全局地址
	
	private String loadMatchPicRequestType;// 加载匹配图接口地址
	
	private HashMap<String, String> postUrlParams;// url请求所带参数每个工程发布悠悠话所带参数可能不一样

	public String getTtfLocalSavePath() {
		return ttfLocalSavePath;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public void setTtfLocalSavePath(String ttfLocalSavePath) {
		this.ttfLocalSavePath = ttfLocalSavePath;
	}

	public String getSaveTempPicPath() {
		return saveTempPicPath;
	}

	public void setSaveTempPicPath(String saveTempPicPath) {
		this.saveTempPicPath = saveTempPicPath;
	}

	public String getSaveWaitUploadPicPath() {
		return saveWaitUploadPicPath;
	}

	public void setSaveWaitUploadPicPath(String saveWaitUploadPicPath) {
		this.saveWaitUploadPicPath = saveWaitUploadPicPath;
	}

	public String getProjectDataInterfaceUrl() {
		return projectDataInterfaceUrl;
	}

	public void setProjectDataInterfaceUrl(String projectDataInterfaceUrl) {
		this.projectDataInterfaceUrl = projectDataInterfaceUrl;
	}

	public String getProjectImgInterfaceUrl() {
		return projectImgInterfaceUrl;
	}

	public void setProjectImgInterfaceUrl(String projectImgInterfaceUrl) {
		this.projectImgInterfaceUrl = projectImgInterfaceUrl;
	}

	public String getLoadMatchPicRequestType() {
		return loadMatchPicRequestType;
	}

	public void setLoadMatchPicRequestType(String loadMatchPicRequestType) {
		this.loadMatchPicRequestType = loadMatchPicRequestType;
	}

	public HashMap<String, String> getPostUrlParams() {
		return postUrlParams;
	}

	public void setPostUrlParams(HashMap<String, String> postUrlParams) {
		this.postUrlParams = postUrlParams;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public WhisperPublishNeedParams() {
		super();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(imgPath);
		dest.writeString(ttfLocalSavePath);
		dest.writeString(saveTempPicPath);
		dest.writeString(saveWaitUploadPicPath);
		dest.writeString(projectDataInterfaceUrl);
		dest.writeString(projectImgInterfaceUrl);
		dest.writeString(loadMatchPicRequestType);

		Bundle b = new Bundle();
		b.putSerializable("postUrlParams", postUrlParams);
		dest.writeBundle(b);
	}

	public WhisperPublishNeedParams(Parcel in) {

		imgPath = in.readString();
		ttfLocalSavePath = in.readString();
		saveTempPicPath = in.readString();
		saveWaitUploadPicPath = in.readString();
		projectDataInterfaceUrl = in.readString();
		projectImgInterfaceUrl = in.readString();
		loadMatchPicRequestType = in.readString();

		Bundle b = in.readBundle();
		postUrlParams = (HashMap<String, String>) b
				.getSerializable("postUrlParams");

	}

	public static Creator<WhisperPublishNeedParams> getCreator() {
		return CREATOR;
	}

	public static void setCreator(
			Creator<WhisperPublishNeedParams> creator) {
		CREATOR = creator;
	}

	public static Creator<WhisperPublishNeedParams> CREATOR = new Creator<WhisperPublishNeedParams>() {
		public WhisperPublishNeedParams createFromParcel(Parcel in) {
			return new WhisperPublishNeedParams(in);
		}

		public WhisperPublishNeedParams[] newArray(int size) {
			return new WhisperPublishNeedParams[size];
		}
	};
}
