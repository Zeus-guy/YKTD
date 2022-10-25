package com.zeusguy.youknowthedrill.item.drills;

import com.zeusguy.youknowthedrill.config.ServerConfig;
import com.zeusguy.youknowthedrill.item.generic.DrillItem;

import net.minecraft.world.item.Rarity;

public class DrillNetherite extends DrillItem {

    static {
        itemTier = "netherite";
    }

    public DrillNetherite(Properties properties, ServerConfig.DiggerItemConfig config) {
        super(properties.rarity(Rarity.RARE).fireResistant(), config);
    }
    
}
