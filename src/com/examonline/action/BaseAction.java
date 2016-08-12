package com.examonline.action;

import java.lang.reflect.ParameterizedType;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.examonline.util.PageInfo;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>{

	private static final long serialVersionUID = 1L;
	
	protected T model;
	protected HttpServletRequest requset;
	protected PageInfo pageInfo;
	public BaseAction() {
		try {
			ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
			Class<T> clazz=(Class<T>) type.getActualTypeArguments()[0];
			//---------
			/*
			 * ��ҳ��ʾ�Ķ���
			 */
			this.requset=ServletActionContext.getRequest();
			//����ԭ���������صķ�ʽ����ȡ��request�ģ����ǻᱨ��˵���Ҳ���session��һ��Ĵ���û��sessionFactory��Ҳ��û�пɵõ�request����
			//������������ͱ�����ɳ����Լ����ҵ������ŵ��������е�request���󡣡�����������������������Ƿ��ǶԵģ�
			this.pageInfo=new PageInfo(requset);//�������������ҳ���Ǳߴ��ݹ��������ݳ��º�PageBean
			//---------��ҳ��ʾ�Ķ���
			this.model=clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public T getModel() {
		return model;
	}
}
