package com.demo.util;

import com.demo.entity.MappingConfig;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by zhangtao on 2018/4/1.
 */
public class ESUtil {

    private static Logger log = Logger.getLogger(ESUtil.class);

    /**
     * 创建索引
     * @param index 索引名称
     * @return
     */
    public static boolean createIndex(String index){
        TransportClient client = ESPool.getConnect();
        log.info("-------- 开始创建ES索引，索引："+index+" --------");
        boolean isAcknowledged =  client.admin().indices().prepareCreate(index).setSettings(Settings.builder()
                .put("index.number_of_shards", 5).put("index.number_of_replicas", 1))
                .execute().actionGet().isAcknowledged();
        ESPool.closeConnect(client);
        return isAcknowledged;
    }

    /**
     * 创建索引的mapping结构
     * @param index 索引名称
     * @param type 类型
     * @param mappingConfigList mapping结构类集合
     * @return
     */
    public static boolean createMapping(String index, String type, List<MappingConfig> mappingConfigList){
        log.info("-------- 开始生成mapping结构，索引："+index+" 类型："+type+" --------");
        XContentBuilder mapping = null;
        boolean isAcknowledged = false;
        try {
            mapping = XContentFactory.jsonBuilder().startObject()
                    .startObject(type).startObject("properties");
            for(int i=0; i<mappingConfigList.size(); i++){
                MappingConfig mappingConfig = mappingConfigList.get(i);
                mapping = mapping.startObject(mappingConfig.getFieldName())
                        .field("type",mappingConfig.getFieldType());

                if(mappingConfig.getFieldType().equals("text")){
                    mapping = mapping.field("analyzer",mappingConfig.getAnalyzer())
                            .startObject("fields").startObject("keyword")
                            .field("ignore_above",256).field("type","keyword")
                            .endObject().endObject();
                }
                mapping = mapping.endObject();
            }
            mapping = mapping.endObject().endObject().endObject();

            TransportClient client = ESPool.getConnect();
            log.info("-------- 开始向ES创建mapping结构，索引："+index+" 类型："+type+" --------");
            PutMappingRequest request = Requests.putMappingRequest(index).type(type).source(mapping);
            isAcknowledged = client.admin().indices().putMapping(request).actionGet().isAcknowledged();
            return isAcknowledged;
        } catch (IOException e) {
            e.printStackTrace();
            return isAcknowledged;
        }
    }

    /**
     * 添加map类型数据
     * @param index 索引名称
     * @param type 类型
     * @param map 数据
     */
    public static void addMapToES(String index, String type, Map map){
        TransportClient client = ESPool.getConnect();
        log.info("-------- 向ES添加map数据，索引："+index+" 类型："+type+" --------");
        IndexResponse response = client.prepareIndex(index,type).setSource(map).get();
        log.info("-------- 向ES添加map数据，结果："+response.getResult().getLowercase()+" --------");
        ESPool.closeConnect(client);
    }

    /**
     * 根据id删除文档
     * @param index 索引名称
     * @param type 类型
     * @param id 文档id
     */
    public static void deleteById(String index, String type, String id){
        TransportClient client = ESPool.getConnect();
        log.info("-------- 删除ES中的数据，索引："+index+" 类型："+type+" id："+id+" --------");
        DeleteResponse response = client.prepareDelete(index,type,id).get();
        log.info("-------- 删除ES中的数据，结果："+response.getResult().getLowercase()+" --------");
        ESPool.closeConnect(client);
    }

    /**
     * 根据id修改文档
     * @param index 索引名称
     * @param type 类型
     * @param id 文档id
     * @param map 修改后的数据
     */
    public static void updateById(String index, String type, String id, Map map){
        TransportClient client = ESPool.getConnect();
        log.info("-------- 修改ES中的数据，索引："+index+" 类型："+type+" id："+id+" --------");
        UpdateResponse response = client.prepareUpdate(index,type,id).setDoc(map).get();
        log.info("-------- 修改ES中的数据，结果："+response.getResult().getLowercase()+" --------");
        ESPool.closeConnect(client);
    }

    /**
     *根据id查询文档
     * @param index 索引名承
     * @param type 类型
     * @param id 文档id
     * @return
     */
    public static Map selectById(String index, String type, String id){
        TransportClient client = ESPool.getConnect();
        log.info("-------- 查询ES中的数据，索引："+index+" 类型："+type+" id："+id+" --------");
        GetResponse response = client.prepareGet(index, type, id).get();
        ESPool.closeConnect(client);
        return response.getSourceAsMap();
    }

    /**
     * 根据关键词查询文档（不对关键词分词）
     * @param index 索引名称
     * @param type 类型
     * @param field 字段 ES默认会对字段内容进行分词，可在字段后加".keyword"（5.x版本)或".raw"(2.x版本)防止分词
     *                    例如："content" -> "content.keyword"
     * @param keyword 关键词
     * @return
     */
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

    /**
     * 根据关键词查询文档（会对关键词分词）
     * @param index 索引名称
     * @param type 类型
     * @param field 字段
     * @param keyword 关键词
     * @param isPhrase 是否匹配所有词
     * @return
     */
    public static List<Map<String,Object>> matchQueryByKeyword(String index, String type, String field, String keyword, boolean isPhrase){
        TransportClient client = ESPool.getConnect();
        log.info("-------- match查询ES中的数据，索引："+index+" 类型："+type+" 字段："+field+" 输入内容："+keyword+" 是否匹配所有词："+isPhrase+"--------");
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
