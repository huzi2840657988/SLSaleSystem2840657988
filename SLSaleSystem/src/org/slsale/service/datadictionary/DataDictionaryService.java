package org.slsale.service.datadictionary;

import java.util.List;

import org.slsale.pojo.DataDictionary;

public interface DataDictionaryService {

		/**
		 * 获取证件类型列表
		 * @return
		 * @throws Exception
		 */
		public List<DataDictionary> getCardtTypeList(DataDictionary dataDictionary) throws Exception;
	
}
