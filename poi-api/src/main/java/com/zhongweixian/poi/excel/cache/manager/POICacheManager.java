package com.zhongweixian.poi.excel.cache.manager;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:22:58
 */
public final class POICacheManager {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(POICacheManager.class);

    private static LoadingCache<String, byte[]> loadingCache;

    private static IFileLoader fileLoder;

    private static ThreadLocal<IFileLoader> LOCAL_FILELOADER = new ThreadLocal<IFileLoader>();

    static {
        loadingCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).maximumSize(50)
                .build(new CacheLoader<String, byte[]>() {
                    @Override
                    public byte[] load(String url) throws Exception {
                        if (LOCAL_FILELOADER.get() != null) {
                            return LOCAL_FILELOADER.get().getFile(url);
                        }
                        return fileLoder.getFile(url);
                    }
                });
        //设置默认实现
        fileLoder = new FileLoaderImpl();
    }

    public static InputStream getFile(String id) {
        try {
            //复杂数据,防止操作原数据
            byte[] result = Arrays.copyOf(loadingCache.get(id), loadingCache.get(id).length);
            return new ByteArrayInputStream(result);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public static void setFileLoder(IFileLoader fileLoder) {
        POICacheManager.fileLoder = fileLoder;
    }

    /**
     * 一次线程有效
     *
     * @param fileLoder
     */
    public static void setFileLoderOnce(IFileLoader fileLoder) {
        if (fileLoder != null) {
            LOCAL_FILELOADER.set(fileLoder);
        }
    }

    public static void setLoadingCache(LoadingCache<String, byte[]> loadingCache) {
        POICacheManager.loadingCache = loadingCache;
    }

}
