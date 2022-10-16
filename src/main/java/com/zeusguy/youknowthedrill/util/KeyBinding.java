package com.zeusguy.youknowthedrill.util;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.zeusguy.youknowthedrill.YouKnowTheDrill;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyBinding {
    public static final String KEY_CATEGORY_YKTD = "key.category." + YouKnowTheDrill.MODID + ".yktd";
    public static final String KEY_TOOL_MODE = "key." + YouKnowTheDrill.MODID + ".tool_mode";

    public static final KeyMapping CHANGE_TOOL_MODE_KEY = new KeyMapping(KEY_TOOL_MODE, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_YKTD);
}
