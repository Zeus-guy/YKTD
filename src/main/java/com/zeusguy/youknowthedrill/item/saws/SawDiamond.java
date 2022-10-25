package com.zeusguy.youknowthedrill.item.saws;

import com.zeusguy.youknowthedrill.config.ServerConfig;
import com.zeusguy.youknowthedrill.item.generic.SawItem;

import net.minecraft.world.item.Rarity;

public class SawDiamond extends SawItem {

    static {
        itemTier = "diamond";
    }

    public SawDiamond(Properties properties, ServerConfig.DiggerItemConfig config) {
        super(properties.rarity(Rarity.UNCOMMON), config);
    }
    
}
