package com.zeusguy.youknowthedrill.jei;

import com.zeusguy.youknowthedrill.YouKnowTheDrill;
import com.zeusguy.youknowthedrill.item.generic.EnergyDiggerItem;
import com.zeusguy.youknowthedrill.registry.YKTDItems;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

@JeiPlugin
public class YouKnowTheDrillJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(YouKnowTheDrill.MODID, "jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        for (RegistryObject<Item> registryItem : YKTDItems.ITEMS.getEntries()) {
            if (registryItem.get() instanceof EnergyDiggerItem) {
                registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, registryItem.get(), (stack, context) -> {
                    if (context == UidContext.Ingredient) {
                        if (stack.getOrCreateTag().getInt("Energy") != 0) {
                            return "filled";
                        } else {
                            return "empty";
                        }
                    }
                    return IIngredientSubtypeInterpreter.NONE;
                });
            }
        }
    }
    
}
