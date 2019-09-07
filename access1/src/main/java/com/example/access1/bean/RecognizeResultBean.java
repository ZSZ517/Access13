package com.example.access1.bean;

public class RecognizeResultBean {
	private String code;
	private String message;
	private ConcreteData data;
	public class ConcreteData {
		private String groupId;
		private String sno;

		private String studentName;
		private String score;
		private String dept;
		private String schoolTime;
		private String major;
		private String sex;
		private String phone;
		private String relation;
		private String visitTime;
		private String badTime;
		private String relativeName;
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
		public String getScore() {
			return score;
		}
		public void setScore(String score) {
			this.score = score;
		}
		public String getDept() {
			return dept;
		}
		public void setDept(String dept) {
			this.dept = dept;
		}
		public String getSchoolTime() {
			return schoolTime;
		}
		public void setSchoolTime(String schoolTime) {
			this.schoolTime = schoolTime;
		}
		public String getMajor() {
			return major;
		}
		public void setMajor(String major) {
			this.major = major;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getGroupId() {
			return groupId;
		}
		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}
		public String getVisitTime() {
			return visitTime;
		}
		public void setVisitTime(String visitTime) {
			this.visitTime = visitTime;
		}
		public String getRelation() {
			return relation;
		}
		public void setRelation(String relation) {
			this.relation = relation;
		}
		public String getBadTime() {
			return badTime;
		}
		public void setBadTime(String badTime) {
			this.badTime = badTime;
		}
		public String getRelativeName() {
			return relativeName;
		}
		public void setRelativeName(String relativeName) {
			this.relativeName = relativeName;
		}
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ConcreteData getData() {
		return data;
	}
	public void setData(ConcreteData data) {
		this.data = data;
	}
}
