package org.slsale.common;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisAPI {
	public JedisPool jedisPool;

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	
	/*设置存放redis的key和value值*/
	public boolean Set(String key,String value){
		Jedis jedis=null;
		try{
			jedis=jedisPool.getResource();
			jedis.set(key, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			returnResource(jedisPool, jedis);
		}
		
	}
	/*判断key值是否存在*/
	public boolean exist(String key){
		Jedis jedis=null;
		try{
			jedis=jedisPool.getResource();
			return jedis.exists(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return false;
	}
	/*返还到数据连接池*/
	public static void returnResource(JedisPool pool,Jedis jedis){
		if(jedis !=null){
			pool.returnResource(jedis);
		}
	}
	/*获取key*/
	public String get(String key){
		String value =null;
		Jedis jedis=null;
		try{
			jedis=jedisPool.getResource();
			value=jedis.get(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return value;
	}
	
	
	
	
	
}
