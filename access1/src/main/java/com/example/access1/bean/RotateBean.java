package com.example.access1.bean;

import java.io.Serializable;

//Õº∆¨¿¥‘¥¿‡
public class RotateBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4799109916113339974L;
	private int imgId;
	private String imgUrl;

	public RotateBean() {
	}

	public RotateBean(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public RotateBean(int imgId) {
		this.imgId = imgId;
	}

	public RotateBean(int imgId, String imgUrl) {
		this.imgId = imgId;
		this.imgUrl = imgUrl;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
