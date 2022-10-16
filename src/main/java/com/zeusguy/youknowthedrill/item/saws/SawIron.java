package com.zeusguy.youknowthedrill.item.saws;

import com.zeusguy.youknowthedrill.config.ServerConfig;
import com.zeusguy.youknowthedrill.item.generic.SawItem;

public class SawIron extends SawItem {

    static {
        itemTier = "iron";
    }

    public SawIron(Properties properties, ServerConfig.DiggerItemConfig config) {
        super(properties, config);
    }
    
}
