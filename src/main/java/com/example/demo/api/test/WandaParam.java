package com.example.demo.api.test;

import lombok.Data;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/2
 */
@Data
public class WandaParam {
    String resId;
    String loginName;
    String authCode;
    String searchParam;
    String page;
    String pageSize;
}
