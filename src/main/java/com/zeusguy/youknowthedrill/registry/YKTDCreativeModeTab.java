package com.zeusguy.youknowthedrill.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class YKTDCreativeModeTab {
    public static final CreativeModeTab YKTD_TAB = new CreativeModeTab("yktdtab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(YKTDItems.IRON_DRILL.get());
        }
    };
}
