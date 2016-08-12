package com.examonline.action;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.examonline.entity.Lesson;
import com.examonline.service.LessonService;

@Controller
@Scope("prototype")
public class LessonAction extends BaseAction<Lesson> {

	private static final long serialVersionUID = 1L;

	@Resource
	private LessonService lessonService;
	
	private List<Lesson> lessons;
	

	public String lesson(){
		//���Ҫ�÷�ҳ����ʾ���ݵĻ�
		this.lessons=this.lessonService.getSplitPage(pageInfo);//���pageInfo�Ǹ����
		//Ȼ������˰�pageInfo������ʾ���ݵ�ҳ��
		this.requset.setAttribute("pageBean", pageInfo.getPageBean());;
		return "lesson";
	}
	
	public List<Lesson> getLessons() {
		
		return lessons;
	}
	
	
	public String lessonAdd (){
		return "lessonAdd";
	}

	public String doAdd(){
		this.lessonService.saveEntity(model);
		return "redirect";
	}
	
	public String doDel(){
		this.lessonService.delteEntity(model);
		return "redirect";
	}
	
}
