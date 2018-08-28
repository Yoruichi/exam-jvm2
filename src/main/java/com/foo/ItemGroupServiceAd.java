package com.foo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ItemGroupServiceAd implements ItemGroupService {

    private SkuService skuService;
    private PriceService priceService;
    private InventoryService inventoryService;

    public ItemGroupServiceAd(SkuService skuService, PriceService priceService,
            InventoryService inventoryService) {
        this.skuService = skuService;
        this.priceService = priceService;
        this.inventoryService = inventoryService;
    }

    public Map<String, SkuInfoDTO> querySkuEntity(List<String> skuIds) {
        List<SkuInfoDTO> skuInfoList = new ArrayList<>();
        int f = 0, t = 20;
        while (skuIds.size() >= t) {
            skuInfoList.addAll(skuService.findByIds(skuIds.subList(f, t)));
            f = t;
            t += 20;
        }
        if (skuIds.size() < t) {
            skuInfoList.addAll(skuService.findByIds(skuIds.subList(f, skuIds.size())));
        }
        if (Objects.isNull(skuInfoList) || skuInfoList.size() == 0) return null;
        return skuInfoList.stream().collect(Collectors.toMap(SkuInfoDTO::getId, sk -> sk));
    }



    public Map<String, BigDecimal> querySkuPrice(List<String> skuIds) {
        Map<String, BigDecimal> priceMap = new HashMap<>();
        skuIds.stream().forEach(id -> {
            BigDecimal p = priceService.getBySkuId(id);
            priceMap.put(id, p == null ? new BigDecimal(0) : p);
        });
        return priceMap;
    }

    public Map<String, BigDecimal> querySkuInventory(List<String> skuIds) {
        Map<String, BigDecimal> inventoryMap = new HashMap<>();
        skuIds.stream().forEach(skuId -> {
            List<ChannelInventoryDTO> inventoryList = inventoryService.getBySkuId(skuId);
            if (null != inventoryList) {
                Optional<BigDecimal> op = inventoryList.stream()
                        .map(ChannelInventoryDTO::getInventory).reduce((a, b) -> a.add(b));
                if (op.isPresent()) inventoryMap.put(skuId, op.get());
            }
        });
        return inventoryMap;
    }

}
