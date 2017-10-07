package org.slsale.dao.user;


import java.util.List;

import org.slsale.pojo.User;

public interface UserMapper {
	/**
	 * getLoginUser
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User getLoginUser(User user)throws Exception;
	/**
	 * getUser
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User getUser(User user)throws Exception;
	/**
	 * loginCodeIsExit
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int loginCodeIsExit(User user)throws Exception;
	//修改
	public int modifyUser(User user)throws Exception;
	//计算用户总条数
	public int count(User user);
	//getUserList获取用户列表
	public List<User> getUserList(User user)throws Exception;
	//添加用户
	
	public void addUser(User user)throws Exception;
	//delUserPic
	public int delUserPic(User user)throws Exception;
	//delUser
	public int delUser(User user)throws Exception;	
	//modifyUserRole
	public int modifyUserRole(User user)throws Exception;	
	//getUserListBySearch
	public List<User> getUserListBySearch(User user)throws Exception;
	
}
