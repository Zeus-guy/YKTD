package com.zeusguy.youknowthedrill.item.drills;

import com.zeusguy.youknowthedrill.config.ServerConfig;
import com.zeusguy.youknowthedrill.item.generic.DrillItem;

import net.minecraft.world.item.Rarity;

public class DrillDiamond extends DrillItem {

    static {
        itemTier = "diamond";
    }

    public DrillDiamond(Properties properties, ServerConfig.DiggerItemConfig config) {
        super(properties.rarity(Rarity.UNCOMMON), config);
    }
    
}
