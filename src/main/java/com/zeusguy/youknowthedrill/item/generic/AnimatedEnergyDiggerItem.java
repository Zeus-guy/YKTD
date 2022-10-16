package com.zeusguy.youknowthedrill.item.generic;

import java.util.function.Consumer;

import com.zeusguy.youknowthedrill.config.ServerConfig;
import com.zeusguy.youknowthedrill.item.client.ToolRender;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class AnimatedEnergyDiggerItem extends EnergyDiggerItem implements IAnimatable, ISyncable {

    public AnimationFactory factory = new AnimationFactory(this);
	public String controllerName = "controller";
	public static String itemName = "Tool";
	public static String itemTier = "";
	public static final String ANIMATION_TAG = "Spin";
    private static final String IDLE_ANIMATION_NAME = "idle";
    private static final String ACTIVE_ANIMATION_NAME = "active";
    
    public AnimatedEnergyDiggerItem(Properties properties, ServerConfig.DiggerItemConfig config) {
        super(properties, config);
        GeckoLibNetwork.registerSyncable(this);
    }
    @Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IClientItemExtensions() {
			private final BlockEntityWithoutLevelRenderer renderer = new ToolRender(itemName, itemTier);

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer;
			}
		});
	}

	public <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
        AnimationController<?> controller = new AnimationController<AnimatedEnergyDiggerItem>(this, controllerName, 1, this::predicate);
		data.addAnimationController(controller);
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
        String animationName = (state == 1 ? ACTIVE_ANIMATION_NAME : IDLE_ANIMATION_NAME);
        if (controller.getCurrentAnimation() == null || !controller.getCurrentAnimation().animationName.equals(animationName)) {
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation(animationName));
        }
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return false;
	}
    
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (!entity.level.isClientSide() && hasEnergy(stack)) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel) entity.level);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity);
            GeckoLibNetwork.syncAnimation(target, this, id, 1);
        }
        CompoundTag tag = stack.getOrCreateTag();
        tag.putLong(ANIMATION_TAG, entity.level.getGameTime() + 10);

        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);        
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(ANIMATION_TAG))
        {
            long time = tag.getLong(ANIMATION_TAG);

            if (entity.level.getGameTime() > time) {
                if (!level.isClientSide()) {
                    int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel) level);
                    final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity);
                    GeckoLibNetwork.syncAnimation(target, this, id, 0);
                    tag.remove(ANIMATION_TAG);
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return slotChanged;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return !(ItemStack.isSame(oldStack, newStack) && oldStack.getOrCreateTag().getInt("Energy") == (newStack.getOrCreateTag().getInt("Energy")));
    }
    
}
