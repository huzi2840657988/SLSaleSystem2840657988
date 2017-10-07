package org.slsale.service.user;

import java.util.List;

import org.slsale.pojo.User;

public interface UserService {
	//查询用户
	public User getLoginUser(User user)throws Exception;
	/**
	 * loginCodeIsExit
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int loginCodeIsExit(User user)throws Exception;
	
	//修改密码
	public int modifyUser(User user)throws Exception;
	 
	//count users
	public int count(User user)throws Exception;
	
	//getUserList
	public List<User> getUserList(User user)throws Exception;
	//添加用户
	
	public void addUser(User user)throws Exception;
	//delUserPic
	public int delUserPic(User user)throws Exception;
	//delUserPic
	public int delUser(User user)throws Exception;
	//getUser
	public User getUser(User user)throws Exception;
	//modifyUserRole
	public int modifyUserRole(User user)throws Exception;	
	//getUserListBySearch
	public List<User> getUserListBySearch(User user)throws Exception;
	
}
