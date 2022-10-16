package com.zeusguy.youknowthedrill.registry;

import com.zeusguy.youknowthedrill.YouKnowTheDrill;
import com.zeusguy.youknowthedrill.config.ServerConfig;
import com.zeusguy.youknowthedrill.item.drills.DrillDiamond;
import com.zeusguy.youknowthedrill.item.drills.DrillIron;
import com.zeusguy.youknowthedrill.item.drills.DrillNetherite;
import com.zeusguy.youknowthedrill.item.generic.CatalystItem;
import com.zeusguy.youknowthedrill.item.generic.MultiTool;
import com.zeusguy.youknowthedrill.item.saws.SawDiamond;
import com.zeusguy.youknowthedrill.item.saws.SawIron;
import com.zeusguy.youknowthedrill.item.saws.SawNetherite;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class YKTDItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, YouKnowTheDrill.MODID);
    
    public static final RegistryObject<Item> IRON_DRILL = ITEMS.register("iron_drill", () -> new DrillIron(new Item.Properties(), ServerConfig.iron_drill));
    public static final RegistryObject<Item> DIAMOND_DRILL = ITEMS.register("diamond_drill", () -> new DrillDiamond(new Item.Properties(), ServerConfig.diamond_drill));
    public static final RegistryObject<Item> NETHERITE_DRILL = ITEMS.register("netherite_drill", () -> new DrillNetherite(new Item.Properties(), ServerConfig.netherite_drill));

    public static final RegistryObject<Item> IRON_SAW = ITEMS.register("iron_saw", () -> new SawIron(new Item.Properties(), ServerConfig.iron_saw));
    public static final RegistryObject<Item> DIAMOND_SAW = ITEMS.register("diamond_saw", () -> new SawDiamond(new Item.Properties(), ServerConfig.diamond_saw));
    public static final RegistryObject<Item> NETHERITE_SAW = ITEMS.register("netherite_saw", () -> new SawNetherite(new Item.Properties(), ServerConfig.netherite_saw));
    
    public static final RegistryObject<Item> ECHO_MULTITOOL = ITEMS.register("echo_multitool", () -> new MultiTool(new Item.Properties(), ServerConfig.multitool));

    public static final RegistryObject<Item> SILK_CATALYST = ITEMS.register("silk_catalyst", () -> new CatalystItem(new Item.Properties(), "enchantment.minecraft.silk_touch"));
    public static final RegistryObject<Item> FORTUNE_CATALYST = ITEMS.register("fortune_catalyst", () -> new CatalystItem(new Item.Properties(), "enchantment.minecraft.fortune"));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
