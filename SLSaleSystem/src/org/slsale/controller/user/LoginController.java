package org.slsale.controller.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slsale.common.Constants;
import org.slsale.common.RedisAPI;
import org.slsale.pojo.Authority;
import org.slsale.pojo.Function;
import org.slsale.pojo.Menu;
import org.slsale.pojo.User;
import org.slsale.service.function.FunctionService;
import org.slsale.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LoginController extends BaseController {
	
	@Resource
	private UserService userService;
	@Resource
	private FunctionService functionService;
	@Resource
	private RedisAPI redisAPI;
	
	
	@RequestMapping("/main.html")
	public ModelAndView main(HttpSession session){
		//权限设置：获得当前的用户信息动态获取菜单加载信息--list
		User user =this.getCurrentUser();
		logger.debug("===============>"+user.getLoginCode()+
				user.getPassword());
		List<Menu> mList=null;
		if(null != user){
			Map<String,Object> model=new HashMap<String,Object>();
			model.put("user", user);
			//根据当前用户获取list菜单列
			//使用redis缓存直接获取登陆的用户信息和显示列表：
			/**
			 * key:"menuList"+roleID(user.getRoleId())---eg:"menuList2"
			 * value:mList
			 */
			
		//分redis中无数据
			if(!redisAPI.exist("menuList"+user.getRoleId())){
				mList=getFuncByCurrentUser(user.getRoleId());
				//json数据对象转换为json数据字符串数组
				if(null !=mList){
					JSONArray jsonArray=JSONArray.fromObject(mList);
					String jsonString =jsonArray.toString();
					logger.debug("*********jsonString ***********: " + jsonString);
					model.put("mList", jsonString);
					//放进redis
					redisAPI.Set("menuList"+user.getRoleId(), jsonString);
					//model 中存入列表信息包含当前的数据用户和对应的信息菜单列表：
				}
		//分redis中有数据:---->直接从redis数据中取
			}else{
				String redisMenuKeyString=redisAPI.get("menuList"+user.getRoleId());
				logger.debug("***********menuList"+user.getRoleId()+" from redis*************:"+redisMenuKeyString);
				//判断取出的数据
				if(null != redisMenuKeyString && !"".equals(redisMenuKeyString)){
					model.put("mList", redisMenuKeyString);
				}else{
					return new ModelAndView("redirect:/");
				}
			}
			//判断并加载权限列表信息
			/**
			 * get all role url list to redis
			 * 防止越过请求修改信息
			 * */
			if(!redisAPI.exist("Role"+user.getRoleId()+"UrlList")){
				try {
					Authority authority= new Authority();
					authority.setRoleId(user.getRoleId());
					List<Function> functionList=functionService.getFunctionListByRoId(authority);
					if(null != functionList || functionList.size()>=0 ){
						StringBuffer sBuff=new StringBuffer();
						for (Function function : functionList) {
							sBuff.append(function.getFuncUrl());
						}
						redisAPI.Set("Role"+user.getRoleId()+"UrlList", sBuff.toString());
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			session.setAttribute(Constants.SESSION_BASE＿MODEL,model);
			return new ModelAndView("main",model);
		}
		return new  ModelAndView("redirect:/");
		
	}
	//用户角色id获取list的展示列表信息
	protected List<Menu> getFuncByCurrentUser(int roleId){
		//菜单列表
		List<Menu> menuList=new ArrayList<Menu>();
		//权限
		Authority authority =new Authority();
		authority.setRoleId(roleId);
		try {
			List<Function> mList=functionService.getMainFunctionList(authority);
			if(mList != null){
				for(Function function: mList){
					Menu menu =new Menu();
					menu.setMainMenu(function);
					
					function.setRoleId(roleId);
					List<Function> subList=functionService.getSubFunctionList(function);
					if(null !=subList){
						menu.setSubMenu(subList);
					}
					menuList.add(menu);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return menuList;
	}
	
	
	//ajax的json对象异步加载
	@RequestMapping("/login.html")
	@ResponseBody
	public Object login(HttpSession session,@RequestParam String user){
		logger.debug("======insert into login.html method=========>");
		if(user == null || "".equals(user)){
			return "nodata";
		}else{
			JSONObject userObject =JSONObject.fromObject(user);
			User userObj=(User) userObject.toBean(userObject,User.class);
		//
			try {
				if(userService.loginCodeIsExit(userObj)==0){
					return "nologincode";
				}else{
					User _user=userService.getLoginUser(userObj);
					if(null!=_user){
						session.setAttribute(Constants.SESSION_USER, _user);
						//lastLoginTime
						User updateLoginTimeUser=new User();
						updateLoginTimeUser.setId(_user.getId());
						updateLoginTimeUser.setLastLoginTime(new Date());
						userService.modifyUser(updateLoginTimeUser);
						updateLoginTimeUser=null;
						
						return "success";
					}else{
						
						return "pwderror";
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "failed";
			}
		}
		
	}
	
	@RequestMapping("/logout.html")
	public String loginOut(HttpSession session){
		session.removeAttribute(Constants.SESSION_USER);
		session.invalidate();
		this.setCurrentUser(null);
		return "index";
	}
	
	//无权访问
	@RequestMapping("/401.html")
	public ModelAndView noRole(){
		return new ModelAndView("401");
	}
	
	
}
