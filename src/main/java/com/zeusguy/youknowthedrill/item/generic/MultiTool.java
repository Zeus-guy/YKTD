package com.zeusguy.youknowthedrill.item.generic;

import com.zeusguy.youknowthedrill.config.ServerConfig;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ToolActions;

public class MultiTool extends AnimatedEnergyDiggerItem {

    static {
        itemName = "multitool";
        itemTier = "echo";
    }
    
    public MultiTool(Properties properties, ServerConfig.DiggerItemConfig config) {
        super(properties.rarity(Rarity.EPIC).fireResistant(), config);

        DEFAULT_ACTIONS.add(ToolActions.PICKAXE_DIG);
        DEFAULT_ACTIONS.add(ToolActions.SHOVEL_DIG);
        DEFAULT_ACTIONS.add(ToolActions.SHOVEL_FLATTEN);

        DEFAULT_ACTIONS.add(ToolActions.AXE_DIG);
        DEFAULT_ACTIONS.add(ToolActions.AXE_SCRAPE);
        DEFAULT_ACTIONS.add(ToolActions.AXE_STRIP);
        DEFAULT_ACTIONS.add(ToolActions.AXE_WAX_OFF);
        DEFAULT_ACTIONS.add(ToolActions.HOE_DIG);
        DEFAULT_ACTIONS.add(ToolActions.HOE_TILL);

        MINEABLE_TAGS.add(BlockTags.MINEABLE_WITH_PICKAXE);
        MINEABLE_TAGS.add(BlockTags.MINEABLE_WITH_SHOVEL);

        MINEABLE_TAGS.add(BlockTags.MINEABLE_WITH_AXE);
        MINEABLE_TAGS.add(BlockTags.MINEABLE_WITH_HOE);
    }

	
    
}
