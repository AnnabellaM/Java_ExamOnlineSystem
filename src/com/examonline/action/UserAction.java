package com.examonline.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;

import com.examonline.entity.User;
import com.examonline.service.UserService;

@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User> {

	private static final long serialVersionUID = 1L;
	@Resource
	private UserService userService;
	
	private List<User> users;
	
	

	public List<User> getUsers() {
		return users;
	}


	public void setUsers(List<User> users) {
		this.users = users;
	}


	public String index(){
		//���Ҫ�÷�ҳ����ʾ���ݵĻ�
		this.users=this.userService.getSplitPage(pageInfo);//���pageInfo�Ǹ����
		//Ȼ������˰�pageInfo������ʾ���ݵ�ҳ��
		this.requset.setAttribute("pageBean", pageInfo.getPageBean());
		return "index";
	}
	
	
	public String add(){
		return "add";
	}
	
	
	public String doadd(){
		model.setPassword(DigestUtils.md5DigestAsHex(model.getPassword().getBytes()));
		userService.saveEntity(model);
		return "doadd";
	}
	
	public String del(){
		userService.delteEntity(model);
		return "del";
	}
	public String edit(){
		model=userService.getEntity(model.getId());
		return "edit";
	}
	
	public String doedit(){
		if(model.getPassword().length()<20){
			model.setPassword(DigestUtils.md5DigestAsHex(model.getPassword().getBytes()));
		}
		userService.saveOrUpdate(model);
		return "doadd";
	}
	
}
