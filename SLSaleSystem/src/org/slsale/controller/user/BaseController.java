package org.slsale.controller.user;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.annotation.InitBinder;

import org.apache.log4j.Logger;
import org.slsale.common.Constants;
import org.slsale.pojo.User;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class BaseController {
	
	Logger logger = Logger.getLogger(BaseController.class);
	
	private User currentUser;//生成对应的set/get方法

	//抽取获得当前类的方法
	
	
	public User getCurrentUser() {
		
		if(null == this.currentUser){
			HttpServletRequest request=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			HttpSession session=request.getSession(false);
			if(null != session ){
				currentUser=(User)session.getAttribute(Constants.SESSION_USER);
			}
		}
		return currentUser;
	}
	
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
/**
 * 日期国际化
 * */
	@InitBinder
	public void InitBinder(WebDataBinder databinder){
		//表单时间处理
		databinder.registerCustomEditor(Date.class, new PropertyEditorSupport(){
			public String getAsText() {
				
				return new SimpleDateFormat("yyyy-MM-dd").format((Date)getValue());
			}

			public void setAsText(String value){
				try {
					setValue(new SimpleDateFormat("yyyy-MM-dd").parse(value));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setValue(null);
				}
				
			}
		});
			
		
	}
	/*@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}*/
	
	
}


