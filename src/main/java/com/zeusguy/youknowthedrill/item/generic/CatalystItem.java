package com.zeusguy.youknowthedrill.item.generic;

import java.util.List;

import javax.annotation.Nullable;

import com.zeusguy.youknowthedrill.registry.YKTDCreativeModeTab;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class CatalystItem extends Item {

    private String tooltipText;
    public CatalystItem(Properties properties, String tooltipText) {
        super(properties.rarity(Rarity.UNCOMMON).tab(YKTDCreativeModeTab.YKTD_TAB));
        this.tooltipText = tooltipText;
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable(tooltipText).withStyle(ChatFormatting.GRAY));
    }
    
}
