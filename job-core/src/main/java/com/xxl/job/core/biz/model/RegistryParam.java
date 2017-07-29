package com.xxl.job.core.biz.model;

import java.io.Serializable;

/**
 * @project 登记参数Bean，用于绑定执行器的名称和执行器的地址
 * @file RegistryParam.java 创建时间:2017年7月28日下午5:25:46
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class RegistryParam implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * @description 登记组名
     * @value value:registGroup
     */
    private String registGroup;
    
    /**
     * @description 登记key
     * @value value:registryKey
     */
    private String registryKey;
    
    /**
     * @description 登记value
     * @value value:registryValue
     */
    private String registryValue;

    public RegistryParam(){}
    
    public RegistryParam(String registGroup, String registryKey, String registryValue) {
        this.registGroup = registGroup;
        this.registryKey = registryKey;
        this.registryValue = registryValue;
    }

    public String getRegistGroup() {
        return registGroup;
    }

    public void setRegistGroup(String registGroup) {
        this.registGroup = registGroup;
    }

    public String getRegistryKey() {
        return registryKey;
    }

    public void setRegistryKey(String registryKey) {
        this.registryKey = registryKey;
    }

    public String getRegistryValue() {
        return registryValue;
    }

    public void setRegistryValue(String registryValue) {
        this.registryValue = registryValue;
    }

    @Override
    public String toString() {
        return "RegistryParam{" +
                "registGroup='" + registGroup + '\'' +
                ", registryKey='" + registryKey + '\'' +
                ", registryValue='" + registryValue + '\'' +
                '}';
    }
}
