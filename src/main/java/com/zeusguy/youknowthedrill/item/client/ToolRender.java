package com.zeusguy.youknowthedrill.item.client;

import com.zeusguy.youknowthedrill.item.generic.AnimatedEnergyDiggerItem;

import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ToolRender extends GeoItemRenderer<AnimatedEnergyDiggerItem> {

    public ToolRender(String name, String tier) {
        super(new ToolModel(name, tier));
    }
    
}
