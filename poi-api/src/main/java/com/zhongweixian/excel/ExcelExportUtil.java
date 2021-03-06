package com.zhongweixian.excel;

import com.zhongweixian.excel.entity.ExportParams;
import com.zhongweixian.excel.entity.TemplateExportParams;
import com.zhongweixian.excel.entity.enmus.ExcelType;
import com.zhongweixian.excel.entity.params.ExcelExportEntity;
import com.zhongweixian.excel.export.ExcelBatchExportServer;
import com.zhongweixian.excel.export.base.ExcelExportServer;
import com.zhongweixian.excel.export.template.ExcelExportOfTemplateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:22:14
 */
public class ExcelExportUtil {
    private ExcelExportUtil() {
    }

    /**
     * @param entity    表格标题属性
     * @param pojoClass Excel对象Class
     * @param dataSet   Excel对象数据List
     */
    public static Workbook exportBigExcel(ExportParams entity, Class<?> pojoClass,
                                          Collection<?> dataSet) {
        ExcelBatchExportServer batachServer = ExcelBatchExportServer
                .getExcelBatchExportServer(entity, pojoClass);
        return batachServer.appendData(dataSet);
    }

    public static void closeExportBigExcel() {
        ExcelBatchExportServer batachServer = ExcelBatchExportServer.getExcelBatchExportServer(null,
                null);
        batachServer.closeExportBigExcel();
    }

    /**
     * @param entity    表格标题属性
     * @param pojoClass Excel对象Class
     * @param dataSet   Excel对象数据List
     */
    public static Workbook exportExcel(ExportParams entity, Class<?> pojoClass,
                                       Collection<?> dataSet) {
        Workbook workbook = getWorkbook(entity.getType(), dataSet.size());
        new ExcelExportServer().createSheet(workbook, entity, pojoClass, dataSet);
        return workbook;
    }

    private static Workbook getWorkbook(ExcelType type, int size) {
        if (ExcelType.HSSF.equals(type)) {
            return new HSSFWorkbook();
        } else if (size < 100000) {
            return new XSSFWorkbook();
        } else {
            return new SXSSFWorkbook();
        }
    }

    /**
     * 根据Map创建对应的Excel
     *
     * @param entity     表格标题属性
     * @param entityList Map对象列表
     * @param dataSet    Excel对象数据List
     */
    public static Workbook exportExcel(ExportParams entity, List<ExcelExportEntity> entityList,
                                       Collection<? extends Map<?, ?>> dataSet) {
        Workbook workbook = getWorkbook(entity.getType(), dataSet.size());
        ;
        new ExcelExportServer().createSheetForMap(workbook, entity, entityList, dataSet);
        return workbook;
    }

    /**
     * 一个excel 创建多个sheet
     *
     * @param list 多个Map key title 对应表格Title key entity 对应表格对应实体 key data
     *             Collection 数据
     * @return
     */
    public static Workbook exportExcel(List<Map<String, Object>> list, ExcelType type) {
        Workbook workbook = getWorkbook(type, 0);
        for (Map<String, Object> map : list) {
            ExcelExportServer server = new ExcelExportServer();
            server.createSheet(workbook, (ExportParams) map.get("title"),
                    (Class<?>) map.get("entity"), (Collection<?>) map.get("data"));
        }
        return workbook;
    }

    /**
     * 导出文件通过模板解析,不推荐这个了,推荐全部通过模板来执行处理
     *
     * @param params    导出参数类
     * @param pojoClass 对应实体
     * @param dataSet   实体集合
     * @param map       模板集合
     * @return
     */
    @Deprecated
    public static Workbook exportExcel(TemplateExportParams params, Class<?> pojoClass,
                                       Collection<?> dataSet, Map<String, Object> map) {
        return new ExcelExportOfTemplateUtil().createExcleByTemplate(params, pojoClass, dataSet,
                map);
    }

    /**
     * 导出文件通过模板解析只有模板,没有集合
     *
     * @param params 导出参数类
     * @param map    模板集合
     * @return
     */
    public static Workbook exportExcel(TemplateExportParams params, Map<String, Object> map) {
        return new ExcelExportOfTemplateUtil().createExcleByTemplate(params, null, null, map);
    }

    /**
     * 导出文件通过模板解析只有模板,没有集合
     * 每个sheet对应一个map,导出到处,key是sheet的NUM
     *
     * @param params 导出参数类
     * @param map    模板集合
     * @return
     */
    public static Workbook exportExcel(Map<Integer, Map<String, Object>> map,
                                       TemplateExportParams params) {
        return new ExcelExportOfTemplateUtil().createExcleByTemplate(params, map);
    }
}
