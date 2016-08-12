package com.examonline.entity;

import java.util.Date;

public class Answer extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	private int id;//��������������
	private String answer;//�û��ύ�Ĵ�����
	private boolean isTrue;//�ж��ύ�Ĵ��Ƿ�����ȷ��
	private String type;//��������ͣ���ѡ����߶�ѡ��
	private Date examdate;
	
	//�����ֶ�----���Ǽ�����ϵ�Ĳ�ѯ��
	private String stuNum;//����ѧ����ѧ��
	private int LessonId;//ѡ��Ŀ��Կγ�
	private int taotiId;//��Ӧ�γ��µ�ĳһ���Ծ�����1��Ԫ����
	private int questionId;//�Ծ�����Ŀ��id
	
	
	public Date getExamdate() {
		return examdate;
	}
	public void setExamdate(Date examdate) {
		this.examdate = examdate;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public boolean getIsTrue() {
		return isTrue;
	}
	public void setIsTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStuNum() {
		return stuNum;
	}
	public void setStuNum(String stuNum) {
		this.stuNum = stuNum;
	}
	public int getLessonId() {
		return LessonId;
	}
	public void setLessonId(int lessonId) {
		LessonId = lessonId;
	}
	public int getTaotiId() {
		return taotiId;
	}
	public void setTaotiId(int taotiId) {
		this.taotiId = taotiId;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	//������TOString�ķ��������ڵ���ʱ�ļ��
	@Override
	public String toString() {
		return "Answer [id=" + id + ", answer=" + answer + ", isTrue=" + isTrue + ", type=" + type + ", examdate="
				+ examdate + ", stuNum=" + stuNum + ", LessonId=" + LessonId + ", taotiId=" + taotiId + ", questionId="
				+ questionId + "]";
	}
	
	
	
}
