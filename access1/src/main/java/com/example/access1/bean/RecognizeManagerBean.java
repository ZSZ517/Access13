package com.example.access1.bean;

public class RecognizeManagerBean {

	private int code;
	private ConcreteData concreteData;
	private String message;

	public static class ConcreteData {
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

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getDept() {
			return dept;
		}

		public void setDept(String dept) {
			this.dept = dept;
		}

		public String getMajor() {
			return major;
		}

		public void setMajor(String major) {
			this.major = major;
		}

		public String getSchoolTime() {
			return schoolTime;
		}

		public void setSchoolTime(String schoolTime) {
			this.schoolTime = schoolTime;
		}

		public String getGrade() {
			return grade;
		}

		public void setGrade(String grade) {
			this.grade = grade;
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

		public String getBadTime() {
			return badTime;
		}

		public void setBadTime(String badTime) {
			this.badTime = badTime;
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

		public void setScore(float score) {
			this.score = score;
		}

		public String getStudentName() {
			return studentName;
		}

		public void setStudentName(String studentName) {
			this.studentName = studentName;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getVisitTime() {
			return visitTime;
		}

		public void setVisitTime(String visitTime) {
			this.visitTime = visitTime;
		}

		private String group_id;
		private String instructorId;
		private String phone;
		private String dept;
		private String major;
		private String schoolTime;
		private String grade;
		private String ralation;
		private String relativeName;
		private String relativeId;
		private String badTime;
		private String sex;
		private String sno;
		private float score;

		private String studentName;
		private int type;
		private String visitTime;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
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
