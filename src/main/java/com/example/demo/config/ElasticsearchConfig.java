package com.example.demo.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/5/21
 */
//@Configuration
//public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
//    @Override
//    public RestHighLevelClient elasticsearchClient() {
//        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo("172.16.255.137:9300")
//                .build();
//        return RestClients.create(clientConfiguration).rest();
//    }
//}
