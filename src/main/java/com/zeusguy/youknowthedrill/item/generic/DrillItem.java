package com.zeusguy.youknowthedrill.item.generic;

import com.zeusguy.youknowthedrill.config.ServerConfig;

import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.ToolActions;

public class DrillItem extends AnimatedEnergyDiggerItem {

    static {
        itemName = "drill";
    }
    
    public DrillItem(Properties properties, ServerConfig.DiggerItemConfig config) {
        super(properties, config);

        DEFAULT_ACTIONS.add(ToolActions.PICKAXE_DIG);
        DEFAULT_ACTIONS.add(ToolActions.SHOVEL_DIG);
        DEFAULT_ACTIONS.add(ToolActions.SHOVEL_FLATTEN);

        MINEABLE_TAGS.add(BlockTags.MINEABLE_WITH_PICKAXE);
        MINEABLE_TAGS.add(BlockTags.MINEABLE_WITH_SHOVEL);
    }

	
    
}
