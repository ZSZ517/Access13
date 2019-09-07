package com.example.access1.bean;

import java.util.List;

public class MyBrokenBean {

	private int code;
//	private ConcreteData concreteData;
	private List<ConcreteData> data;
	private String message;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public class ConcreteData {

		private String groupId;
		private String sno;

		private String studentName;

		private String badTime;
		private int type;

		public String getGroup_id() {
			return groupId;
		}

		public void setGroup_id(String groupId) {
			this.groupId = groupId;
		}

		public String getSno() {
			return sno;
		}

		public void setSno(String sno) {
			this.sno = sno;
		}

		public String getStudentName() {
			return studentName;
		}

		public void setStudentName(String studentName) {
			this.studentName = studentName;
		}

		public String getBadTime() {
			return badTime;
		}

		public void setBadTime(String badTime) {
			this.badTime = badTime;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

	}

//	public ConcreteData getConcreteData() {
//		return concreteData;
//	}
//
//	public void setConcreteData(ConcreteData concreteData) {
//		this.concreteData = concreteData;
//	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ConcreteData> getData() {
		return data;
	}

	public void setData(List<ConcreteData> data) {
		this.data = data;
	}

	
}
