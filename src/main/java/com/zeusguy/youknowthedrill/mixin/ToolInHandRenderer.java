package com.zeusguy.youknowthedrill.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.zeusguy.youknowthedrill.config.ClientConfig;
import com.zeusguy.youknowthedrill.item.generic.AnimatedEnergyDiggerItem;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemInHandRenderer.class)
public class ToolInHandRenderer {
    @Inject(at = @At("Head"), method = "renderArmWithItem", cancellable =  true)
    private void renderArmWithItem(AbstractClientPlayer p_109372_, float p_109373_, float p_109374_, InteractionHand p_109375_, float p_109376_, ItemStack p_109377_, float p_109378_, PoseStack p_109379_, MultiBufferSource p_109380_, int p_109381_, CallbackInfo callback) {
        if (!ClientConfig.itemUseFocus.get()) return;
        ItemStack itemstack = p_109372_.getItemInHand(p_109375_);
        if (!itemstack.isEmpty()) {
            if (itemstack.getItem() instanceof AnimatedEnergyDiggerItem
                    && itemstack.getOrCreateTag().contains(AnimatedEnergyDiggerItem.ANIMATION_TAG)) {

                boolean flag = p_109375_ == InteractionHand.MAIN_HAND;
                HumanoidArm humanoidarm = flag ? p_109372_.getMainArm() : p_109372_.getMainArm().getOpposite();
                p_109379_.pushPose();
                boolean flag3 = humanoidarm == HumanoidArm.RIGHT;
                int k = flag3 ? 1 : -1;

                this.applyItemArmTransform(p_109379_, humanoidarm, p_109378_);
                p_109379_.translate((double)((float)k * -0.2785682F), (double)0.18344387F, (double)0.15731531F);
                p_109379_.mulPose(Vector3f.XP.rotationDegrees(-13.935F));
                p_109379_.mulPose(Vector3f.YP.rotationDegrees((float)k * 35.3F));
                p_109379_.mulPose(Vector3f.ZP.rotationDegrees((float)k * -9.785F));

                float f20 = Mth.sin(-0.13F) * 0.9F;
                p_109379_.translate((double)(f20 * 0.0F), (double)(f20 * 0.004F), (double)(f20 * 0.0F));

                p_109379_.translate((double)(1.0F * 0.0F), (double)(1.0F * 0.0F), (double)(1.0F * 0.04F));
                p_109379_.scale(1.0F, 1.0F, 1.0F + 1.0F * 0.2F);
                p_109379_.mulPose(Vector3f.YN.rotationDegrees((float)k * 45.0F));

                this.renderItem(p_109372_, p_109377_, flag3 ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag3, p_109379_, p_109380_, p_109381_);

                p_109379_.popPose();

                callback.cancel();
            }
        }
                
    }

    @Shadow
    private void applyItemArmTransform(PoseStack p_109383_, HumanoidArm p_109384_, float p_109385_) {
        throw new IllegalStateException("Mixin failed to shadow applyItemArmTransform()");
    }

    @Shadow
    public void renderItem(LivingEntity p_109323_, ItemStack p_109324_, ItemTransforms.TransformType p_109325_, boolean p_109326_, PoseStack p_109327_, MultiBufferSource p_109328_, int p_109329_) {
        throw new IllegalStateException("Mixin failed to shadow renderItem()");
    }
}
