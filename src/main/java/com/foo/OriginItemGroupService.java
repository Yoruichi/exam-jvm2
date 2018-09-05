package com.foo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.foo.ItemType.ORIGIN;

public class OriginItemGroupService extends ItemGroupServiceByType {
    @Override List<ItemInfo> itemGroupByType(List<ItemInfo> itemInfoList) {
        List<ItemInfo> res = new ArrayList<>();
        Map<String, List<ItemInfo>> originItems =
                itemInfoList.stream().filter(s -> ORIGIN.name().equals(s.getSkuType()))
                        .collect(Collectors.groupingBy(ItemInfo::getArtNo));
        originItems.entrySet().stream().forEach(e -> {
            ItemInfo item = new ItemInfo();
            item.setName(e.getValue().get(0).getName());
            item.setArtNo(e.getKey());
            item.setSkuType(ORIGIN.name());
            Optional<BigDecimal> inventory =
                    e.getValue().stream().map(ItemInfo::getInventory).reduce((a, b) -> a.add(b));
            if (inventory.isPresent()) item.setInventory(inventory.get());
            Optional<BigDecimal> minPrice =
                    e.getValue().stream().map(ItemInfo::getPrice).min(Comparator.naturalOrder());
            if (minPrice.isPresent()) item.setMinPrice(minPrice.get());
            Optional<BigDecimal> maxPrice =
                    e.getValue().stream().map(ItemInfo::getPrice).max(BigDecimal::compareTo);
            if (minPrice.isPresent()) item.setMaxPrice(maxPrice.get());
            res.add(item);
        });
        return res;
    }


}
