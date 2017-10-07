package org.slsale.controller.user;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.slsale.common.Constants;
import org.slsale.common.RedisAPI;
import org.slsale.pojo.Authority;
import org.slsale.pojo.Function;
import org.slsale.pojo.Menu;
import org.slsale.pojo.Role;
import org.slsale.pojo.RoleFunctions;
import org.slsale.pojo.User;
import org.slsale.service.authority.AuthorityService;
import org.slsale.service.function.FunctionService;
import org.slsale.service.role.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class AuthorityController extends BaseController {
	
	@Resource
	private RoleService roleService;
	@Resource
	private FunctionService functionService;
	@Resource
	private AuthorityService authorityService;
	@Resource
	private LoginController loginController;
	@Resource
	private RedisAPI redisAPI;
	
	
	@RequestMapping(value="/backend/authoritymanage.html")
	public ModelAndView Authority(HttpSession session,Model model){
		Map<String,Object> baseModel= (Map<String, Object>) session.getAttribute(Constants.SESSION_BASE＿MODEL);
		if(null == baseModel){
			return  new ModelAndView("redirect:/");
		}else{
			//get roleList
			List <Role> roleList =null;
			try {
				roleList=roleService.getRoleIdAndNameList();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				roleList=null;
			}
			model.addAllAttributes(baseModel);
			model.addAttribute("roleList", roleList);
			
			return new ModelAndView("/backend/authoritymanage");
		}
		
	
	}
	
	@RequestMapping(value="/backend/functions.html", produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public Object functionList(){
		
		String result = "nodata";
		
		Function function = new Function();
		function.setId(0);
		try {
			List <Function> flist=functionService.getSubFunList(function);
			List <RoleFunctions> roleFunctions=new ArrayList<RoleFunctions>();
			if(flist != null){
				//循环功能菜单
				for(Function fun : flist){
					RoleFunctions rfun=new RoleFunctions();
					rfun.setMainFunction(fun);
					rfun.setSubFunctions(functionService.getSubFunList(fun));
					roleFunctions.add(rfun);
				}
				result =JSONArray.fromObject(roleFunctions).toString();
				logger.debug("resultString=====>"+result);
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	//回显加载当前角色已有的功能(checkbox)--"/backend/getAuthorityDefault.html"
		@RequestMapping(value = "/backend/getAuthorityDefault.html", produces = {"text/html;charset=UTF-8"})
		@ResponseBody
		public Object getAuthorityDefault(@RequestParam Integer rid,@RequestParam Integer fid){
			String resultString = "nodata";
			try {
				Authority authority = new Authority();
				authority.setRoleId(rid);
				authority.setFunctionId(fid);
				if(authorityService.getAuthority(authority) != null){
					resultString =  "success";
				}
			} catch (Exception e) {
			}
			return resultString;
		}
		//先获取当前用户权限，然后更新操作(redis)：将该角色下的所有功能先删除后授权
		@RequestMapping(value = "/backend/modifyAuthority.html", produces = {"text/html;charset=UTF-8"})
		@ResponseBody
		public Object modifyAuthority(HttpSession session,@RequestParam String ids){
			String resultString = "nodata";
			try {
				if(null != ids){
					String[] idsArrayString =StringUtils.split(ids,"-");
					if(idsArrayString.length > 0){
						User user = (User)session.getAttribute(Constants.SESSION_USER);
						authorityService.hl_addAuthority(idsArrayString,user.getLoginCode());
						//获取主菜单并使用redis API存入
						List <Menu> mList = null;
						mList = loginController.getFuncByCurrentUser(Integer.valueOf(idsArrayString[0]));
						//转换为JSON串
						JSONArray jsonArray =JSONArray.fromObject(mList);
						redisAPI.Set("menuList"+idsArrayString[0], jsonArray.toString());
						//get all role url list to redis
						Authority authority = new Authority();
						authority.setRoleId(Integer.valueOf(idsArrayString[0]));
						List<Function> functionList=functionService.getFunctionListByRoId(authority);
						if(null != functionList || functionList.size() >= 0 ){
							StringBuffer sBuff=new StringBuffer();
							for (Function function : functionList) {
								sBuff.append(function.getFuncUrl());
							}
							redisAPI.Set("Role"+idsArrayString[0]+"UrlList", sBuff.toString());
						}
						resultString =  "success";
					
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return resultString;
		}
}
