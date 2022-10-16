package com.zeusguy.youknowthedrill.event;

import com.zeusguy.youknowthedrill.YouKnowTheDrill;
import com.zeusguy.youknowthedrill.networking.YKTDMessages;
import com.zeusguy.youknowthedrill.networking.packet.ToolModeC2SPacket;
import com.zeusguy.youknowthedrill.util.KeyBinding;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = YouKnowTheDrill.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBinding.CHANGE_TOOL_MODE_KEY.consumeClick()) {
                YKTDMessages.sendToServer(new ToolModeC2SPacket());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = YouKnowTheDrill.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.CHANGE_TOOL_MODE_KEY);
        }
    }
}
