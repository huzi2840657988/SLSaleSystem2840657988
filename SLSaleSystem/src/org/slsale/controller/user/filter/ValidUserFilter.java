package org.slsale.controller.user.filter;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ValidUserFilter implements javax.servlet.Filter {

	public void destroy() {
		
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request =(HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		//获取session
		HttpSession session = request.getSession();
		if(session.getAttribute("user")==null){
			//未登录
			String msg = "<script>alert('未登录,请登录!');top.location.href='login.jsp'</script>";
			response.getWriter().print(msg);
		}else{
			arg2.doFilter(arg0, arg1);//已登录
		}
	}

	
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
