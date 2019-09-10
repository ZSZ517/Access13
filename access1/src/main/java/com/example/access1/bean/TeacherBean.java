package com.example.access1.bean;

/**
 * Created by Administrator on 2019/09/07.
 */

public class TeacherBean {
    private String code;
    private String message;
    private ConcreteData data;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public class ConcreteData{
        private String score;
        private String teacherName;
        private String teacherNum;

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getTeacherNum() {
            return teacherNum;
        }

        public void setTeacherNum(String teacherNum) {
            this.teacherNum = teacherNum;
        }
    }
}
