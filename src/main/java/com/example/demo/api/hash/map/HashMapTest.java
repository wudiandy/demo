package com.example.demo.api.hash.map;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/6/21
 */
public class HashMapTest {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("key", "value");
        System.out.println(map);
    }
}
