package org.slsale.service.function;

import java.util.List;

import javax.annotation.Resource;

import org.slsale.dao.function.FunctionMapper;
import org.slsale.dao.user.UserMapper;
import org.slsale.pojo.Authority;
import org.slsale.pojo.Function;
import org.slsale.pojo.User;
import org.springframework.stereotype.Service;

@Service
public class FunctionServiceImpl implements FunctionService {
	
	@Resource
	private FunctionMapper functionMapper;

	public List<Function> getMainFunctionList(Authority authority) throws Exception {
		// TODO Auto-generated method stub
		return functionMapper.getMainFunctionList(authority);
	}

	public List<Function> getSubFunctionList(Function function) throws Exception {
		// TODO Auto-generated method stub
		return functionMapper.getSubFunctionList(function);
	}

	public List<Function> getSubFunList(Function function) throws Exception {
		// TODO Auto-generated method stub
		
		return functionMapper.getSubFunList(function);
	}

	public Function getFunctionById(Function function) throws Exception {
		// TODO Auto-generated method stub
		return functionMapper.getFunctionById(function);
	}

	public List<Function> getFunctionListByIn(String sqlInString)
			throws Exception {
		// TODO Auto-generated method stub
		return functionMapper.getFunctionListByIn(sqlInString);
	}

	public List<Function> getFunctionListByRoId(Authority authority)
			throws Exception {
		// TODO Auto-generated method stub
		return functionMapper.getFunctionListByRoId(authority);
	}

	
	
	

}
