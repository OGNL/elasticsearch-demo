package com.demo.util;

import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;

import java.util.Map;


/**
 * Created by zhangtao on 2018/4/1.
 */
public class ESUtil {

    private static Logger log = Logger.getLogger(ESUtil.class);

    public static void addMapToES(String index, String type, Map map){
        log.info("-------- 向ES添加map数据，索引："+index+" 类型："+type+" --------");
        TransportClient client = ESPool.getConnect();
        IndexResponse response = client.prepareIndex(index,type).setSource(map).get();
        log.info("-------- 向ES添加map数据，结果："+response.getResult().getLowercase()+" --------");
        ESPool.closeConnect(client);
    }
}
