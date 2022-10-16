package com.zeusguy.youknowthedrill;

import com.zeusguy.youknowthedrill.config.ClientConfig;
import com.zeusguy.youknowthedrill.config.ServerConfig;
import com.zeusguy.youknowthedrill.networking.YKTDMessages;
import com.zeusguy.youknowthedrill.registry.YKTDItems;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(YouKnowTheDrill.MODID)
public class YouKnowTheDrill {
    public static final String MODID = "youknowthedrill";

    public YouKnowTheDrill() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.GENERAL_SPEC, "yktd-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.GENERAL_SPEC, "yktd-server.toml");
        
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        YKTDItems.register(modEventBus);

        GeckoLib.initialize();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            YKTDMessages.register();
        });
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
