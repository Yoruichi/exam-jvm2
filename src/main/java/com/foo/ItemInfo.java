package com.foo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfo {
    private String id;
    private String name;
    private String artNo;
    private String spuId;
    private BigDecimal inventory;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String skuType;
}
