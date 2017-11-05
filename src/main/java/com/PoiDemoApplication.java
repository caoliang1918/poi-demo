package com;

import com.excel.entity.params.ExcelExportEntity;
import com.excel.entity.vo.MapExcelConstants;
import com.excel.view.PoiBaseView;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author caoliang1918@aliyun.com
 * @Date 2017/11/5:1:13
 */
@SpringBootApplication
@RestController
public class PoiDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoiDemoApplication.class , args);
    }

    @GetMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        //标题
        List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
        //内容
        List<Map<String,Object>> dataResult = new ArrayList<Map<String,Object>>();
        entityList.add(new ExcelExportEntity("表头1", "table1", 15));
        entityList.add(new ExcelExportEntity("表头2", "table2", 25));
        entityList.add(new ExcelExportEntity("表头3", "table3", 35));
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("table1", "苹果"+i);
            map.put("table2", "香蕉"+i);
            map.put("table3", "鸭梨"+i);
            dataResult.add(map);
        }
        modelMap.put(MapExcelConstants.ENTITY_LIST, entityList);
        modelMap.put(MapExcelConstants.MAP_LIST, dataResult);
        String fileName = "easypoi测试列表";
        modelMap.put(MapExcelConstants.FILE_NAME, fileName);
//        Workbook workbook = ExcelExportUtil.exportExcel(dataResult , ExcelType.XSSF.name());
        PoiBaseView.render(modelMap, request, response, MapExcelConstants.EASYPOI_MAP_EXCEL_VIEW);
    }
}
