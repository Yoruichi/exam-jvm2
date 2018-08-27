package com.foo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class ItemGroupServiceAd implements ItemGroupService {

    private final SkuService skuService =
            ServiceBeanFactory.getInstance().getServiceBean(SkuService.class);
    private final PriceService priceService =
            ServiceBeanFactory.getInstance().getServiceBean(PriceService.class);
    private final InventoryService inventoryService =
            ServiceBeanFactory.getInstance().getServiceBean(InventoryService.class);

    public Map<String, SkuInfoDTO> querySkuEntiy(List<String> skuIds) {
        List<SkuInfoDTO> skuInfoList = skuService.findByIds(skuIds);
        if (Objects.isNull(skuInfoList) || skuInfoList.size()==0) return null;
        return skuInfoList.stream().collect(Collectors.toMap(SkuInfoDTO::getId, sk -> sk));
    }

}
