package com.zhongweixian;

import com.zhongweixian.entity.ContactsEntity;
import com.zhongweixian.entity.ContactsGroup;
import com.zhongweixian.excel.export.base.ExcelExportServer;
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
	IExcelExportServer iExcelExportServer(){
		return new IExcelExportServer() {
			@Override
			public List<Object> selectListForExcelExport(Object obj, int page) {
				List<Object> list = new ArrayList<Object>();
				for (int i = 0; i < 10; i++) {
					ContactsEntity client = new ContactsEntity();
					client.setBirthday(new Date());
					client.setClientName("小明" + i);
					client.setClientPhone("18797" + i);
					client.setCreateBy("JueYue");
					client.setId("1" + i);
					client.setRemark("测试" + i);
					client.setCts(new Date());
					ContactsGroup group = new ContactsGroup();
					group.setGroupName("测试" + i);
					client.setGroup(group);
					list.add(client);
				}
				if(page>10){
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
