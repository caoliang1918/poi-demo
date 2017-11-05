package com.excel.export.template;

import com.excel.annotation.ExcelTarget;
import com.excel.cache.ExcelCache;
import com.excel.entity.ImageEntity;
import com.excel.entity.TemplateExportParams;
import com.excel.entity.enmus.ExcelType;
import com.excel.entity.params.ExcelExportEntity;
import com.excel.entity.params.ExcelForEachParams;
import com.excel.exception.ExcelExportEnum;
import com.excel.exception.ExcelExportException;
import com.excel.export.base.BaseExportServer;
import com.excel.export.style.IExcelExportStyler;
import com.excel.html.helper.MergedRegionHelper;
import com.excel.util.PoiElUtil;
import com.excel.util.PoiExcelGraphDataUtil;
import com.excel.util.PoiPublicUtil;
import com.excel.util.PoiSheetUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

import static com.excel.util.PoiElUtil.eval;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:23:43
 */
public final class ExcelExportOfTemplateUtil extends BaseExportServer {

    private static final Logger  LOGGER            = LoggerFactory
            .getLogger(ExcelExportOfTemplateUtil.class);

    /**
     * 缓存TEMP 的for each创建的cell ,跳过这个cell的模板语法查找,提高效率
     */
    private Set<String>          tempCreateCellSet = new HashSet<String>();
    /**
     * 模板参数,全局都用到
     */
    private TemplateExportParams teplateParams;
    /**
     * 单元格合并信息
     */
    private MergedRegionHelper   mergedRegionHelper;

    private TemplateSumHanlder   templateSumHanlder;

    /**
     * 往Sheet 填充正常数据,根据表头信息 使用导入的部分逻辑,坐对象映射
     *
     * @param sheet
     * @param pojoClass
     * @param dataSet
     * @param workbook
     */
    private void addDataToSheet(Class<?> pojoClass, Collection<?> dataSet, Sheet sheet,
                                Workbook workbook) throws Exception {

        // 获取表头数据
        Map<String, Integer> titlemap = getTitleMap(sheet);
        Drawing patriarch = PoiExcelGraphDataUtil.getDrawingPatriarch(sheet);
        // 得到所有字段
        Field[] fileds = PoiPublicUtil.getClassFields(pojoClass);
        ExcelTarget etarget = pojoClass.getAnnotation(ExcelTarget.class);
        String targetId = null;
        if (etarget != null) {
            targetId = etarget.value();
        }
        // 获取实体对象的导出数据
        List<ExcelExportEntity> excelParams = new ArrayList<ExcelExportEntity>();
        getAllExcelField(null, targetId, fileds, excelParams, pojoClass, null, null);
        // 根据表头进行筛选排序
        sortAndFilterExportField(excelParams, titlemap);
        short rowHeight = getRowHeight(excelParams);
        int index = teplateParams.getHeadingRows() + teplateParams.getHeadingStartRow(),
                titleHeight = index;
        //下移数据,模拟插入
        sheet.shiftRows(teplateParams.getHeadingRows() + teplateParams.getHeadingStartRow(),
                sheet.getLastRowNum(), getShiftRows(dataSet, excelParams), true, true);
        if (excelParams.size() == 0) {
            return;
        }
        Iterator<?> its = dataSet.iterator();
        while (its.hasNext()) {
            Object t = its.next();
            index += createCells(patriarch, index, t, excelParams, sheet, workbook, rowHeight);
        }
        // 合并同类项
        mergeCells(sheet, excelParams, titleHeight);
    }

    /**
     * 下移数据
     * @param dataSet
     * @param excelParams
     * @return
     */
    private int getShiftRows(Collection<?> dataSet,
                             List<ExcelExportEntity> excelParams) throws Exception {
        int size = 0;
        Iterator<?> its = dataSet.iterator();
        while (its.hasNext()) {
            Object t = its.next();
            size += getOneObjectSize(t, excelParams);
        }
        return size;
    }

    /**
     * 获取单个对象的高度,主要是处理一堆多的情况
     *
     * @param t
     * @param excelParams
     * @throws Exception
     */
    public int getOneObjectSize(Object t, List<ExcelExportEntity> excelParams) throws Exception {
        ExcelExportEntity entity;
        int maxHeight = 1;
        for (int k = 0, paramSize = excelParams.size(); k < paramSize; k++) {
            entity = excelParams.get(k);
            if (entity.getList() != null) {
                Collection<?> list = (Collection<?>) entity.getMethod().invoke(t, new Object[] {});
                if (list != null && list.size() > maxHeight) {
                    maxHeight = list.size();
                }
            }
        }
        return maxHeight;

    }

    public Workbook createExcleByTemplate(TemplateExportParams params, Class<?> pojoClass,
                                          Collection<?> dataSet, Map<String, Object> map) {
        // step 1. 判断模板的地址
        if (params == null || map == null || StringUtils.isEmpty(params.getTemplateUrl())) {
            throw new ExcelExportException(ExcelExportEnum.PARAMETER_ERROR);
        }
        Workbook wb = null;
        // step 2. 判断模板的Excel类型,解析模板
        try {
            this.teplateParams = params;
            wb = getCloneWorkBook();
            if (wb instanceof XSSFWorkbook) {
                super.type = ExcelType.XSSF;
            }
            // 创建表格样式
            setExcelExportStyler((IExcelExportStyler) teplateParams.getStyle()
                    .getConstructor(Workbook.class).newInstance(wb));
            // step 3. 解析模板
            for (int i = 0, le = params.isScanAllsheet() ? wb.getNumberOfSheets()
                    : params.getSheetNum().length; i < le; i++) {
                if (params.getSheetName() != null && params.getSheetName().length > i
                        && StringUtils.isNotEmpty(params.getSheetName()[i])) {
                    wb.setSheetName(i, params.getSheetName()[i]);
                }
                tempCreateCellSet.clear();
                parseTemplate(wb.getSheetAt(i), map, params.isColForEach());
            }
            if (dataSet != null) {
                // step 4. 正常的数据填充
                dataHanlder = params.getDataHanlder();
                if (dataHanlder != null) {
                    needHanlderList = Arrays.asList(dataHanlder.getNeedHandlerFields());
                }
                addDataToSheet(pojoClass, dataSet, wb.getSheetAt(params.getDataSheetNum()), wb);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        return wb;
    }

    /**
     * 克隆excel防止操作原对象,workbook无法克隆,只能对excel进行克隆
     *
     * @throws Exception
     */
    private Workbook getCloneWorkBook() throws Exception {
        return ExcelCache.getWorkbook(teplateParams.getTemplateUrl(), teplateParams.getSheetNum(),
                teplateParams.isScanAllsheet());

    }

    /**
     * 获取表头数据,设置表头的序号
     *
     * @param sheet
     * @return
     */
    private Map<String, Integer> getTitleMap(Sheet sheet) {
        Row row = null;
        Iterator<Cell> cellTitle;
        Map<String, Integer> titlemap = new HashMap<String, Integer>();
        for (int j = 0; j < teplateParams.getHeadingRows(); j++) {
            row = sheet.getRow(j + teplateParams.getHeadingStartRow());
            cellTitle = row.cellIterator();
            int i = row.getFirstCellNum();
            while (cellTitle.hasNext()) {
                Cell cell = cellTitle.next();
                String value = cell.getStringCellValue();
                if (!StringUtils.isEmpty(value)) {
                    titlemap.put(value, i);
                }
                i = i + 1;
            }
        }
        return titlemap;

    }

    private void parseTemplate(Sheet sheet, Map<String, Object> map,
                               boolean colForeach) throws Exception {
        deleteCell(sheet, map);
        mergedRegionHelper = new MergedRegionHelper(sheet);
        templateSumHanlder = new TemplateSumHanlder(sheet);
        if (colForeach) {
            colForeach(sheet, map);
        }
        Row row = null;
        int index = 0;
        while (index <= sheet.getLastRowNum()) {
            row = sheet.getRow(index++);
            if (row == null) {
                continue;
            }
            for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                if (row.getCell(i) != null && !tempCreateCellSet
                        .contains(row.getRowNum() + "_" + row.getCell(i).getColumnIndex())) {
                    setValueForCellByMap(row.getCell(i), map);
                }
            }
        }

        //修改需要处理的统计值
        hanlderSumCell(sheet);
    }

    private void hanlderSumCell(Sheet sheet) {
        for (TemplateSumHanlder.TemplateSumEntity sumEntity : templateSumHanlder.getDataList()) {
            Cell cell = sheet.getRow(sumEntity.getRow()).getCell(sumEntity.getCol());
            cell.setCellValue(cell.getStringCellValue()
                    .replace("sum:(" + sumEntity.getSumKey() + ")", sumEntity.getValue() + ""));
        }
    }

    /**
     * 先进行列的循环,因为涉及很多数据
     * @param sheet
     * @param map
     */
    private void colForeach(Sheet sheet, Map<String, Object> map) throws Exception {
        Row row = null;
        Cell cell = null;
        int index = 0;
        while (index <= sheet.getLastRowNum()) {
            row = sheet.getRow(index++);
            if (row == null) {
                continue;
            }
            for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                cell = row.getCell(i);
                if (row.getCell(i) != null && (cell.getCellType() == Cell.CELL_TYPE_STRING
                        || cell.getCellType() == Cell.CELL_TYPE_NUMERIC)) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String text = cell.getStringCellValue();
                    if (text.contains(PoiElUtil.FOREACH_COL) || text.contains(PoiElUtil.FOREACH_COL_VALUE)) {
                        foreachCol(cell, map, text);
                    }
                }
            }
        }
    }

    /**
     * 循环列表
     * @param cell
     * @param map
     * @param name
     * @throws Exception
     */
    private void foreachCol(Cell cell, Map<String, Object> map, String name) throws Exception {
        boolean isCreate = name.contains(PoiElUtil.FOREACH_COL_VALUE);
        name = name.replace(PoiElUtil.FOREACH_COL_VALUE, PoiElUtil.EMPTY).replace(PoiElUtil.FOREACH_COL, PoiElUtil.EMPTY).replace(PoiElUtil.START_STR,
                PoiElUtil.EMPTY);
        String[] keys = name.replaceAll("\\s{1,}", " ").trim().split(" ");
        Collection<?> datas = (Collection<?>) PoiPublicUtil.getParamsValue(keys[0], map);
        Object[] columnsInfo = getAllDataColumns(cell, name.replace(keys[0], PoiElUtil.EMPTY),
                mergedRegionHelper);
        if (datas == null) {
            return;
        }
        Iterator<?> its = datas.iterator();
        int rowspan = (Integer) columnsInfo[0], colspan = (Integer) columnsInfo[1];
        @SuppressWarnings("unchecked")
        List<ExcelForEachParams> columns = (List<ExcelForEachParams>) columnsInfo[2];
        while (its.hasNext()) {
            Object t = its.next();
            setForEeachRowCellValue(true, cell.getRow(), cell.getColumnIndex(), t, columns, map,
                    rowspan, colspan, mergedRegionHelper);
            if(cell.getRow().getCell(cell.getColumnIndex() + colspan) == null){
                cell.getRow().createCell(cell.getColumnIndex() + colspan);
            }
            cell = cell.getRow().getCell(cell.getColumnIndex() + colspan);
        }
        if (isCreate) {
            cell = cell.getRow().getCell(cell.getColumnIndex() - 1);
            cell.setCellValue(cell.getStringCellValue() + PoiElUtil.END_STR);
        }
    }

    /**
     * 先判断删除,省得影响效率
     * @param sheet
     * @param map
     * @throws Exception
     */
    private void deleteCell(Sheet sheet, Map<String, Object> map) throws Exception {
        Row row = null;
        Cell cell = null;
        int index = 0;
        while (index <= sheet.getLastRowNum()) {
            row = sheet.getRow(index++);
            if (row == null) {
                continue;
            }
            for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                cell = row.getCell(i);
                if (row.getCell(i) != null && (cell.getCellType() == Cell.CELL_TYPE_STRING
                        || cell.getCellType() == Cell.CELL_TYPE_NUMERIC)) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String text = cell.getStringCellValue();
                    if (text.contains(PoiElUtil.IF_DELETE)) {
                        if (Boolean.valueOf(
                                eval(text.substring(text.indexOf(PoiElUtil.START_STR) + 2, text.indexOf(PoiElUtil.END_STR))
                                        .trim(), map).toString())) {
                            PoiSheetUtility.deleteColumn(sheet, i);
                        }
                        cell.setCellValue("");
                    }
                }
            }
        }
    }

    /**
     * 给每个Cell通过解析方式set值
     *
     * @param cell
     * @param map
     */
    private void setValueForCellByMap(Cell cell, Map<String, Object> map) throws Exception {
        int cellType = cell.getCellType();
        if (cellType != Cell.CELL_TYPE_STRING && cellType != Cell.CELL_TYPE_NUMERIC) {
            return;
        }
        String oldString;
        cell.setCellType(Cell.CELL_TYPE_STRING);
        oldString = cell.getStringCellValue();
        if (oldString != null && oldString.indexOf(PoiElUtil.START_STR) != -1
                && !oldString.contains(PoiElUtil.FOREACH)) {
            // step 2. 判断是否含有解析函数
            String params = null;
            boolean isNumber = false;
            if (isNumber(oldString)) {
                isNumber = true;
                oldString = oldString.replaceFirst(PoiElUtil.NUMBER_SYMBOL, "");
            }
            Object obj = PoiPublicUtil.getRealValue(oldString, map);
            //如何是数值 类型,就按照数值类型进行设置
            if (obj instanceof ImageEntity) {// 如果是图片就设置为图片
                ImageEntity img = (ImageEntity)obj;
                cell.setCellValue("");
                createImageCell(cell,img.getHeight(),img.getUrl(),img.getData());
            }else if (isNumber && StringUtils.isNotBlank(obj.toString())) {
                cell.setCellValue(Double.parseDouble(obj.toString()));
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            } else {
                cell.setCellValue(obj.toString());
            }
        }
        //判断foreach 这种方法
        if (oldString != null && oldString.contains(PoiElUtil.FOREACH)) {
            addListDataToExcel(cell, map, oldString.trim());
        }

    }

    private boolean isNumber(String text) {
        return text.startsWith(PoiElUtil.NUMBER_SYMBOL) || text.contains("{" + PoiElUtil.NUMBER_SYMBOL)
                || text.contains(" " + PoiElUtil.NUMBER_SYMBOL);
    }

    /**
     * 利用foreach循环输出数据
     * @param cell
     * @param map
     * @param name
     * @throws Exception
     */
    private void addListDataToExcel(Cell cell, Map<String, Object> map,
                                    String name) throws Exception {
        boolean isCreate = !name.contains(PoiElUtil.FOREACH_NOT_CREATE);
        boolean isShift = name.contains(PoiElUtil.FOREACH_AND_SHIFT);
        name = name.replace(PoiElUtil.FOREACH_NOT_CREATE, PoiElUtil.EMPTY).replace(PoiElUtil.FOREACH_AND_SHIFT, PoiElUtil.EMPTY)
                .replace(PoiElUtil.FOREACH, PoiElUtil.EMPTY).replace(PoiElUtil.START_STR, PoiElUtil.EMPTY);
        String[] keys = name.replaceAll("\\s{1,}", " ").trim().split(" ");
        Collection<?> datas = (Collection<?>) PoiPublicUtil.getParamsValue(keys[0], map);
        Object[] columnsInfo = getAllDataColumns(cell, name.replace(keys[0], PoiElUtil.EMPTY),
                mergedRegionHelper);
        if (datas == null) {
            return;
        }
        Iterator<?> its = datas.iterator();
        int rowspan = (Integer) columnsInfo[0], colspan = (Integer) columnsInfo[1];
        @SuppressWarnings("unchecked")
        List<ExcelForEachParams> columns = (List<ExcelForEachParams>) columnsInfo[2];
        Row row = null;
        int rowIndex = cell.getRow().getRowNum() + 1;
        //处理当前行
        if (its.hasNext()) {
            Object t = its.next();
            setForEeachRowCellValue(isCreate, cell.getRow(), cell.getColumnIndex(), t, columns, map,
                    rowspan, colspan, mergedRegionHelper);
            rowIndex += rowspan - 1;
        }
        if (isShift && datas.size() * rowspan > 1 && cell.getRowIndex() + rowspan < cell.getRow().getSheet().getLastRowNum()) {
            createRowNoRow(cell.getRowIndex() + rowspan,cell.getRow().getSheet().getLastRowNum(),(datas.size() - 1) * rowspan);
            cell.getRow().getSheet().shiftRows(cell.getRowIndex() + rowspan,
                    cell.getRow().getSheet().getLastRowNum(), (datas.size() - 1) * rowspan, true, true);
            templateSumHanlder.shiftRows(cell.getRowIndex(),(datas.size() - 1) * rowspan);
        }
        while (its.hasNext()) {
            Object t = its.next();
            row = createRow(rowIndex, cell.getSheet(), isCreate, rowspan);
            setForEeachRowCellValue(isCreate, row, cell.getColumnIndex(), t, columns, map, rowspan,
                    colspan, mergedRegionHelper);
            rowIndex += rowspan;
        }
    }

    private void createRowNoRow(int startRow, int lastRowNum, int i1) {

    }

    /**
     * 创建并返回第一个Row
     * @param sheet
     * @param rowIndex
     * @param isCreate
     * @param rows
     * @return
     */
    private Row createRow(int rowIndex, Sheet sheet, boolean isCreate, int rows) {
        for (int i = 0; i < rows; i++) {
            if (isCreate) {
                sheet.createRow(rowIndex++);
            } else if (sheet.getRow(rowIndex++) == null) {
                sheet.createRow(rowIndex - 1);
            }
        }
        return sheet.getRow(rowIndex - rows);
    }

    private void setForEeachRowCellValue(boolean isCreate, Row row, int columnIndex, Object t,
                                         List<ExcelForEachParams> columns, Map<String, Object> map,
                                         int rowspan, int colspan,
                                         MergedRegionHelper mergedRegionHelper) throws Exception {
        //所有的cell创建一遍
        for (int i = 0; i < rowspan; i++) {
            int size = columns.size();//判断是不是超出设置了
            for (int j = columnIndex, max = columnIndex + colspan; j < max; j++) {
                if (row.getCell(j) == null) {
                    row.createCell(j);
                    CellStyle style = row.getRowNum() % 2 == 0
                            ? getStyles(false,
                            size >= j - columnIndex ? null : columns.get(j - columnIndex))
                            : getStyles(true,
                            size >= j - columnIndex ? null : columns.get(j - columnIndex));
                    //返回的styler不为空时才使用,否则使用Excel设置的,更加推荐Excel设置的样式
                    if (style != null) {
                        row.getCell(j).setCellStyle(style);
                    }
                }

            }
            if (i < rowspan - 1) {
                row = row.getSheet().getRow(row.getRowNum() + 1);
            }
        }
        //填写数据
        ExcelForEachParams params;
        row = row.getSheet().getRow(row.getRowNum() - rowspan + 1);
        for (int k = 0; k < rowspan; k++) {
            int ci = columnIndex;//cell的序号
            row.setHeight(columns.get(0 * colspan).getHeight());
            for (int i = 0; i < colspan && i < columns.size(); i++) {
                boolean isNumber = false;
                params = columns.get(colspan * k + i);
                tempCreateCellSet.add(row.getRowNum() + "_" + (ci));
                if (params == null) {
                    continue;
                }
                if (StringUtils.isEmpty(params.getName())
                        && StringUtils.isEmpty(params.getConstValue())) {
                    row.getCell(ci).setCellStyle(columns.get(i).getCellStyle());
                    ci = ci + columns.get(i).getColspan();
                    continue;
                }
                String val = null;
                //是不是常量
                if (StringUtils.isEmpty(params.getName())) {
                    val = params.getConstValue();
                } else {
                    String tempStr = new String(params.getName());
                    if (isNumber(tempStr)) {
                        isNumber = true;
                        tempStr = tempStr.replaceFirst(PoiElUtil.NUMBER_SYMBOL, "");
                    }
                    map.put(teplateParams.getTempParams(), t);
                    val = eval(tempStr, map).toString();
                }
                if (isNumber && StringUtils.isNotEmpty(val)) {
                    row.getCell(ci).setCellValue(Double.parseDouble(val));
                    row.getCell(ci).setCellType(Cell.CELL_TYPE_NUMERIC);
                } else {
                    try {
                        row.getCell(ci).setCellValue(val);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
                row.getCell(ci).setCellStyle(columns.get(i).getCellStyle());
                //判断这个属性是不是需要统计
                if (params.isNeedSum()) {
                    templateSumHanlder.addValueOfKey(params.getName(), val);
                }
                //如果合并单元格,就把这个单元格的样式和之前的保持一致
                setMergedRegionStyle(row, ci, columns.get(i));
                //合并对应单元格
                if ((params.getRowspan() != 1 || params.getColspan() != 1)
                        && !mergedRegionHelper.isMergedRegion(row.getRowNum() + 1, ci)) {
                    row.getSheet()
                            .addMergedRegion(new CellRangeAddress(row.getRowNum(),
                                    row.getRowNum() + params.getRowspan() - 1, ci,
                                    ci + params.getColspan() - 1));
                }
                ci = ci + params.getColspan();
            }
            row = row.getSheet().getRow(row.getRowNum() + 1);
        }

    }

    private CellStyle getStyles(boolean isSingle, ExcelForEachParams excelForEachParams) {
        return excelExportStyler.getTemplateStyles(isSingle, excelForEachParams);
    }

    /**
     * 设置合并单元格的样式
     * @param row
     * @param ci
     * @param params
     */
    private void setMergedRegionStyle(Row row, int ci, ExcelForEachParams params) {
        //第一行数据
        for (int i = 1; i < params.getColspan(); i++) {
            row.getCell(ci + i).setCellStyle(params.getCellStyle());
        }
        for (int i = 1; i < params.getRowspan(); i++) {
            for (int j = 0; j < params.getColspan(); j++) {
                row.getCell(ci + j).setCellStyle(params.getCellStyle());
            }
        }
    }

    /**
     * 获取迭代的数据的值
     * @param cell
     * @param name
     * @param mergedRegionHelper
     * @return
     */
    private Object[] getAllDataColumns(Cell cell, String name,
                                       MergedRegionHelper mergedRegionHelper) {
        List<ExcelForEachParams> columns = new ArrayList<ExcelForEachParams>();
        cell.setCellValue("");
        columns.add(getExcelTemplateParams(name.replace(PoiElUtil.END_STR, PoiElUtil.EMPTY), cell, mergedRegionHelper));
        int rowspan = 1, colspan = 1;
        if (!name.contains(PoiElUtil.END_STR)) {
            int index = cell.getColumnIndex();
            //保存col 的开始列
            int startIndex = cell.getColumnIndex();
            Row row = cell.getRow();
            while (true) {
                int colSpan = columns.get(columns.size() - 1) != null
                        ? columns.get(columns.size() - 1).getColspan() : 1;
                index += colSpan;
                for (int i = 1; i < colSpan; i++) {
                    //添加合并的单元格,这些单元可能不是空,但是没有值,所以也需要跳过
                    columns.add(null);
                    continue;
                }
                cell = row.getCell(index);
                //可能是合并的单元格
                if (cell == null) {
                    //读取是判断,跳过
                    columns.add(null);
                    continue;
                }
                String cellStringString;
                try {//不允许为空 便利单元格必须有结尾和值
                    cellStringString = cell.getStringCellValue();
                    if (StringUtils.isBlank(cellStringString) && colspan + startIndex <= index) {
                        throw new ExcelExportException("for each 当中存在空字符串,请检查模板");
                    } else if (StringUtils.isBlank(cellStringString)
                            && colspan + startIndex > index) {
                        //读取是判断,跳过,数据为空,但是不是第一次读这一列,所以可以跳过
                        columns.add(new ExcelForEachParams(null, cell.getCellStyle(), (short) 0));
                        continue;
                    }
                } catch (Exception e) {
                    throw new ExcelExportException(ExcelExportEnum.TEMPLATE_ERROR, e);
                }
                //把读取过的cell 置为空
                cell.setCellValue("");
                if (cellStringString.contains(PoiElUtil.END_STR)) {
                    columns.add(getExcelTemplateParams(cellStringString.replace(PoiElUtil.END_STR, PoiElUtil.EMPTY),
                            cell, mergedRegionHelper));
                    break;
                } else if (cellStringString.contains(PoiElUtil.WRAP)) {
                    columns.add(getExcelTemplateParams(cellStringString.replace(PoiElUtil.WRAP, PoiElUtil.EMPTY), cell,
                            mergedRegionHelper));
                    //发现换行符,执行换行操作
                    colspan = index - startIndex + 1;
                    index = startIndex - 1;
                    row = row.getSheet().getRow(row.getRowNum() + 1);
                    rowspan++;
                } else {
                    columns.add(getExcelTemplateParams(cellStringString.replace(PoiElUtil.WRAP, PoiElUtil.EMPTY), cell,
                            mergedRegionHelper));
                }
            }
        }
        colspan = 0;
        for (int i = 0; i < columns.size(); i++) {
            colspan += columns.get(i) != null ? columns.get(i).getColspan() : 0;
        }
        colspan = colspan / rowspan;
        return new Object[] { rowspan, colspan, columns };
    }

    /**
     * 获取模板参数
     * @param name
     * @param cell
     * @param mergedRegionHelper
     * @return
     */
    private ExcelForEachParams getExcelTemplateParams(String name, Cell cell,
                                                      MergedRegionHelper mergedRegionHelper) {
        name = name.trim();
        ExcelForEachParams params = new ExcelForEachParams(name, cell.getCellStyle(),
                cell.getRow().getHeight());
        //判断是不是常量
        if (name.startsWith(PoiElUtil.CONST) && name.endsWith(PoiElUtil.CONST)) {
            params.setName(null);
            params.setConstValue(name.substring(1, name.length() - 1));
        }
        //判断是不是空
        if (PoiElUtil.NULL.equals(name)) {
            params.setName(null);
            params.setConstValue(PoiElUtil.EMPTY);
        }
        //获取合并单元格的数据
        if (mergedRegionHelper.isMergedRegion(cell.getRowIndex() + 1, cell.getColumnIndex())) {
            Integer[] colAndrow = mergedRegionHelper.getRowAndColSpan(cell.getRowIndex() + 1,
                    cell.getColumnIndex());
            params.setRowspan(colAndrow[0]);
            params.setColspan(colAndrow[1]);
        }
        params.setNeedSum(templateSumHanlder.isSumKey(params.getName()));
        return params;
    }

    /**
     * 对导出序列进行排序和塞选
     *
     * @param excelParams
     * @param titlemap
     */
    private void sortAndFilterExportField(List<ExcelExportEntity> excelParams,
                                          Map<String, Integer> titlemap) {
        for (int i = excelParams.size() - 1; i >= 0; i--) {
            if (excelParams.get(i).getList() != null && excelParams.get(i).getList().size() > 0) {
                sortAndFilterExportField(excelParams.get(i).getList(), titlemap);
                if (excelParams.get(i).getList().size() == 0) {
                    excelParams.remove(i);
                } else {
                    excelParams.get(i).setOrderNum(i);
                }
            } else {
                if (titlemap.containsKey(excelParams.get(i).getName())) {
                    excelParams.get(i).setOrderNum(i);
                } else {
                    excelParams.remove(i);
                }
            }
        }
        sortAllParams(excelParams);
    }

    public Workbook createExcleByTemplate(TemplateExportParams params,
                                          Map<Integer, Map<String, Object>> map) {
        // step 1. 判断模板的地址
        if (params == null || map == null || StringUtils.isEmpty(params.getTemplateUrl())) {
            throw new ExcelExportException(ExcelExportEnum.PARAMETER_ERROR);
        }
        Workbook wb = null;
        // step 2. 判断模板的Excel类型,解析模板
        try {
            this.teplateParams = params;
            wb = getCloneWorkBook();
            // step 3. 解析模板
            for (int i = 0, le = params.isScanAllsheet() ? wb.getNumberOfSheets()
                    : params.getSheetNum().length; i < le; i++) {
                if (params.getSheetName() != null && params.getSheetName().length > i
                        && StringUtils.isNotEmpty(params.getSheetName()[i])) {
                    wb.setSheetName(i, params.getSheetName()[i]);
                }
                tempCreateCellSet.clear();
                parseTemplate(wb.getSheetAt(i), map.get(i), params.isColForEach());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        return wb;
    }

}