package com.zeusguy.youknowthedrill.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    public static ForgeConfigSpec.BooleanValue isKeyReversed;
    public static ForgeConfigSpec.BooleanValue itemUseFocus;

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Pressing the mode change hotkey will change the held tool's mode, and pressing it while crouching will toggle the tool's overclocked mode.\nIf set to true, this behaviour will be reversed.");
        isKeyReversed = builder.define("tool_change_reversed", false);
        
        builder.comment("If set to true, tools will move to the center of the screen while they're in use (similar to what happens when using a bow).");
        itemUseFocus = builder.define("item_use_focus", false);
    }
}
