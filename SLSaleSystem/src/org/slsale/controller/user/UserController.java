package org.slsale.controller.user;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slsale.common.Constants;
import org.slsale.common.JsonDateValueProcessor;
import org.slsale.common.PageSupport;
import org.slsale.common.SQLTools;
import org.slsale.pojo.DataDictionary;
import org.slsale.pojo.Role;
import org.slsale.pojo.User;
import org.slsale.service.datadictionary.DataDictionaryService;
import org.slsale.service.role.RoleService;
import org.slsale.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;


@Controller
public class UserController extends BaseController {
	
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	@Resource
	private DataDictionaryService dataDictionaryService;
	
	//密码修改
	@RequestMapping(value="/backend/modifyPwd.html")
	@ResponseBody
	public Object modifyPassword(@RequestParam String userJson){
		logger.debug("=====insert into modifyPassword===========>");
		//从前台获取password 和password2 （放入json对像并转换后判断）；
		//调用后台的查询方法比对password;
		//根据相应的查询结果返回对应的ajax 返回值result
			User sessionUser = this.getCurrentUser();
			if(userJson == null || userJson.equals("")){
				return "nodata";
			}else{
				JSONObject userObject = JSONObject.fromObject(userJson);
				User user = (User)JSONObject.toBean(userObject,User.class);
				user.setId(sessionUser.getId());
				user.setLoginCode(sessionUser.getLoginCode());
				try {
					if(userService.getLoginUser(user)!=null){
						logger.debug("====修改前密码："+user.getPassword());
						user.setPassword(user.getPassword2());
						
						user.setPassword2(null);
						userService.modifyUser(user);
						logger.debug("====修改后密码："+user.getPassword());
					}else{
						return "oldpwdwrong";
					}
				} catch (Exception e) {
					// TODO: handle exception
					return "failed";
				}
			}
			return "success";
		}
		
		/**
		 * 获取用户列表（分页查询）
		 *  starNum;//分页的起始行
		 *  pageSize;//每页显示多少行
		 * @return
		 */
	@RequestMapping("/backend/userlist.html")	
	public ModelAndView userList(HttpSession session,Model model,
			//跳转当前页
			@RequestParam(value="current",required=false)Integer current,
			//显示的当前页
			@RequestParam(value="currentPage",required=false) Integer currentPage,
			//推荐人
			@RequestParam(value="s_referCode",required=false) String s_referCode,
			//登录名
			@RequestParam(value="s_loginCode",required=false) String s_loginCode,
			//角色编码
			@RequestParam(value="s_roleId",required=false) String s_roleId,
			//是否可用
			@RequestParam(value="s_isStart",required=false) String s_isStart){
		Map<String, Object> baseModel =	(Map<String, Object>)session.getAttribute(Constants.SESSION_BASE＿MODEL);
		if(baseModel == null){
			return  new ModelAndView("redirect:/");
		}else {
		//roleList:  //cardTypeList
			DataDictionary dataDictionary=new DataDictionary();
			dataDictionary.setTypeCode("CARD_TYPE");
			List<DataDictionary> cardTypeList =null;
			
			List<Role> roleList=null;
			try {
				roleList=roleService.getRoleList();
				cardTypeList=dataDictionaryService.getCardtTypeList(dataDictionary);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//用户对象植入模糊查询信息
			User user=new User();
			if(null !=s_referCode){
				user.setReferCode("%"+SQLTools.transfer(s_referCode)+"%");
			}
			if(null !=s_loginCode){
				user.setLoginCode("%"+SQLTools.transfer(s_loginCode)+"%");
			}
			
			if(!StringUtils.isNullOrEmpty(s_roleId)){
				user.setRoleId(Integer.valueOf(s_roleId));
			}else{
				user.setRoleId(null);
			}
			
			if(!StringUtils.isNullOrEmpty(s_isStart)){
				user.setIsStart(Integer.valueOf(s_isStart));
			}else{
				user.setIsStart(null);
			}
		//分页
			logger.debug("=====>current"+current);
			PageSupport page = new PageSupport();
			try {
				int count=userService.count(user);
				//总页数
				page.setTotalCount(count);
			} catch (Exception e) {
				
				e.printStackTrace();
				page.setTotalCount(0);//置空
			}
			logger.debug("page.getTotalCount()=========>"+page.getTotalCount());
			//对总页数判断后分页
			int totals=page.getTotalCount();
			int totalages=page.getPageCount();
			if(totals > 0){
				if(current !=null)
					page.setPage(current);
				if(page.getPage() <=0)
					page.setPage(1);
				if(page.getPage() >totalages)
					page.setPage(totalages);
				//计算设置起始页码
				user.setStarNum((page.getPage()-1)*page.getPageSize() );
				//获取页面容量
				user.setPageSize(page.getPageSize());
				logger.debug("user.getPageSize()=============== " + user.getPageSize());
				logger.debug("user.getStarNum()=============== " + user.getStarNum());
				List<User> userList=null;
				//查询用户列表
				try {
					userList=userService.getUserList(user);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					userList=null;
					if(page==null){
						page =new PageSupport();
						page.setItems(null);
					}
					e.printStackTrace();
				}
				logger.debug("userList=====>"+userList.size());
				page.setItems(userList);
			}else {
				page.setItems(null);
			}
			model.addAllAttributes(baseModel);
			model.addAttribute("roleList",roleList);
			model.addAttribute("cardTypeList", cardTypeList);
			model.addAttribute("page",page);
			model.addAttribute("s_loginCode",s_loginCode);
			model.addAttribute("s_referCode",s_referCode);
			model.addAttribute("s_isStart",s_isStart);
			model.addAttribute("s_roleId",s_roleId);
			return  new ModelAndView("/backend/userlist");
			
		}
	
	}
	/**
	 * 获取用户类型列表
	 * */
	@RequestMapping(value="/backend/loadUserTypeList.html",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public Object loadUserTypeList(@RequestParam(value="s_roleId",required=false) String s_roleId){
		String cjson=null;
		DataDictionary dataDictionary=new DataDictionary();
		dataDictionary.setTypeCode("USER_TYPE"); 
		
		try {
			List<DataDictionary> userTypeList =dataDictionaryService.getCardtTypeList(dataDictionary);
			JSONArray jo=JSONArray.fromObject(userTypeList);
			cjson=jo.toString();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return cjson;
	}
	
	/**
	 * 验证用户是否存在
	 * 
	 * */
	
	@RequestMapping(value="/backend/logincodeisexit.html",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public String  loginCodeIsExist(@RequestParam(value="loginCode",required=false) String loginCode,
									@RequestParam(value="id",required=false) String id){
		
		String result="fialed";
		logger.debug("=======>logincodeisexist of loginCode :"+loginCode);
		logger.debug("=======>logincodeisexist of id :"+id);
		User _user=new User();
		_user.setLoginCode(loginCode);
		if(! id.equals("-1")){//修改时同名判断
			_user.setId(Integer.valueOf(id));
		}
		try {
			if(userService.loginCodeIsExit(_user) == 0){
				result="only";
			}else{
				result="repeat";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	return result;
	}
	/***
	 * 添加用户
	 * 路径： /backend/adduser.html
	 * 
	 * */
	@RequestMapping(value="/backend/adduser.html",method = RequestMethod.POST)
	public ModelAndView addUser(HttpSession session,@ModelAttribute("addUser") User addUser){
		if(session.getAttribute(Constants.SESSION_BASE＿MODEL) == null){
			return new ModelAndView("redirect:/");
		}else{
			try {
				String idCard = addUser.getIdCard();
				String pwod=idCard.substring(idCard.length()-6);	
				addUser.setPassword(pwod);
				addUser.setPassword2(pwod);
				addUser.setCreateTime(new Date());
				addUser.setReferCode(this.getCurrentUser().getLoginCode());
				addUser.setReferId(this.getCurrentUser().getId());
				addUser.setLastUpdateTime(new Date());
				userService.addUser(addUser);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("redirect:/backend/userlist.html");
		}
	}
	/**
	 * 
	 * 文件上传
	 * 
	 * */
	@RequestMapping(value = "/backend/upload.html", produces = {"text/html;charset=UTF-8"})  
	@ResponseBody
    public Object upload(@RequestParam(value = "a_fileInputID", required = false) MultipartFile cardFile, 
    		             @RequestParam(value = "a_fileInputBank", required = false) MultipartFile bankFile, 
    		             @RequestParam(value = "m_fileInputID", required = false) MultipartFile mCardFile, 
    		             @RequestParam(value = "m_fileInputBank", required = false) MultipartFile mBankFile, 
    		             @RequestParam(value = "loginCode", required = false) String loginCode, 
    					 HttpServletRequest request,HttpSession session) {  
  
        logger.debug("comeing uploadFile start!!");
        //根据服务器的操作系统，自动获取物理路径，自动适应
        String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");  
        logger.debug("the whole path======== " + path);
        List<DataDictionary> list = null;
        DataDictionary dataDictionary = new DataDictionary();
        dataDictionary.setTypeCode("PERSONALFILE_SIZE");
        try {
			list = dataDictionaryService.getCardtTypeList(dataDictionary);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        int filesize = 50000;
        if(null != list){
        	 if(list.size() == 1){
             	filesize = Integer.valueOf(list.get(0).getValueName());
             }
        }
        //添加身份卡
        if(cardFile != null){
        	String oldFileName = cardFile.getOriginalFilename();//原文件名
            String prefix=FilenameUtils.getExtension(oldFileName);//后缀
            logger.debug("prefix the bankFile prefix======== " + prefix);
            if(cardFile.getSize() >  filesize){//上传大小
            	return "1";

            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
            		|| prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){
            	//给文件重命名：系统毫秒数+100W以内的随机数
            	String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_Idcard.jpg";  
                logger.debug("new fileName======== " + cardFile.getName());
                File targetFile = new File(path, fileName);  
                if(!targetFile.exists()){  
                    targetFile.mkdirs();  
                }  
                //保存  
                try {  
                	cardFile.transferTo(targetFile);  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
                String url = request.getContextPath()+"/statics/uploadfiles/"+fileName;
                logger.debug("fileName of url======== " + url);
                return url;  
            }else{
            	return "2";
            }
        }
        //添加银行卡
        if(bankFile != null){
        	String oldFileName = bankFile.getOriginalFilename();//获取原文件名
            String prefix=FilenameUtils.getExtension(oldFileName);//取文件后缀
            logger.debug("prefix the bankFile prefix======== " + prefix);
            if(bankFile.getSize() >  filesize){//上传大小
            	return "1";

            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
            		|| prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){
            	//给文件重命名：系统毫秒数+100W以内的随机数
            	String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_Idbank.jpg";  
                logger.debug("new fileName======== " + bankFile.getName());
                File targetFile = new File(path, fileName);  
                if(!targetFile.exists()){  
                    targetFile.mkdirs();  
                }  
                //保存  
                try {  
                	bankFile.transferTo(targetFile);  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
                String url = request.getContextPath()+"/statics/uploadfiles/"+fileName;
                logger.debug("fileName of url bankFile======== " + url);
                return url;  
            }else{
            	return "2";
            }
        }
        //修改--m_fileInputID---mCardFile
        if(mCardFile != null){
        	String oldFileName = mCardFile.getOriginalFilename();//获取原文件名
            String prefix=FilenameUtils.getExtension(oldFileName);//取文件后缀
            logger.debug("prefix the mCardFile prefix======== " + prefix);
            if(mCardFile.getSize() >  filesize){//上传大小
            	return "1";

            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
            		|| prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){
            	//给文件重命名：系统毫秒数+100W以内的随机数
            	String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_IdCard.jpg";  
                logger.debug("new fileName======== " + mCardFile.getName());
                File targetFile = new File(path, fileName);  
                if(!targetFile.exists()){  
                    targetFile.mkdirs();  
                }  
                //保存  
                try {  
                	mCardFile.transferTo(targetFile);  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
                String url = request.getContextPath()+"/statics/uploadfiles/"+fileName;
                logger.debug("fileName of url mCardFile======== " + url);
                return url;  
            }else{
            	return "2";
            }
        }
        
        if(mBankFile != null){
        	String oldFileName = mBankFile.getOriginalFilename();//获取原文件名
            String prefix=FilenameUtils.getExtension(oldFileName);//取文件后缀
            logger.debug("prefix the mBankFile prefix======== " + prefix);
            if(mBankFile.getSize() >  filesize){//上传大小
            	return "1";

            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
            		|| prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){
            	//给文件重命名：系统毫秒数+100W以内的随机数
            	String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_Idbank.jpg";  
                logger.debug("new fileName======== " + mBankFile.getName());
                File targetFile = new File(path, fileName);  
                if(!targetFile.exists()){  
                    targetFile.mkdirs();  
                }  
                //保存  
                try {  
                	mBankFile.transferTo(targetFile);  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
                String url = request.getContextPath()+"/statics/uploadfiles/"+fileName;
                logger.debug("fileName of url mCardFile======== " + url);
                return url;  
            }else{
            	return "2";
            }
        }
		return null;
	}
	
	/**
	 * 删除图片
	 * 前台参数:id,closeSpan,uploadBtn,obj,picpath,picText,fileinputid
	 * backend/delpic.html
	 * */
	
	@RequestMapping(value="/backend/delpic.html",produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public String delpic(@RequestParam(value="id",required=false)String id,
			@RequestParam(value="picpath",required=false)String picpath,
			HttpServletRequest request,HttpSession session){
		logger.debug("===into delpic method =============="+picpath);
		String result = "failed";
		if(picpath == null || picpath.equals("")){
			return "success";
		}else{
		//解析前台路径成物理路径
			//文件分割
			String[] paths= picpath.split("/");
			//路径适应
			String path =request.getSession().getServletContext().getRealPath(paths[2]+File.separator+paths[3]+File.separator+paths[4]);
			//String path=session.getServletContext().getRealPath(paths[2]+File.separator+paths[3]);
			//通过将给定路径名字符串转换成抽象路径名来创建一个新 File 实例
			logger.debug("========="+path);
			
			File file = new File(path);
			logger.debug(path);
			if(file.exists()){//文件存在
				//delete() :删除此抽象路径名表示的文件或目录== boolean
				//deleteOnExit()  在虚拟机终止时，请求删除此抽象路径名表示的文件或目录。void
				if( file.delete() ){
					if(id.equals("0")){
						result = "success";
					}else{//修改操作时删除
						User _user =new User();
						_user.setId(Integer.valueOf(id));
						//判断文件后缀名并赋值
						logger.debug("======="+picpath.indexOf("_Idcard.jpg")); 
						if(picpath.indexOf("_Idcard.jpg") != -1 ){
							_user.setIdCardPicPath(picpath);
						}else if(picpath.indexOf("_Idbank.jpg") != -1){
							_user.setBankPicPath(picpath);
						}
						//删除操作:
						try {
							int a=userService.delUserPic(_user);
							logger.debug("=============>"+a); 
							if(a>0){
								result = "success";
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return result;
						}
					}
				}
		}
			return result;
			}
	}
	/**
	 * 通过 userId查询用户
	 * 
	 * */
	@RequestMapping(value="/backend/getUser.html",produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public Object getUser(@RequestParam(value="id",required=false)String id){
		String result="";
		if(id == null || "".equals(id)){
			return "nodata";
		}else{
			User user= new User();
			user.setId(Integer.valueOf(id));
			try {
				user=userService.getUser(user);
				//user对象里有日期，所有有日期的属性，
				//都要按照此日期格式进行json转换（对象转json）
				JsonConfig json =new JsonConfig();
				json.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
				JSONObject jo=JSONObject.fromObject(user,json);
				result=jo.toString();
				logger.debug("=============>"+user.getLoginCode()); 
				logger.debug("=============>"+result); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "failed";
			}
			return result;
			
		}
	}
	@RequestMapping(value="/backend/modifyuser.html",method=RequestMethod.POST)
	public ModelAndView modifyUser(HttpSession session,@ModelAttribute("modifyUser") User modifyUser){
		if(session.getAttribute(Constants.SESSION_BASE＿MODEL) == null){
			return new ModelAndView("redirect:/");
		}else{
			try {
				modifyUser.setLastUpdateTime(new Date());
				userService.modifyUser(modifyUser);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("redirect:/backend/userlist.html");
		}
	}
	
	/**
	 * 删除用户
	 * 出入id和两张图片的路径,用户类型userType
	 * 若被删除的用户为：普通消费会员、VIP会员、加盟店  则不可被删除
	 * */
	@RequestMapping(value="/backend/deluser.html",produces = {"text/html;charset=UTF-8"})
	public String deleteUser(
			@RequestParam(value = "delId",required=false) String id,
			@RequestParam(value = "delcardpic",required=false) String cardpic,
			@RequestParam(value = "delbankpic",required=false) String bankpic,
			@RequestParam(value = "deluserType",required=false) String delUserType,
			HttpServletRequest request,HttpSession session){
		String res="failed";
		logger.debug("***********insert into delUserMethod**delUserType===>"+delUserType);
		if(delUserType.equals("2") || delUserType.equals("3") || delUserType.equals("4")){
			res = "noallow";
		}else{
			if(this.delpic(id, cardpic, request, session).equals("success") && this.delpic(id, bankpic, request, session).equals("success")){
				
				try {
					User deluser = new User();
					deluser.setId(Integer.valueOf(id));
					int a=userService.delUser(deluser);
					if(a>0){
						res = "success";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}
}
