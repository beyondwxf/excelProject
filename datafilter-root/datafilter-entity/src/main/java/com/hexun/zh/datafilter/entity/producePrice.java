package com.hexun.zh.datafilter.entity;

public class producePrice {
    private Integer id;

    private String serialnumber;

    private String productcoding;

    private String productname;

    private String saleprice;

    private String baseprice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber == null ? null : serialnumber.trim();
    }

    public String getProductcoding() {
        return productcoding;
    }

    public void setProductcoding(String productcoding) {
        this.productcoding = productcoding == null ? null : productcoding.trim();
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname == null ? null : productname.trim();
    }

    public String getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(String saleprice) {
        this.saleprice = saleprice == null ? null : saleprice.trim();
    }

    public String getBaseprice() {
        return baseprice;
    }

    public void setBaseprice(String baseprice) {
        this.baseprice = baseprice == null ? null : baseprice.trim();
    }
}