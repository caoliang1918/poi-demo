package com.zhongweixian.excel.cache.manager;

import com.zhongweixian.excel.util.PoiPublicUtil;
import org.apache.poi.util.IOUtils;

import java.io.*;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:22:59
 */
public class FileLoaderImpl implements IFileLoader {
    @Override
    public byte[] getFile(String key) {
        InputStream fileis = null;
        ByteArrayOutputStream baos = null;
        try {
            //先用绝对路径查询,再查询相对路径
            try {
                fileis = new FileInputStream(key);
            } catch (FileNotFoundException e) {
                //获取项目文件
                fileis = ClassLoader.getSystemResourceAsStream(key);
                if (fileis == null) {
                    //最后再拿想对文件路径
                    String path = PoiPublicUtil.getWebRootPath(key);
                    fileis = new FileInputStream(path);
                }
            }
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fileis.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fileis);
            IOUtils.closeQuietly(baos);
        }
        new FileNotFoundException(fileis + "：  文件路径未找到");
        return null;
    }
}
