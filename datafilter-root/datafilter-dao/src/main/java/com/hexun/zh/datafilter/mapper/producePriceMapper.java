package com.hexun.zh.datafilter.mapper;

import com.hexun.zh.datafilter.entity.producePrice;

public interface producePriceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(producePrice record);

    int insertSelective(producePrice record);

    producePrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(producePrice record);

    int updateByPrimaryKey(producePrice record);
}