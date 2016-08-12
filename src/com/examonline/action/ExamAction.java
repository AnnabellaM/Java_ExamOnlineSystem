package com.examonline.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.examonline.entity.Answer;
import com.examonline.entity.Question;
import com.examonline.entity.Score;
import com.examonline.entity.Student;
import com.examonline.entity.Taoti;
import com.examonline.service.AnswerService;
import com.examonline.service.QuestionService;
import com.examonline.service.TaotiService;

@Controller
@Scope("prototype")
public class ExamAction extends BaseAction<Question> implements SessionAware,ParameterAware{
	//ParameterAware----�����ڽ�������jspҳ���е����п��в���
	private static final long serialVersionUID = 1L;

	public String examRule(){
		return "examRule";
	}
	
	public String ready(){
		return "ready";
	}

	
	@Resource
	private TaotiService taotiservice;
	private List<Taoti> taotis;
	public List<Taoti> getTaotis(){
		return taotis;
	}
	public String selectLesson(){
		this.taotis = this.taotiservice.findAll();
		return "selectLesson";
	}
	
	/**
	 * ��ʱ�䶯����
	 * ------Time-------Go-----
	 */
	public void showStartTime(){
		try {
			String startTime = (String) this.session.get("startTime");
			long time = Long.parseLong(startTime);
			Date date = new Date(time);
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			String str = format.format(date);
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=utf-8");
			response.getWriter();
			PrintWriter writer = response.getWriter();
			writer.write(str);
			writer.flush();
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} 
	}
	
	public void showRemainTime(){
		try {
			String startTime = session.get("startTime").toString();
			long a = Long.parseLong(startTime);
			Date start = new Date(a);
			Date current = new Date();
			long yyTime = current.getTime()-start.getTime();
			Date total = new Date(20*60*1000);
			long remain = total.getTime()-yyTime-8*3600*1000;
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=utf-8");
			PrintWriter writer = response.getWriter();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			writer.write(format.format(new Date(remain)));
			writer.flush();
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} 
	}
	//-----Time-------Go-----
	
	
	
	
	/**
	 * ��ʼ���Զ�Ӧ��ִ���߼�
	 * ------Exam-------Start--------
	 */
	@Resource
	private QuestionService questionservice;
	
	private List<Question> singleQuestions;//��ѡ��
	private List<Question> multipleQuestions;//��ѡ��
	//�ڷ���examStart���ҵ����ٽ���Щ�⣨��Spring����������get����ע�룩��������ĵ�ѡ���飬��ѡ����
	//Ȼ����ݴ����������taotiID��question���ҵ���Ŀ
	//�������������ô��ݹ�����taotiIdֵૣ�����ע��ķ�ʽ
	//Ŀǰ����question��ķ�ʽ�����---->ֱ������д�Ļ���taotiId��ע�뵽question�е�ָ���ֶ���
	//���������ԡ���ע�뵽����ǰ����������-----����ע�⣺�������ص���model�������Question��
	
	public List<Question> getSingleQuestions() {
		return singleQuestions;
	}
	public List<Question> getMultipleQuestions() {
		return multipleQuestions;
	}

	//���õ�ѡ�⣬��ѡ��ķ���----->�����־�̬��������ʽ������jspҳ�����
	//<s:property value="getQueScore(40,singleQuestions)�ĸ�ʽ���õ�����Ľ����
	public int getQueScore(int total,List<Question> question){
		int itemscore=0;//����ÿ����Ŀ�ķ���
		if(!question.isEmpty()&&question.size()>0){
			 itemscore=total/question.size();
		}
		return itemscore;
	}
	public String startExam(){
		//����������Ҫչʾ�Ծ����⣺������ѡ����ѡ��ÿ��ķ������Լ��ֵܷȵȣ��͵���ʱ��ָ����ҳ������
		//��ʼ����֮ǰ��Ҫ�ɿ���ѡ���taotiID���ҵ���Ӧ�������е���Ŀ���Ͱ�����ѡ�͵�ѡ��
		String hql="from Question where taotiId=? and type=?";
		singleQuestions=this.questionservice.findEntiyByHql(hql, this.model.getTaotiId(),"��ѡ��");
		System.out.println(singleQuestions.toString());
		multipleQuestions=this.questionservice.findEntiyByHql(hql, this.model.getTaotiId(),"��ѡ��");
		System.out.println(multipleQuestions.toString());
		//��¼���Կ�ʼ��ʱ��
		this.session.put("startTime",new Date().getTime()+"");
		/*
		 * ������������ɹ��Ļ�����ô���ܹ����Ծ���ʾ��ҳ����
			<s:iterator var="qs" id="questions" value="singleQuestion" status="st">
			�ȱ�ǩ�����Ŀ
		 */
		return "startExam";//����߼���ͼ��ָ������ת����startExam�Ľ����У�����Ҫ��ǰ�����ݴ���ã�Ȼ��ע��Ľ�
		//Ŀǰ��һ��С������ǣ������ϵ��ύ��ťû����ʾ���������ǵ�ʱ����������ݵ�����
	}
	//* ------Exam-------Start--------

	
	
	/**
	 * ���Խ���֮���ύ��ť������ִ���߼�
	 * ���ǵĹ��̣����𰸱��浽���ݿ��Aswer����---������ɼ�----�����ɼ����ɼ����йؼ��ֶν���һ������
	 * ------Exam---------End----------
	 */
	@Resource
	private AnswerService answerservice;//���������Ĺ���
	//�����漰��Ҫ�ύ��������ҳ�棬�ύ�Ĳ��������;��ǣ�Map<String(��ѡ���߶�ѡ)��String[](�û��ύ�Ĵ����飬��ѡ���߶�ѡ)>
	
	//����֮ǰ�Ĵ����õ�@Resourceע�⣬��Spring˵��Ҫע��������󣬵���Spring���������û���������ģ��ڿ����ύ�Ծ�֮ǰ��
	//�ύ�Ծ�֮����ParameterAware����jspҳ��Ĳ�������
	private Map<String, String[]> questions;//���ڴ�ſ����ύ�Ĵ𰸵Ĳ���
	public String endExam(){
		/**
		 * ˼·��
		 * 1.��ȡ����Ӧ�������ֶΣ���������ѧ�ţ�����׼��֤�ţ������Կγ�ID���γ�������������ѡ��taotiID����������questionID�������֣�������+��ţ�
		 *����Щ����Ҫ����һ��Ԥ����Ĳ���-----����Щ�ֶζ��Ǳ�Ҫ�ģ������Ĳ�����ֻ���õ��˶��ѣ��ȸ������ݿ��еģ����Ա������
		 *�����߼��г��ֲ���Ҫ��������
		 */
		String stuNum=((Student)this.session.get("student")).getStuNum();//���session�д���Ŀ�����׼��֤�ţ�ѧ�ţ�
		Score score=new Score();
		score.setStuNum(stuNum);
		//��ȡ������ID
		score.setPaperID(this.model.getTaotiId());
		//���LessonID���γ�ID
		//��������ID�������������ҵ���Ӧ��lessonID
		Taoti taoti = this.taotiservice.getEntity(this.model.getTaotiId());
		//������ֵ�����ǣ��ҵ������ݿ��ж�Ӧ��taoti�ˣ���������lessonIDΪ0---�����ݿ�����LessonIdֵΪ1
		System.out.println(taoti.toString());
		score.setLessonID(taoti.getLesson().getId());
		//����ע���ˣ����ݿ���taotis��lessonID�ֶ�ֻ����Ϊһ�������ⲿ��lesson�ķǳ־��������ֶΣ�������ʵ�ʵ�ֵ��ֻ����������ã�
		//��������ҪҪ���߶������lessonIDֵ������Ҫ����lessonIDָ������lesson��ȥ��ȡ��������id
		score.setTest_Date(new Date());//���ý���ʱ��
		
		/*��2.��:���𰸵����ݸ�����Ӧ�����ݿ���
		 * ������ɼ����ٸ������ݿ���
		 */
		this.answerservice.saveAnswers(handleAnswer(),score);
		
		return "end";
	}
	
	//�����
	private List<Answer> handleAnswer(){
		List<Answer> results=new ArrayList<Answer>();
		Answer answer=null;//��ʱ�趨һ����Ŵ𰸵�Answer����
		//��ȡ����������ҳ��Ĵ𰸲���
		for(Entry<String, String[]> entry:questions.entrySet()){
			String key=entry.getKey();//�����ύ���������ƣ�qd+id����ѡ����+��Ŀ��꣩ qm+id����ѡ����+��Ӧ����Ŀ��ţ�
			String[] value=entry.getValue();//�����ύ�Ķ�Ӧ��Ŀ�Ĵ�---�������ݶ����ύ����ʽΪ���ַ�����
			answer=new Answer();
			//��������Ҫ���ô�---�������洢�𰸵����ݿ��У���Ϊ��ѡ��Ͷ�ѡ��
			//���￼�ǵ��ύ���������Ĳ���Ϊ�����Ĳ������ͣ�taotiID������ID����hidden����Ԫ�ص���ʽ��һ���ύ
			//����Ҫ���ǵ��ٲ���taotiID���͵Ĳ�������������������������
			String temp="";
			if(!key.contains("taoti")){//�����ȿ��ǵ�ֻ��taotiID��Ϊ����Ԫ�ص����
				if(value.length>0&&value!=null){
					for(int i=0;i<value.length;i++){
						temp=temp+value[i]+",";
					}
				}
//				System.out.println(temp);
				temp=temp.substring(0, temp.length()-1);//������Ҫ�����������������ַ����������һ�����Ÿ�ȥ����--->�ѽ��
					if(key.contains("qd")){//question�������չֵ�ȡ��������ѡ
						answer.setType("��ѡ��");	
						answer.setAnswer(temp);//�ַ���������ַ���
						
					}
					if(key.contains("qm")){//question multiple--->��ѡ�⣨��Ӣ��������������ﰡ��
						answer.setType("��ѡ��");
						answer.setAnswer(temp);
					}
			
			//�������жϴ𰸵���ȷ��
			/*
			 * ˵�������ݿ��д洢�𰸵ķ���Ϊ�����ȷ�𰸶�Ӧ���±꣬��������һ���ַ��������
			 * ˼·���ڸ�ǰ���£��Ϳ��������ݿ�����ȥ�ҵ���Ӧ��Ŀ����ȷ�𰸣�Ȼ�󽫴𰸽��зָ
			 * ���ɷָ�����ȷ�������������ύ�õ���������бȶԣ�Ȼ�����ж�����
			 */
			int questionID=Integer.parseInt(key.substring(2));//��Ϊkeyֵ������Ϊqd5����qm3����ʽ
			answer.setQuestionId(questionID);
			boolean isTrueFlag=isTrueJudge(questionID,value);
			answer.setIsTrue(isTrueFlag);//����Ĵ���ȷֵ����û�Ŷ�Ŷ------��
			results.add(answer);
			answer=null;
			}
		}
		return results;
	}
	
	
	public boolean isTrueJudge(int id, String[] value) {
		//�趨һ�����ڷ��صı��
		boolean flag=true;
		//��Ҫ����ID�ҵ���Ӧ����Ŀ
		Question ques=questionservice.getEntity(id);
		//�õ����
		String rightanswer=ques.getRightAnswer();
		if(rightanswer!=null){
			if(value.length>0&&value!=null){//˵��������ύ������Ч��
				if(rightanswer.contains(",")){
					String[] rightanswerArray=rightanswer.split(",");
					if(value.length!=rightanswerArray.length){
						flag=false;
					}
				}
				
				for(String str:value){
					if(!rightanswer.contains(str)){
						flag=false;
						break;
					}
				}
			}
		}
	return flag;
	}
	//* ------Exam---------End----------
	
	private Map<String, Object> session;
	@Override
	public void setSession(Map<String, Object> session) {
		// TODO �Զ����ɵķ������
		this.session = session;
	}
	
	
	//����ҳ��Ĳ���----����ȡ����ѡ����ĿЩ��questions
	@Override
	public void setParameters(Map<String, String[]> questions) {
		this.questions=questions;
		
	}
}
