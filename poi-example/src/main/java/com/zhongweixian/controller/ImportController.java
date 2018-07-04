package com.zhongweixian.controller;

import com.zhongweixian.entity.ContactsEntity;
import com.zhongweixian.excel.ExcelImportUtil;
import com.zhongweixian.excel.entity.ImportParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author : caoliang
 * @date : 2017/11/14  下午4:09
 */
@RestController
@RequestMapping("import")
public class ImportController {
    Logger logger = LoggerFactory.getLogger(ImportController.class);


    @ApiOperation(value = "导入测试", tags = "1.0.0")
    @PostMapping("")
    public ResponseEntity importExcel(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return new ResponseEntity("FILE ", HttpStatus.BAD_REQUEST);
        }
        ImportParams params = new ImportParams();
        long start = System.currentTimeMillis();
        List<ContactsEntity> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), ContactsEntity.class, params);
        logger.info("导入耗时 : {}  毫秒", System.currentTimeMillis() - start);
        logger.info("导入行数 : {}", list.size());
        return new ResponseEntity(list, HttpStatus.OK);
    }
}
