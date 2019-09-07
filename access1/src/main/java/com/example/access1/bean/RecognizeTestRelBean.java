package com.example.access1.bean;

public class RecognizeTestRelBean {

	private int code;
	private ConcreteData concreteData;
	private String message;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public static class ConcreteData {

		private String group_id;
		private String instructorId;
		private String sex;
		private String sno;
		private int score;

		private String phone;
		private String ralation;
		private String relativeName;
		private String relativeId;
		private String studentName;

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getRalation() {
			return ralation;
		}

		public void setRalation(String ralation) {
			this.ralation = ralation;
		}

		public String getRelativeName() {
			return relativeName;
		}

		public void setRelativeName(String relativeName) {
			this.relativeName = relativeName;
		}

		public String getRelativeId() {
			return relativeId;
		}

		public void setRelativeId(String relativeId) {
			this.relativeId = relativeId;
		}

		public String getGroup_id() {
			return group_id;
		}

		public void setGroup_id(String group_id) {
			this.group_id = group_id;
		}

		public String getInstructorId() {
			return instructorId;
		}

		public void setInstructorId(String instructorId) {
			this.instructorId = instructorId;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public String getSno() {
			return sno;
		}

		public void setSno(String sno) {
			this.sno = sno;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}

		public String getStudentName() {
			return studentName;
		}

		public void setStudentName(String studentName) {
			this.studentName = studentName;
		}

	}

	public ConcreteData getConcreteData() {
		return concreteData;
	}

	public void setConcreteData(ConcreteData concreteData) {
		this.concreteData = concreteData;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
