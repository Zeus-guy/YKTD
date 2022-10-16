package com.zeusguy.youknowthedrill.item.client;

import com.zeusguy.youknowthedrill.YouKnowTheDrill;
import com.zeusguy.youknowthedrill.item.generic.AnimatedEnergyDiggerItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ToolModel extends AnimatedGeoModel<AnimatedEnergyDiggerItem>{

    private String toolName, toolTier;
    public ToolModel(String name, String tier) {
        this.toolName = name;
        this.toolTier = tier;
    }

    @Override
    public ResourceLocation getAnimationResource(AnimatedEnergyDiggerItem animatable) {
        return new ResourceLocation(YouKnowTheDrill.MODID, "animations/" + toolName + ".json");
    }

    @Override
    public ResourceLocation getModelResource(AnimatedEnergyDiggerItem object) {
        return new ResourceLocation(YouKnowTheDrill.MODID, "geo/" + toolName + ".json");
    }

    @Override
    public ResourceLocation getTextureResource(AnimatedEnergyDiggerItem object) {
        return new ResourceLocation(YouKnowTheDrill.MODID, "textures/item/" + toolTier + "_" + toolName + ".png");
    }
    
}
