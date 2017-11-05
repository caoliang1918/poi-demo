package com.excel.cache;

import com.excel.cache.manager.POICacheManager;
import org.apache.poi.util.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:22:50
 */
public class ImageCache {

    private static LoadingCache<String, byte[]> loadingCache;

    static {
        loadingCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS)
                .maximumSize(2000).build(new CacheLoader<String, byte[]>() {
                    @Override
                    public byte[] load(String imagePath) throws Exception {
                        InputStream is = POICacheManager.getFile(imagePath);
                        BufferedImage bufferImg = ImageIO.read(is);
                        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                        try {
                            ImageIO.write(bufferImg,
                                    imagePath.substring(imagePath.indexOf(".") + 1, imagePath.length()),
                                    byteArrayOut);
                            return byteArrayOut.toByteArray();
                        } finally {
                            IOUtils.closeQuietly(is);
                            IOUtils.closeQuietly(byteArrayOut);
                        }
                    }
                });
    }

    public static byte[] getImage(String imagePath) {
        try {
            return loadingCache.get(imagePath);
        } catch (Exception e) {
            return null;
        }

    }

    public static void setLoadingCache(LoadingCache<String, byte[]> loadingCache) {
        ImageCache.loadingCache = loadingCache;
    }
}
