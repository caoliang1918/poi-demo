package com.zhongweixian.excel.cache.manager;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:22:58
 */
public interface IFileLoader {
    /**
     * 可以自定义KEY的作用
     *
     * @param key
     * @return
     */
    public byte[] getFile(String key);

}
