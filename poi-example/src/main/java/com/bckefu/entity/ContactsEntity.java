package com.bckefu.entity;

import com.zhongweixian.excel.annotation.Excel;
import com.zhongweixian.excel.annotation.ExcelEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : caoliang
 * @date : 2017/11/6:下午3:55
 */
public class ContactsEntity implements Serializable {

    /**
     * id
     */
    private java.lang.String id;
    // 电话号码(主键)
    @Excel(name = "电话号码", width = 15)
    private String clientPhone = null;
    // 客户姓名
    @Excel(name = "姓名")
    private String clientName = null;
    // 所属分组
    @ExcelEntity
    private ContactsGroup group = null;
    // 备注
    @Excel(name = "备注")
    private String remark = null;
    // 生日
    @Excel(name = "出生日期", format = "yyyy-MM-dd", width = 20)
    private Date birthday = null;

    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss", width = 25)
    private Date cts;
    // 创建人
    private String createBy = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ContactsGroup getGroup() {
        return group;
    }

    public void setGroup(ContactsGroup group) {
        this.group = group;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getCts() {
        return cts;
    }

    public void setCts(Date cts) {
        this.cts = cts;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
