package org.slsale.service.user;

import java.util.List;

import javax.annotation.Resource;

import org.slsale.dao.user.UserMapper;
import org.slsale.pojo.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	
	@Resource
	private UserMapper userMapper;

	public User getLoginUser(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getLoginUser(user);
	}

	public int loginCodeIsExit(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.loginCodeIsExit(user);
	}

	public int modifyUser(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.modifyUser(user);
	}
	//count
	public int count(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.count(user);
	}
	//getUserList
	public List<User> getUserList(User user)throws Exception{
		
		return userMapper.getUserList(user);
	}
	//添加用户
	
	public void addUser(User user)throws Exception{
		
		userMapper.addUser(user);
	}
	//delUserPic
	public int delUserPic(User user)throws Exception{
		return userMapper.delUserPic(user);
		
	}
	//delUser
	public int delUser(User user)throws Exception{
			return userMapper.delUser(user);
			
		}
	public User getUser(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getUser(user);
	}

	public int modifyUserRole(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.modifyUserRole(user);
	}

	public List<User> getUserListBySearch(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getUserListBySearch(user);
	}
		
}
