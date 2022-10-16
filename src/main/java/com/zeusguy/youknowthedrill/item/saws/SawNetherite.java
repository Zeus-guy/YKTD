package com.zeusguy.youknowthedrill.item.saws;

import com.zeusguy.youknowthedrill.config.ServerConfig;
import com.zeusguy.youknowthedrill.item.generic.SawItem;

import net.minecraft.world.item.Rarity;

public class SawNetherite extends SawItem {

    static {
        itemTier = "netherite";
    }

    public SawNetherite(Properties properties, ServerConfig.DiggerItemConfig config) {
        super(properties.rarity(Rarity.RARE), config);
        canOverclock = true;
        AVAILABLE_MODES.add(DrillMode.SILK);
        AVAILABLE_MODES.add(DrillMode.FORTUNE);
    }
    
}
