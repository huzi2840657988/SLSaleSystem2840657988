package org.slsale.controller.user;


import java.util.Date;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


import net.sf.json.JSONObject;

import org.slsale.common.Constants;
import org.slsale.pojo.Role;
import org.slsale.pojo.User;
import org.slsale.service.role.RoleService;
import org.slsale.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class RoleController extends BaseController {
	
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	
	@RequestMapping("/backend/rolelist.html")
	public ModelAndView roleList(HttpSession session,Model model){
		logger.debug("=== insert into rolelist method ==========> "+model);
		Map<String,Object> baseModel= (Map<String,Object>)session.getAttribute(Constants.SESSION_BASE＿MODEL);
		
		if(baseModel == null){
			return new ModelAndView("redirect:/");
		}else{
			List<Role> roleList = null;
			Role role = new Role();
			try {
				roleList = roleService.getRoleList();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				roleList = null;
				e.printStackTrace();
			}
			model.addAllAttributes(baseModel);
			model.addAttribute(roleList);
			return new ModelAndView("/backend/rolelist");
		}
	}	
/**添加角色*/
	@RequestMapping("/backend/addRole.html")
	@ResponseBody
	public Object addRole(HttpSession session,@RequestParam String role){
		logger.debug("=== insert into addRole method ==========> "+role);
		if(null == role || "".equals(role)){
			return "nodata";
		}else{
			JSONObject roleObject = JSONObject.fromObject(role);
			Role roleObj = (Role)JSONObject.toBean(roleObject,Role.class);
			roleObj.setCreateDate(new Date());
			roleObj.setIsStart(1);
			//???????
			roleObj.setCreatedBy(((User)session.getAttribute(Constants.SESSION_USER)).getLoginCode());
			try{
				if(roleService.getRoleR(roleObj) !=  null){
					return "rename";
				}else{
					roleService.addRole(roleObj);
				}
			}catch(Exception e){
				e.printStackTrace();
				return "failed";
			}
			return "success";
		}
		
	}
/**
 * 删除角色
 * 先判断对应传入的角色是否有用户信息存在
 * 如果该角色下存在用户则无法删除；
 * 无法删除时展示该角色下用户信息列表
 * 
 * delRole
 * */
	@RequestMapping("/backend/delRole.html")
	@ResponseBody
	public Object delRole(HttpSession session,@RequestParam String role){
		logger.debug("*** ****insert into delRole method ==========> "+role);
		if(null == role || "".equals(role)){
			return "nodata";
		}else{
			JSONObject jobj=JSONObject.fromObject(role);
			Role roleobj=(Role) JSONObject.toBean(jobj, Role.class);
			try{
				User u= new User();
				u.setRoleId(roleobj.getId());
				List <User> ulist=null;
				ulist=userService.getUserListBySearch(u);
				if(null==ulist || ulist.size()==0){
					roleService.deleteRole(roleobj);
				}else{
					String flag="";
					for (int i=0;i<ulist.size();i++) {
					  flag+=ulist.get(i).getLoginCode();
					  flag+=",";
					}
					return flag;
				}
			}catch(Exception e){
				e.printStackTrace();
				return "failed";
			}
		}
		return "success";
	}
/**
 * 修改角色表
 * 前台将数据打包转化为json字符串传入；
 * /backend/modifyRole.html
 * 判断传入的数据是否为空
 * 先将前台数据转化为JAVA对象；
 * 传入修改时间及修改人信息（当前用户）
 * */
	@RequestMapping("/backend/modifyRole.html")
	@ResponseBody
	public Object modifyRole(HttpSession session,@RequestParam String role){
			
			if(null == role || "".equals(role)){
				return "nodata";
			}else{
				JSONObject jobj=JSONObject.fromObject(role);
				Role roleObj=(Role) jobj.toBean(jobj, Role.class);
				roleObj.setCreateDate(new Date());
				//roleObj.setIsStart(1);
				roleObj.setCreatedBy(this.getCurrentUser().getLoginCode());
				
				try {
					roleService.hl_modifyRole(roleObj);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return "failed";
				}
			}
			return "success";
	}
	
	
	
}
