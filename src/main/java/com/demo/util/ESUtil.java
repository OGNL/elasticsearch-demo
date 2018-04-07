package com.demo.util;

import org.apache.log4j.Logger;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by zhangtao on 2018/4/1.
 */
public class ESUtil {

    private static Logger log = Logger.getLogger(ESUtil.class);

    public static void addMapToES(String index, String type, Map map){
        TransportClient client = ESPool.getConnect();
        log.info("-------- 向ES添加map数据，索引："+index+" 类型："+type+" --------");
        IndexResponse response = client.prepareIndex(index,type).setSource(map).get();
        log.info("-------- 向ES添加map数据，结果："+response.getResult().getLowercase()+" --------");
        ESPool.closeConnect(client);
    }

    public static void deleteById(String index, String type, String id){
        TransportClient client = ESPool.getConnect();
        log.info("-------- 删除ES中的数据，索引："+index+" 类型："+type+" id："+id+" --------");
        DeleteResponse response = client.prepareDelete(index,type,id).get();
        log.info("-------- 删除ES中的数据，结果："+response.getResult().getLowercase()+" --------");
        ESPool.closeConnect(client);
    }

    public static void updateById(String index, String type, String id, Map map){
        TransportClient client = ESPool.getConnect();
        log.info("-------- 修改ES中的数据，索引："+index+" 类型："+type+" id："+id+" --------");
        UpdateResponse response = client.prepareUpdate(index,type,id).setDoc(map).get();
        log.info("-------- 修改ES中的数据，结果："+response.getResult().getLowercase()+" --------");
        ESPool.closeConnect(client);
    }

    public static Map selectById(String index, String type, String id){
        TransportClient client = ESPool.getConnect();
        log.info("-------- 查询ES中的数据，索引："+index+" 类型："+type+" id："+id+" --------");
        GetResponse response = client.prepareGet(index, type, id).get();
        ESPool.closeConnect(client);
        return response.getSourceAsMap();
    }

    public static List<Map<String,Object>> termQueryByKeyword(String index, String type, String field, String...keyword){
        TransportClient client = ESPool.getConnect();
        log.info("-------- term查询ES中的数据，索引："+index+" 类型："+type+" 字段："+field+" --------");
        QueryBuilder queryBuilder = QueryBuilders.termsQuery(field,keyword);
        SearchHits searchHits = client.prepareSearch(index).setTypes(type).setQuery(queryBuilder).get().getHits();
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>() ;
        for(SearchHit hit : searchHits){
            mapList.add(hit.getSourceAsMap());
        }
        ESPool.closeConnect(client);
        return mapList;
    }

    public static List<Map<String,Object>> matchQueryByKeyword(String index, String type, String field, String keyword, boolean isPhrase){
        TransportClient client = ESPool.getConnect();
        log.info("-------- match查询ES中的数据，索引："+index+" 类型："+type+" 字段："+field+" keyword："+keyword+" 是否匹配所有词："+isPhrase+"--------");
        QueryBuilder queryBuilder = QueryBuilders.matchQuery(field,keyword);
        if(isPhrase){
            queryBuilder = QueryBuilders.matchPhraseQuery(field,keyword);
        }
        SearchHits searchHits = client.prepareSearch(index).setTypes(type).setQuery(queryBuilder).get().getHits();
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>() ;
        for(SearchHit hit : searchHits){
            mapList.add(hit.getSourceAsMap());
        }
        ESPool.closeConnect(client);
        return mapList;
    }


}
