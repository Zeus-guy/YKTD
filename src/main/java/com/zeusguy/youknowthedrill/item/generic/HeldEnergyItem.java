package com.zeusguy.youknowthedrill.item.generic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.zeusguy.zglib.item.IEnergyItem;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class HeldEnergyItem extends CrossbowItem implements IEnergyItem {

    protected int max_energy, energy_per_use, energy_extract, energy_receive;

    public HeldEnergyItem(Properties properties) {

        super(properties.setNoRepair().stacksTo(1));
        this.max_energy = 100;

    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {

        tooltip.add(Component.literal(getEnergy(stack) + "/" + getMaxEnergy(stack) + " FE"));

        List<Component> tooltips = addTooltips();
        if (!tooltips.isEmpty()) {
            tooltip.addAll(tooltips);
        }

        List<Component> extraTooltips = addExtraTooltips();
        if (!extraTooltips.isEmpty()) {
            if (Screen.hasShiftDown()) {
                tooltip.addAll(extraTooltips);
            } else {
                tooltip.add(Component.translatable("info.zglib.hold_shift").withStyle(ChatFormatting.YELLOW));
            }
        }

    }

    public List<Component> addTooltips() {
        return new ArrayList<Component>();
    }

    public List<Component> addExtraTooltips() {
        return new ArrayList<Component>();
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xFF161D;
    }

    @Override
    public int getBarWidth(ItemStack stack) {

        if (stack.getTag() == null) {
            return 0;
        }
        return getScaledEnergy(stack, 13);

    }

    @Override
    public int getEnergyPerUse(ItemStack stack) {
        return energy_per_use;
    }

    @Override
    public int getExtract(ItemStack container) {
        return energy_extract;
    }

    @Override
    public int getReceive(ItemStack container) {
        return energy_receive;
    }

    @Override
    public int getMaxEnergy(ItemStack container) {
        return max_energy;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {

        if (allowedIn(tab)) {
            list.add(new ItemStack(this));

            ItemStack maxedItem = new ItemStack(this);
            setEnergy(maxedItem, max_energy);
            list.add(maxedItem);
        }

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_40920_, Player p_40921_, InteractionHand p_40922_) {
        ItemStack itemstack = p_40921_.getItemInHand(p_40922_);
        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack p_40875_, Level p_40876_, LivingEntity p_40877_, int p_40878_) {
        return;
    }

    @Override
    public void onUseTick(Level p_40910_, LivingEntity p_40911_, ItemStack p_40912_, int p_40913_) {
        return;
    }

    @Override
    public int getUseDuration(ItemStack p_40938_) {
        return 0;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_40935_) {
        return UseAnim.BOW;
    }

    @Override
    public boolean useOnRelease(ItemStack p_150801_) {
        return false;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 0;
    }

}
