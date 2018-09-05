package com.foo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.foo.ItemType.DIGITAL;

public class DigitalItemGroupService extends ItemGroupServiceByType {
    @Override List<ItemInfo> itemGroupByType(List<ItemInfo> itemInfoList) {
        List<ItemInfo> res = new ArrayList<>();
        Map<String, List<ItemInfo>> digitalItems =
                itemInfoList.stream().filter(s -> DIGITAL.name().equals(s.getSkuType()))
                        .collect(Collectors.groupingBy(ItemInfo::getSpuId));
        digitalItems.entrySet().stream().forEach(e -> {
            ItemInfo item = new ItemInfo();
            item.setName(e.getValue().get(0).getName());
            item.setSpuId(e.getKey());
            item.setSkuType(DIGITAL.name());
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
