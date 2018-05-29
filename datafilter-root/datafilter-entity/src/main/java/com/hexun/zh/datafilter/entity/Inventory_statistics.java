package com.hexun.zh.datafilter.entity;


public class Inventory_statistics {
    private String id;

    private String filename;

    private String sheetname;

    private String serialnumber;

    private String productcoding;

    private String productname;

    private Integer productinventory;

    private String producteffectivetimedec;

    private String effectivetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public String getSheetname() {
        return sheetname;
    }

    public void setSheetname(String sheetname) {
        this.sheetname = sheetname == null ? null : sheetname.trim();
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

    public Integer getProductinventory() {
        return productinventory;
    }

    public void setProductinventory(Integer productinventory) {
        this.productinventory = productinventory;
    }

    public String getProducteffectivetimedec() {
        return producteffectivetimedec;
    }

    public void setProducteffectivetimedec(String producteffectivetimedec) {
        this.producteffectivetimedec = producteffectivetimedec == null ? null : producteffectivetimedec.trim();
    }

    public String getEffectivetime() {
        return effectivetime;
    }

    public void setEffectivetime(String effectivetime) {
        this.effectivetime = effectivetime == null ? null : effectivetime.trim();
    }
}