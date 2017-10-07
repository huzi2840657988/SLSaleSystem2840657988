package org.slsale.dao.function;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.slsale.pojo.Authority;
import org.slsale.pojo.Function;

public interface FunctionMapper {
	//获取主菜单的方法roleId
	public List<Function> getMainFunctionList(Authority authority) throws Exception;
	//获取子菜单
	public List<Function> getSubFunctionList(Function function) throws Exception;
	/**getSubFunList*/
	public List<Function> getSubFunList(Function function) throws Exception;
	
	public List<Function> getFunctionListByIn(@Param(value="sqlInString") String sqlInString);
	
	public Function getFunctionById(Function function);
	
	public List<Function> getFunctionListByRoId(Authority authority);

}
