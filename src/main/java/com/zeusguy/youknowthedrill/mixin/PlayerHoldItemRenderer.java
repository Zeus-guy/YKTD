package com.zeusguy.youknowthedrill.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.zeusguy.youknowthedrill.item.generic.AnimatedEnergyDiggerItem;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

@Mixin(PlayerRenderer.class)
public class PlayerHoldItemRenderer {
    @Inject(at = @At("Head"), method = "getArmPose", cancellable = true)
    private static void getArmPose(AbstractClientPlayer p_117795_, InteractionHand p_117796_,
            CallbackInfoReturnable<HumanoidModel.ArmPose> callback) {
                
        ItemStack itemstack = p_117795_.getItemInHand(p_117796_);
        if (itemstack.getItem() instanceof AnimatedEnergyDiggerItem
                && itemstack.getOrCreateTag().contains(AnimatedEnergyDiggerItem.ANIMATION_TAG)) {
            callback.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
        }

    }
}
