package com.zeusguy.youknowthedrill.item.generic;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import com.zeusguy.youknowthedrill.YouKnowTheDrill;
import com.zeusguy.youknowthedrill.config.ClientConfig;
import com.zeusguy.youknowthedrill.config.ServerConfig;
import com.zeusguy.youknowthedrill.registry.YKTDCreativeModeTab;
import com.zeusguy.youknowthedrill.util.KeyBinding;
import com.zeusguy.zglib.item.EnergyItem;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class EnergyDiggerItem extends EnergyItem {

    public enum DrillMode {
        NONE(NO_MODE_TAG),
        SILK(SILK_TAG),
        FORTUNE(FORTUNE_TAG);

        private String tag;

        private DrillMode(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }

    }

    protected final Set<ToolAction> DEFAULT_ACTIONS = new ObjectOpenHashSet<>();
    protected final Set<TagKey<Block>> MINEABLE_TAGS = new ObjectOpenHashSet<>();

    public static final String OVERCLOCKED_TAG = "overclocked";
    public static final String MODE_TAG = "mode";
    public static final String NO_MODE_TAG = "none";
    public static final String SILK_TAG = "silk";
    public static final String FORTUNE_TAG = "fortune";

    protected ServerConfig.DiggerItemConfig config;

    EnergyDiggerItem(Properties properties, ServerConfig.DiggerItemConfig config) {
        super(properties.tab(YKTDCreativeModeTab.YKTD_TAB));
        this.config = config;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {

        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        if (slot == EquipmentSlot.MAINHAND) {
            float damage = getAttackDamage(stack);
            float speed = getAttackSpeed(stack);
            if (damage != 0.0F) {
                multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier",
                        damage, AttributeModifier.Operation.ADDITION));
            }
            if (speed != 0.0F) {
                multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier",
                        speed, AttributeModifier.Operation.ADDITION));
            }
        }
        return multimap;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return DEFAULT_ACTIONS.contains(toolAction);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {

        for (TagKey<Block> tag : MINEABLE_TAGS) {
            if (state.is(tag)) {
                return getEfficiency(stack);
            }
        }

        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {

        for (TagKey<Block> tag : MINEABLE_TAGS) {
            if (state.is(tag)) {
                return TierSortingRegistry.isCorrectTierForDrops(getHarvestTier(stack), state);
            }
        }
        return false;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (attacker instanceof Player && !((Player) attacker).getAbilities().instabuild) {
            extractEnergy(stack, getEnergyPerUse(stack) * ServerConfig.energy_digger_hurt_enemy_energy_multiplier.get(),
                    false);
        }
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos,
            LivingEntity entityLiving) {

        if (!worldIn.isClientSide && state.getDestroySpeed(worldIn, pos) != 0.0F) {
            if (entityLiving instanceof Player && !((Player) entityLiving).getAbilities().instabuild) {
                extractEnergy(stack, getEnergyPerUse(stack), false);
            }
        }
        return true;
    }

    @Override
    public boolean isRepairable(ItemStack is) {
        return false;
    }

    protected float getAttackDamage(ItemStack stack) {

        return hasEnergy(stack) ? config.damage.get() : 0.0F;
    }

    protected float getAttackSpeed(ItemStack stack) {

        return hasEnergy(stack) ? -2.4F : -3.0F;
    }

    protected float getEfficiency(ItemStack stack) {
        float multiplier = ServerConfig.energy_digger_overclock_efficiency_multiplier.get().floatValue();
        return hasEnergy(stack) ? config.efficiency.get() * (isOverclocked(stack) ? multiplier : 1) : 1.0F;
    }

    protected Tier getHarvestTier(ItemStack stack) {

        return switch (getHarvestLevel(stack)) {
            case 0 -> Tiers.WOOD;
            case 1 -> Tiers.STONE;
            case 2 -> Tiers.IRON;
            case 3 -> Tiers.DIAMOND;
            case 4 -> Tiers.NETHERITE;
            default -> Tiers.GOLD;
        };
    }

    protected int getHarvestLevel(ItemStack stack) {
        return hasEnergy(stack) ? config.harvest_level.get() : 0;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        ItemStack itemstack = context.getItemInHand();

        if (hasEnergy(itemstack)) {
            Level level = context.getLevel();
            BlockPos blockpos = context.getClickedPos();
            Player player = context.getPlayer();
            BlockState blockstate = level.getBlockState(blockpos);

            // Axe Actions
            if (DEFAULT_ACTIONS.contains(ToolActions.AXE_STRIP) || DEFAULT_ACTIONS.contains(ToolActions.AXE_SCRAPE)
                    || DEFAULT_ACTIONS.contains(ToolActions.AXE_WAX_OFF)) {
                Optional<BlockState> strip = Optional.ofNullable(
                        blockstate.getToolModifiedState(context, ToolActions.AXE_STRIP, false));
                Optional<BlockState> scrape = strip.isPresent() ? Optional.empty()
                        : Optional.ofNullable(blockstate.getToolModifiedState(context,
                                ToolActions.AXE_SCRAPE, false));
                Optional<BlockState> waxOff = strip.isPresent() || scrape.isPresent() ? Optional.empty()
                        : Optional.ofNullable(blockstate.getToolModifiedState(context,
                                ToolActions.AXE_WAX_OFF, false));
                Optional<BlockState> axeOptional = Optional.empty();
                if (strip.isPresent() && DEFAULT_ACTIONS.contains(ToolActions.AXE_STRIP)) {
                    level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
                    axeOptional = strip;
                } else if (scrape.isPresent() && DEFAULT_ACTIONS.contains(ToolActions.AXE_SCRAPE)) {
                    level.playSound(player, blockpos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.levelEvent(player, 3005, blockpos, 0);
                    axeOptional = scrape;
                } else if (waxOff.isPresent() && DEFAULT_ACTIONS.contains(ToolActions.AXE_WAX_OFF)) {
                    level.playSound(player, blockpos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.levelEvent(player, 3004, blockpos, 0);
                    axeOptional = waxOff;
                }

                if (axeOptional.isPresent()) {
                    if (player instanceof ServerPlayer) {
                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
                    }

                    level.setBlock(blockpos, axeOptional.get(), 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, axeOptional.get()));
                    if (player != null) {
                        extractEnergy(itemstack, getEnergyPerUse(itemstack), false);
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }

            // Shovel actions
            if (DEFAULT_ACTIONS.contains(ToolActions.SHOVEL_FLATTEN)) {
                if (context.getClickedFace() != Direction.DOWN) {
                    BlockState blockstate1 = blockstate.getToolModifiedState(context,
                            net.minecraftforge.common.ToolActions.SHOVEL_FLATTEN, false);
                    BlockState blockstate2 = null;
                    if (blockstate1 != null && level.isEmptyBlock(blockpos.above())) {
                        level.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                        blockstate2 = blockstate1;
                    } else if (blockstate.getBlock() instanceof CampfireBlock
                            && blockstate.getValue(CampfireBlock.LIT)) {
                        if (!level.isClientSide()) {
                            level.levelEvent((Player) null, 1009, blockpos, 0);
                        }

                        CampfireBlock.dowse(context.getPlayer(), level, blockpos, blockstate);
                        blockstate2 = blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false));
                    }

                    if (blockstate2 != null) {
                        if (!level.isClientSide) {
                            level.setBlock(blockpos, blockstate2, 11);
                            level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos,
                                    GameEvent.Context.of(player, blockstate2));
                        }
                        if (player != null) {
                            extractEnergy(itemstack, getEnergyPerUse(itemstack), false);
                        }

                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }

            // Hoe actions
            if (DEFAULT_ACTIONS.contains(ToolActions.HOE_TILL)) {
                BlockState toolModifiedState = level.getBlockState(blockpos).getToolModifiedState(context,
                        net.minecraftforge.common.ToolActions.HOE_TILL, false);
                Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null
                        : Pair.of(ctx -> true, changeIntoState(toolModifiedState));
                if (pair != null) {
                    Predicate<UseOnContext> predicate = pair.getFirst();
                    Consumer<UseOnContext> consumer = pair.getSecond();
                    if (predicate.test(context)) {
                        level.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        if (!level.isClientSide) {
                            consumer.accept(context);
                        }
                        if (player != null) {
                            extractEnergy(itemstack, getEnergyPerUse(itemstack), false);
                        }

                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    public static Consumer<UseOnContext> changeIntoState(BlockState p_150859_) {
        return (p_238241_) -> {
            p_238241_.getLevel().setBlock(p_238241_.getClickedPos(), p_150859_, 11);
            p_238241_.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, p_238241_.getClickedPos(),
                    GameEvent.Context.of(p_238241_.getPlayer(), p_150859_));
        };
    }

    public void changeMode(ItemStack stack, Player player, boolean isConfigReversed) {
        boolean condition = player.isCrouching();

        if ((condition && config.overclock.get()) || (!condition && config.modes.get().size() > 1)) {
            player.getLevel().playSound(null, player.getOnPos(), SoundEvents.STONE_BUTTON_CLICK_OFF, SoundSource.PLAYERS,
                    1F, (condition ? (isOverclocked(stack) ? 1 : 1.5F) : 1.2F));
        }

        if (isConfigReversed)
            condition = !condition;
        if (condition) {
            changeOverclockMode(stack);
        } else {
            changeToolMode(stack);
        }
    }

    private void changeOverclockMode(ItemStack stack) {
        setOverclock(stack, !isOverclocked(stack));
    }

    private void changeToolMode(ItemStack stack) {
        DrillMode curMode = getMode(stack);
        if (config.modes.get().contains(curMode.name())) {
            setMode(stack,
                    getDrillModeFromConfig(config.modes.get().get((config.modes.get().indexOf(curMode.name()) + 1) % config.modes.get().size())));
        } else {
            setMode(stack, config.modes.get().size() > 0 ? getDrillModeFromConfig(config.modes.get().get(0)) : DrillMode.NONE);
        }
    }

    public void setOverclock(ItemStack stack, boolean state) {
        stack.getOrCreateTag().putBoolean(OVERCLOCKED_TAG, state && config.overclock.get());
    }

    public boolean isOverclocked(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(OVERCLOCKED_TAG);
    }

    public void setMode(ItemStack stack, DrillMode mode) {
        stack.getOrCreateTag().putString(MODE_TAG, mode.getTag());
    }

    public DrillMode getMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();

        if (!tag.contains(MODE_TAG)) {
            if (config.modes.get().isEmpty()) {
                return DrillMode.NONE;
            } else {
                return getDrillModeFromConfig(config.modes.get().get(0));
            }
        } else {
            // I'll admit it, this is horrifying.
            switch (tag.getString(MODE_TAG)) {
                case SILK_TAG:
                    return DrillMode.SILK;
                case FORTUNE_TAG:
                    return DrillMode.FORTUNE;
                case NO_MODE_TAG:
                default:
                    return DrillMode.NONE;
            }
        }
    }

    private DrillMode getDrillModeFromConfig(String string) {
        return DrillMode.valueOf(string);
    }

    @Override
    public Component getHighlightTip(ItemStack item, Component displayName) {
        MutableComponent component = MutableComponent.create(displayName.getContents());
        component.append(displayName);

        DrillMode mode = getMode(item);
        boolean hasMode = (mode != DrillMode.NONE);
        boolean overclocked = isOverclocked(item);

        if (hasMode || overclocked) {
            component.append(" (");
            if (hasMode) {
                component.append(Component.translatable("info." + YouKnowTheDrill.MODID + "." + mode.getTag()));
                if (overclocked) {
                    component.append(", ");
                }
            }
            if (overclocked) {
                component.append(Component.translatable("info." + YouKnowTheDrill.MODID + ".overclocked"));
            }
            component.append(")");
        }

        return component;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        // Probably not needed anymore, I should check if it is. But of course, I won't.
        getMode(itemstack);
        return super.onBlockStartBreak(itemstack, pos, player);
    }

    @Override
    public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
        Map<Enchantment, Integer> map = Maps.newLinkedHashMap();
        DrillMode mode = getMode(stack);

        switch (mode) {
            case FORTUNE:
                map.put(Enchantments.BLOCK_FORTUNE, 3);
                break;
            case SILK:
                map.put(Enchantments.SILK_TOUCH, 1);
                break;
            default:
                break;
        }
        return map;
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        DrillMode mode = getMode(stack);

        switch (mode) {
            case FORTUNE:
                if (enchantment.equals(Enchantments.BLOCK_FORTUNE))
                    return 3;
                break;
            case SILK:
                if (enchantment.equals(Enchantments.SILK_TOUCH))
                    return 3;
                break;
            default:
                break;
        }
        return super.getEnchantmentLevel(stack, enchantment);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {

        DrillMode mode = getMode(stack);

        if (mode != DrillMode.NONE) {
            tooltip.add(Component.translatable("info." + YouKnowTheDrill.MODID + "." + mode.getTag())
                    .withStyle(ChatFormatting.DARK_AQUA));
        }

        if (isOverclocked(stack)) {
            tooltip.add(Component.translatable("info." + YouKnowTheDrill.MODID + ".overclocked")
                    .withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

    }

    @Override
    public List<Component> addExtraTooltips() {
        List<Component> extraTooltips = super.addExtraTooltips();

        if (config.modes.get().size() > 1) {
            extraTooltips.add(
                    appendToolKey(Component.translatable("info." + YouKnowTheDrill.MODID + ".tooltip_press")
                            .withStyle(ChatFormatting.GRAY), false)
                            .append(Component.translatable("info." + YouKnowTheDrill.MODID + ".tooltip_modes")
                                    .withStyle(ChatFormatting.GRAY)));
        }

        if (config.overclock.get()) {
            extraTooltips
                    .add(appendToolKey(Component.translatable("info." + YouKnowTheDrill.MODID + ".tooltip_press")
                            .withStyle(ChatFormatting.GRAY), true)
                            .append(Component.translatable("info." + YouKnowTheDrill.MODID + ".tooltip_overclocked")
                                    .withStyle(ChatFormatting.GRAY)));
        }

        return extraTooltips;
    }

    private MutableComponent appendToolKey(MutableComponent component, boolean defaultCrouch) {
        boolean condition = defaultCrouch;
        if (ClientConfig.isKeyReversed.get())
            condition = !condition;
        component.append(((MutableComponent) KeyBinding.CHANGE_TOOL_MODE_KEY.getKey().getDisplayName())
                .withStyle(ChatFormatting.GRAY));
        if (condition) {
            component.append(Component.translatable("info." + YouKnowTheDrill.MODID + ".while_shifting")
                    .withStyle(ChatFormatting.GRAY));
        }
        return component;
    }

    @Override
    public int getMaxEnergy(ItemStack container) {
        return config.max_energy.get();
    }

    @Override
    public int getEnergyPerUse(ItemStack stack) {
        return config.energy_per_use.get()
                * (isOverclocked(stack) ? ServerConfig.energy_digger_overclock_energy_multiplier.get() : 1);
    }

    @Override
    public int getExtract(ItemStack container) {
        return config.extract_rate.get();
    }

    @Override
    public int getReceive(ItemStack container) {
        return config.insert_rate.get();
    }
}
