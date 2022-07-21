package com.example.demo.api.es;

import com.example.demo.api.es.pojo.Book;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/5/19
 */
@RestController
@RequestMapping("/es")
public class ElasticSearchController {

    @Resource
    private ElasticsearchOperations elasticsearchOperations;

    @PostMapping("/book")
    public String save(@RequestBody Book book) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(book.getId())
                .withObject(book)
                .build();
        IndexCoordinates indexCoordinates = IndexCoordinates.of("books");
        return elasticsearchOperations.index(indexQuery, indexCoordinates);
    }

    @GetMapping("/book/{id}")
    public Book findById(@PathVariable("id") String id) {
        return elasticsearchOperations.get(id, Book.class);
    }
}
