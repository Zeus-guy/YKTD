package com.zeusguy.youknowthedrill.networking.packet;

import java.util.function.Supplier;

import com.zeusguy.youknowthedrill.item.generic.EnergyDiggerItem;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class ToolModeC2SPacket {
    
    public ToolModeC2SPacket() {

    }

    public ToolModeC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player.getItemInHand(InteractionHand.MAIN_HAND) != null) {
                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (stack.getItem() instanceof EnergyDiggerItem) {
                    ((EnergyDiggerItem)stack.getItem()).changeMode(stack, player);
                }
            }
        });
        return true;
    }
}
