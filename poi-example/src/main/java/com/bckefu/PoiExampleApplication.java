package com.bckefu;

import com.bckefu.entity.ContactsEntity;
import com.bckefu.entity.ContactsGroup;
import com.zhongweixian.excel.handler.inter.IExcelExportServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class PoiExampleApplication {

    @Bean
    IExcelExportServer iExcelExportServer() {
        return new IExcelExportServer() {
            @Override
            public List<Object> selectListForExcelExport(Object obj, int page) {
                List<Object> list = new ArrayList<Object>();
                for (int i = 0; i < 10; i++) {
                    ContactsEntity client = new ContactsEntity();
                    client.setBirthday(new Date());
                    client.setClientName("小明" + i);
                    client.setClientPhone("186129831" + i);
                    client.setCreateBy("hello world");
                    client.setId("1" + i);
                    client.setRemark("测试" + i);
                    client.setCts(new Date());
                    ContactsGroup group = new ContactsGroup();
                    group.setGroupName("测试" + i);
                    client.setGroup(group);
                    list.add(client);
                }
                if (page > 10) {
                    return null;
                }
                return list;
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(PoiExampleApplication.class, args);
    }
}
