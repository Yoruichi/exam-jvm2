package com.foo;

public class ItemGroupServiceFactory {
    public static final OriginItemGroupService originItemGroupService=new OriginItemGroupService();
    public static final DigitalItemGroupService digitalItemGroupService=new DigitalItemGroupService();

    public static ItemGroupServiceByType getService(ItemType type) {
        switch (type) {
            case ORIGIN: return originItemGroupService;
            case DIGITAL:return digitalItemGroupService;
            default: throw new RuntimeException("Not valid item type.");
        }
    }
}
