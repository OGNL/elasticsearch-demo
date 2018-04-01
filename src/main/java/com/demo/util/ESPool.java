package com.demo.util;

import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by zhangtao on 2018/3/25.
 */
public class ESPool {

    public static final String CLUSTER_NAME = "es-5.0"; //ES集群名称
    private static final String IP = "127.0.0.1";//集群ip
    private static final int PORT = 9300;  //端口

    private static TransportClient client = null;
    private static Logger log = Logger.getLogger(ESPool.class);

    public synchronized  static TransportClient getConnect(){
        Settings settings = Settings.builder()
                .put("cluster.name", CLUSTER_NAME)
                .put("client.transport.sniff", true) //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                .build();
        try {
            client = new PreBuiltTransportClient(settings).addTransportAddresses(
                    new InetSocketTransportAddress(InetAddress.getByName(IP),PORT));
        } catch (UnknownHostException e) {
           log.error("-------- ES连接失败，请检查配置信息是否正确 --------");
            e.printStackTrace();
        }
        return client;
    }

    public synchronized static void closeConnect(Client client) {
        if(null != client) {
            log.info("-------- 执行关闭ES连接操作 --------");
            client.close();
        }
    }




}
