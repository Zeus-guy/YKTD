package com.zeusguy.youknowthedrill.item.drills;

import com.zeusguy.youknowthedrill.config.ServerConfig;
import com.zeusguy.youknowthedrill.item.generic.DrillItem;

public class DrillIron extends DrillItem {

    static {
        itemTier = "iron";
    }

    public DrillIron(Properties properties, ServerConfig.DiggerItemConfig config) {
        super(properties, config);
    }
    
}
