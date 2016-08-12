package com.examonline.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;

import com.examonline.entity.Lesson;
import com.examonline.entity.Score;
import com.examonline.entity.Student;
import com.examonline.entity.User;
import com.examonline.service.LessonService;
import com.examonline.service.ScoreService;
import com.examonline.service.StudentService;

@Controller
@Scope("prototype")
public class StudentAction extends BaseAction<Student> implements SessionAware{

	private static final long serialVersionUID = 1L;
	
	private String queryIf;
	
	public String getQueryIf() {
		return queryIf;
	}
	
	List<Score>scores;
	public List<Score> getScores() {
		return scores;
	}

	public void setScores(List<Score> scores) {
		this.scores = scores;
	}

	private String key;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setQueryIf(String queryIf) {
		this.queryIf = queryIf;
	}
	public String logout(){
		this.session.clear();//�˳�ʱҪ���session�е�ֵ
		return "logout";
	}

	@Resource
	private LessonService LessonService;
	
	@Resource
	private ScoreService scoreService;
	
	@Resource
	private StudentService studentService;
	
	private Map<String, Object> session;
	
	private List<Lesson> lessons;
	
	public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	private List<Student>students;
	
	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public String register(){
		return "register";
	}
	
	public String stuAdd() throws IOException{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = df.format(new Date());
		model.setStuNum(date);
		model.setPwd(DigestUtils.md5DigestAsHex(model.getPwd().getBytes()));//����ѧ��ע�������
		this.studentService.saveEntity(model);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=gb2312");
		PrintWriter out;
		
			out = response.getWriter();
			out.print("<script>alert('���Ŀ���:"+ date + "');</script>");
			out.flush();
			return "index";
	}
	
	public String index(){
		return "index";
	}
	
	public String main(){
		return "main";
	}
	
	public String queryExam(){
		String hql="from Score where stuNum=?";
		this.scores=scoreService.findEntiyByHql(hql, model.getStuNum());
		return "queryExam";
	}
	
	public void validateMain(){
		String pwd=model.getPwd();
		model.setPwd(DigestUtils.md5DigestAsHex(pwd.getBytes()));
		Student student = studentService.validateStudentInfo(model);
		if(student!=null){
			this.session.put("student", student);
		}else {
			addActionError("�û������������");
		}
	}
	
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}
	
	/**
	 * ��ѯ�ɼ��Ĵ����
	 * -------seek-------score�����������lesson��
	 *��¼��
	 */
	
	
	
	
	// * -------seek-------score�����������lesson��
	
	
	
	/**
	 * �һ������漰��action����
	 * ------seek---P---W-----D-------
	 */
	private String destpage;
	public String getDestpage() {
		return destpage;
	}
	
	
	public String step1(){
		return "step1";
	}
	
	
	public String step2(){
		return "step2";
	}
	public void validateStep2(){
	String stunum=this.requset.getParameter("stuNum");
	String hql="from Student where stuNum=?";
	List<Student> entity=null;
	entity = studentService.findEntiyByHql(hql, stunum);
	if(entity.isEmpty()){
		this.destpage="seekPwd";
		addActionError("��������������������ϸ�˶ԣ�");
	}else{
		this.model=entity.get(0);
	}
	
}
	
	
	
	public String step3(){
		return "step3";
	}
	public void validateStep3(){
		String question=this.model.getSecQue();
		String answer=this.requset.getParameter("answer");
		List<Student> findEntiy =null;
		if(question!=null&&answer!=null){
			String hql="from Student where secQue=? and secAns=?";
			findEntiy = studentService.findEntiyByHql(hql, question,answer);
			if(findEntiy.isEmpty()){
				this.destpage="seekPwd1";
				addActionError("��������������������ϸ�˶ԣ�");
			}
		}
	}
	
	public String step4(){
		String password=this.requset.getParameter("pwd");
		String stunum=this.model.getStuNum();
		String hql="from Student where stuNum=?";
		List<Student> findEntiy =null;
		findEntiy=studentService.findEntiyByHql(hql, stunum);
		if(!findEntiy.isEmpty()){
			this.model=findEntiy.get(0);
			this.model.setPwd(DigestUtils.md5DigestAsHex(password.getBytes()));
			studentService.updateEntity(model);
		}
		return "step4";
	}
	// * ------seek---P---W-----D-------
	
	
	/**
	 * ������ִ���޸�ѧ�����ϵĴ����
	 * PS����δ����
	 * PSS:���ܴ��ڵ����⣬���Ǽ�ʱˢ�µ�������Ƕδ�����Ҫ����
	 * �������ӡ�
	 * --------Modify------Student-----------
	 */
	public String edit(){
		//String str=this.requset.getParameter("stuNo");
		int id =model.getId();
		String hql="from Student where id=?";
		//ע�⣺�����from xx ....���xx����ʵ�����������������ݿ��ж�Ӧ�ı���
		if(id!=0){
			this.model=studentService.findEntiyByHql(hql, id).get(0);
		}
		//���ҵ������student����student_modify��ҳ��
		return "edit";
	}
	public String finished(){
		String pwd=model.getPwd();
		if(pwd.length()<22){
			model.setPwd(DigestUtils.md5DigestAsHex(pwd.getBytes()));
		}
		
		studentService.updateEntity(this.model);
		return "finished";
	}
	//* --------Modify------Student-----------

	public String showinfo(){
		//���Ҫ�÷�ҳ����ʾ���ݵĻ�
		this.students=this.studentService.getSplitPage(pageInfo);//���pageInfo�Ǹ����
		//Ȼ������˰�pageInfo������ʾ���ݵ�ҳ��
		this.requset.setAttribute("pageBean", pageInfo.getPageBean());
		return "showinfo";
	}
	
	public String del(){
		studentService.delteEntity(model);
		return "showinfo";
	}
	
	public String search(){
		if("stuNo".equals(queryIf)){
			String hql="from Score where stuNum=?";
			scores=scoreService.findEntiyByHql(hql, key);
			
		}
		if("subjectName".equals(queryIf)){
			String lesson="from Lesson where lessonName=?";
			lessons=LessonService.findEntiyByHql(lesson, key);
			if (lessons==null||lessons.size()==0) {
				return "search";
			}
			String hql="from Score where LessonID=?";
			scores=scoreService.findEntiyByHql(hql,lessons.get(0).getId());
			
		}
		return "search";
	}
	
	
}
