package com.example.access1.bean;

import java.util.List;

public class VisitBean {

	private String code;
	private String message;
	private List<ConcreteData> data;

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

	public List<ConcreteData> getData() {
		return data;
	}

	public void setData(List<ConcreteData> data) {
		this.data = data;
	}

	public class ConcreteData {
		private String groupId;
		private String relation;
		private String relativeName;
		private String sno;
		private String studentName;
		private int type;
		private String visitTime;

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public String getRelation() {
			return relation;
		}

		public void setRelation(String relation) {
			this.relation = relation;
		}

		public String getRelativeName() {
			return relativeName;
		}

		public void setRelativeName(String relativeName) {
			this.relativeName = relativeName;
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

	}

}
