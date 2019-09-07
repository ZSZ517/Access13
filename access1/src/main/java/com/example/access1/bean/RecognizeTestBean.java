package com.example.access1.bean;

public class RecognizeTestBean {

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
		private String dept;
		private String grade;
		private String instructorId;
		private String schoolTime;
		private String sex;
		private String sno;
		private float score;

		public String getGroup_id() {
			return group_id;
		}

		public void setGroup_id(String group_id) {
			this.group_id = group_id;
		}

		public String getDept() {
			return dept;
		}

		public void setDept(String dept) {
			this.dept = dept;
		}

		public String getGrade() {
			return grade;
		}

		public void setGrade(String grade) {
			this.grade = grade;
		}

		public String getInstructorId() {
			return instructorId;
		}

		public void setInstructorId(String instructorId) {
			this.instructorId = instructorId;
		}

		public String getSchoolTime() {
			return schoolTime;
		}

		public void setSchoolTime(String schoolTime) {
			this.schoolTime = schoolTime;
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

		public float getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
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
