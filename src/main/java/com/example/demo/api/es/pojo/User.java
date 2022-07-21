package com.example.demo.api.es.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/5/19
 */
@Data
@Document(indexName = "user")
public class User {
    @Id
    Long id;

    @Field(store = true)
    String name;

    @Field(store = true)
    Integer age;
}
