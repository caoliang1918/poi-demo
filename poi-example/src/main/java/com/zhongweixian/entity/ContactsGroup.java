package com.zhongweixian.entity;

import com.zhongweixian.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @author : caoliang
 * @date : 2017/11/6:下午3:56
 */
public class ContactsGroup implements Serializable {

    /**
     * 组名
     */
    @Excel(name = "分组")
    private String groupName = null;
    /**
     * 创建人
     */
    private String createBy;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
