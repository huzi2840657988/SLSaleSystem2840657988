package org.slsale.service.function;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.slsale.pojo.Authority;
import org.slsale.pojo.Function;

public interface FunctionService {
	
	
	//获取主菜单的方法roleId
		public List<Function> getMainFunctionList(Authority authority) throws Exception;
		//获取子菜单
		public List<Function> getSubFunctionList(Function function) throws Exception;
	/**getSubFunList*/
		public List<Function> getSubFunList(Function function) throws Exception;
		public Function getFunctionById(Function function) throws Exception;
		/**
		 * getFunctionListByIn
		 * @param sqlInString
		 * @return
		 * @throws Exception
		 */
		public List<Function> getFunctionListByIn(@Param(value="sqlInString") String sqlInString) throws Exception;
		/**
		 * getFunctionListByRoId
		 * @param function
		 * @return
		 * @throws Exception
		 */
		public List<Function> getFunctionListByRoId(Authority authority) throws Exception;

	
}
