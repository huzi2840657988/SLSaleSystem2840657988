package org.slsale.service.role;

import java.util.List;

import javax.annotation.Resource;

import org.slsale.dao.role.RoleMapper;
import org.slsale.dao.user.UserMapper;
import org.slsale.pojo.Role;
import org.slsale.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleMapper  roleMapper;
	@Resource
	private UserMapper userMapper;

	
	//获取角色列表
	public List<Role> getRoleList()throws Exception{
		
		return roleMapper.getRoleList();
		
	}


	public Role getRoleR(Role role) throws Exception {
		// TODO Auto-generated method stub
		return roleMapper.getRoleR(role);
	}


	public Role getRole(Role role) throws Exception {
		// TODO Auto-generated method stub
		return roleMapper.getRole(role);
	}


	public int addRole(Role role) throws Exception {
		// TODO Auto-generated method stub
		return roleMapper.addRole(role);
	}


	public int modifyRole(Role role) throws Exception {
		// TODO Auto-generated method stub
		return roleMapper.modifyRole(role);
	}


	public int deleteRole(Role role) throws Exception {
		// TODO Auto-generated method stub
		return roleMapper.deleteRole(role);
	}


	public List<Role> getRoleIdAndNameList() throws Exception {
		// TODO Auto-generated method stub
		return roleMapper.getRoleIdAndNameList();
	}


	public boolean hl_modifyRole(Role role) throws Exception {
		roleMapper.modifyRole(role);
		int roleId=role.getId();
		String roleName=role.getRoleName();
		User user=new User();
		user.setRoleId(roleId);
		user.setRoleName(roleName);
		if(null != roleName && !"".equals(roleName)){
			userMapper.modifyUser(user);
		}
		
		return true;
	}

	
	
	

}
