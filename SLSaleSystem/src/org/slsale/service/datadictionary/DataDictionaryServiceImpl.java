package org.slsale.service.datadictionary;

import java.util.List;

import javax.annotation.Resource;

import org.slsale.dao.datadictionary.DataDictionaryMapper;
import org.slsale.pojo.DataDictionary;
import org.springframework.stereotype.Service;

@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {
	
	@Resource
	private DataDictionaryMapper dataDaictionaryMapper;

		/**
		 * 获取证件类型列表
		 * @return
		 * @throws Exception
		 */
		public List<DataDictionary> getCardtTypeList(DataDictionary dataDictionary) throws Exception{
			
			return dataDaictionaryMapper.getCardtTypeList(dataDictionary);
			
		}
	
	

}
