package com.tj.matriximageandmovetext.vo;

/**
 * 上传图片返回body(非http协议body 而是自定义内容中的body)
 * 
 * @author tangjie
 * 
 */
public class UploadWhisperPhotoResultVo {

	private String pid;
	private String url;
	private String url_1;
	private String url_5;
	private String url_6;
	private String result;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl_1() {
		return url_1;
	}

	public void setUrl_1(String url_1) {
		this.url_1 = url_1;
	}

	public String getUrl_5() {
		return url_5;
	}

	public void setUrl_5(String url_5) {
		this.url_5 = url_5;
	}

	public String getUrl_6() {
		return url_6;
	}

	public void setUrl_6(String url_6) {
		this.url_6 = url_6;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "UploadWhisperPhotoResultVo [pid=" + pid + ", url=" + url
				+ ", url_1=" + url_1 + ", url_5=" + url_5 + ", url_6=" + url_6
				+ ", result=" + result + "]";
	}

}
