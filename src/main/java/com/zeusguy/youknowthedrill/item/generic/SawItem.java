package com.zeusguy.youknowthedrill.item.generic;

import com.zeusguy.youknowthedrill.config.ServerConfig;

import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.ToolActions;

public class SawItem extends AnimatedEnergyDiggerItem {

    static {
        itemName = "saw";
    }

    public SawItem(Properties properties, ServerConfig.DiggerItemConfig config) {
        super(properties, config);
        
        DEFAULT_ACTIONS.add(ToolActions.AXE_DIG);
        DEFAULT_ACTIONS.add(ToolActions.AXE_SCRAPE);
        DEFAULT_ACTIONS.add(ToolActions.AXE_STRIP);
        DEFAULT_ACTIONS.add(ToolActions.AXE_WAX_OFF);
        DEFAULT_ACTIONS.add(ToolActions.HOE_DIG);
        DEFAULT_ACTIONS.add(ToolActions.HOE_TILL);

        MINEABLE_TAGS.add(BlockTags.MINEABLE_WITH_AXE);
        MINEABLE_TAGS.add(BlockTags.MINEABLE_WITH_HOE);
    }
    
    
}
