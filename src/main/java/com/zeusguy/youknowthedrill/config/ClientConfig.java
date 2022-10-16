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

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Pressing the mode change hotkey will change the held tool's mode, and pressing it while crouching will toggle the tool's overclocked mode.\nIf set to true, this behaviour will be reversed.");
        isKeyReversed = builder.define("tool_change_reversed", false);
    }
}
