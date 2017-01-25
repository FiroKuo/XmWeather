package com.gsx.xmweather.db;

import org.litepal.crud.DataSupport;

/**
 * @author Administrator
 * @version $Rev
 * @time 2017/1/24 15:26
 * @des ${TODO}
 * @updateAuthor $Author
 * @updateDate $Date
 * @updateDes ${TODO}
 */

public class Province extends DataSupport {

    private int id;
    private String provinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
