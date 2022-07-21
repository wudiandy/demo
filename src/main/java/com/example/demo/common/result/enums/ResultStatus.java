package com.example.demo.common.result.enums;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/6/26
 */
public enum ResultStatus {

    /**
     * 成功
     */
    SUCCESS("success"),

    /**
     * 失败
     */
    FAILED("failed");

    private final String value;

    ResultStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
