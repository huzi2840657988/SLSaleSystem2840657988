package org.slsale.dao.datadictionary;

import java.util.List;

import org.slsale.pojo.DataDictionary;

public interface DataDictionaryMapper {
	/**
	 * 获取证件类型列表
	 * @return
	 * @throws Exception
	 */
	public List<DataDictionary> getCardtTypeList(DataDictionary dataDictionary) throws Exception;
}
