package com.tj.matriximageandmovetext.vo;

public class WhisperPublishNeedParams{
	
	private String saveTempPicPath;// 保存未处理的jpg文件路径
	private String saveWaitUploadPicPath;// 保存已处理待上传的jpg文件路径

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

}
