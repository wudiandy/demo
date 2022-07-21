package com.example.demo.es;

import com.example.demo.DemoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import javax.annotation.Resource;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/5/22
 */
@SpringBootTest(classes = DemoApplication.class)
public class ElasticsearchTest {
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void createIndexUser() {
        elasticsearchRestTemplate.indexOps(IndexCoordinates.of("user")).create();
    }
}
