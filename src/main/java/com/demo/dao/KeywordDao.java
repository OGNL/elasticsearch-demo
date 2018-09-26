package com.demo.dao;




import com.demo.entity.Prefix;
import com.demo.util.Pinyin4jUtil;
import com.demo.util.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 自动提示
 */
public class KeywordDao {
	


	/**
	 * 根据用户输入值查询可能出现结果
	 * @param key 用户输入值
	 * @return
	 */
	public List<String> queryResultByKeys(String key){
		final Jedis jedis = RedisUtil.getJedis();
		Boolean flag = jedis.exists(key);
		if(flag == false){
			jedis.close();
			return null;
		}
		//得分最高的前5条数据
		Set<String> set = jedis.zrevrange(key, 0, 4);
		//Set<String> set = jedis.zrevrange(key, 0, -1);
		List<String> result = new ArrayList<String>();
		Iterator<String> iterator = set.iterator();
		while(iterator.hasNext()){
			result.add(iterator.next());
		}
		jedis.close();
		return result;
	}
	
	/**
	 * 存储用户搜索内容
	 * @param member 用户搜索值
	 */
	public void addToKeys(String member){
		final Jedis jedis = RedisUtil.getJedis();
		Set<String> keys = jedis.keys(member.substring(0, 1)+"*");
		//用户搜索值的所有前缀（最长10位）
		List<Prefix> members = new ArrayList<Prefix>();
		for(int i=0; i<member.length()-1&&i<10; i++){
			Prefix prefix = new Prefix();
			prefix.setContent(member.substring(0, i+1));
			members.add(prefix);
		}
		//若搜索值为中文，则额外添加拼音前缀（最长15位）
		String chinese = member.replaceAll(" ","");
		if(chinese.length()>5){//截取前5个中文字符串
			chinese = chinese.substring(0,5);
		}
		if(chinese.matches("[\\u4e00-\\u9fa5]+")){
			String [] firstSpell = Pinyin4jUtil.converterToFirstSpell(chinese.substring(0,1)).split(",");
			for(int i=0; i<firstSpell.length; i++){
				keys.addAll(jedis.keys(firstSpell[i]+"*"));
			}

			String [] spell = Pinyin4jUtil.converterToSpell(chinese).split(",");
			for(int i=0; i<spell.length; i++){
				for(int j=0; j<spell[i].length()&&j<15; j++){
					Prefix prefix = new Prefix();
					prefix.setContent(spell[i].substring(0, j+1));
					members.add(prefix);
				}
			}
		}
		//将用户输入值添加到相关前缀的结果集中
		if(keys.size() > 0){
			for(int i=0; i<members.size(); i++){
				for(String k : keys){
					if(members.get(i).getContent().equals(k)){
						//集合分数变动
						Double score = jedis.zscore(k, member);
						if(score == null){
							jedis.zadd(k, 0, member);
						}else{
							jedis.zadd(k, score + 1, member);
						}
						members.get(i).setExist(true);
						break;
					}
				}
			}
			
			for(int i=0; i<members.size(); i++){
				if(members.get(i).isExist() == false){
					jedis.zadd(members.get(i).getContent(), 0, member);
				}
			}
			jedis.close();
		}else{
			for(int i=0; i<members.size(); i++){
				jedis.zadd(members.get(i).getContent(), 0, member);
			}
			jedis.close();
		}
	
	}


}
