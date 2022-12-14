package com.zeusguy.youknowthedrill.config;

import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ServerConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        Builder configBuilder = new Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    public static BooleanValue isKeyReversed;

    public static IntValue energy_digger_hurt_enemy_energy_multiplier;
    public static DoubleValue energy_digger_overclock_efficiency_multiplier;
    public static IntValue energy_digger_overclock_energy_multiplier;

    public static DiggerItemConfig iron_drill;
    public static DiggerItemConfig diamond_drill;
    public static DiggerItemConfig netherite_drill;

    public static DiggerItemConfig iron_saw;
    public static DiggerItemConfig diamond_saw;
    public static DiggerItemConfig netherite_saw;

    public static DiggerItemConfig multitool;
    
    
    private static void setupConfig(Builder builder) {
        builder.push("Common tool settings");
        energy_digger_hurt_enemy_energy_multiplier = builder.comment("Energy usage multiplier used when attacking an entity").defineInRange("energy_digger_hurt_enemy_energy_multiplier", 2, 1, 100);
        energy_digger_overclock_efficiency_multiplier = builder.comment("Efficiency multiplier for overclocked tools").defineInRange("energy_digger_overclock_efficiency_multiplier", 1.5F, 1, 100);
        energy_digger_overclock_energy_multiplier = builder.comment("Energy usage multiplier for overclocked tools").defineInRange("energy_digger_overclock_energy_multiplier", 5, 1, 100);
        builder.pop();

        iron_drill = new DiggerItemConfig(50000, 100, 1000, 1000, 4, 6, 2, false, Arrays.asList("NONE"), "iron_drill", builder);
        diamond_drill = new DiggerItemConfig(250000, 100, 5000, 5000, 5, 12, 3, true, Arrays.asList("NONE"), "diamond_drill", builder);
        netherite_drill = new DiggerItemConfig(1000000, 100, 10000, 10000, 6, 24, 4, true, Arrays.asList("SILK", "FORTUNE"), "netherite_drill", builder);
        
        iron_saw = new DiggerItemConfig(50000, 100, 1000, 1000, 4, 6, 2, false, Arrays.asList("NONE"), "iron_saw", builder);
        diamond_saw = new DiggerItemConfig(250000, 100, 5000, 5000, 5, 12, 3, true, Arrays.asList("NONE"), "diamond_saw", builder);
        netherite_saw = new DiggerItemConfig(1000000, 100, 10000, 10000, 6, 24, 4, true, Arrays.asList("SILK", "FORTUNE"), "netherite_saw", builder);
        
        multitool = new DiggerItemConfig(10000000, 100, 100000, 100000, 7, 36, 4, true, Arrays.asList("SILK", "FORTUNE"), "multitool", builder);
    }

    public static class DiggerItemConfig {

        public IntValue max_energy;
        public IntValue energy_per_use;
        public IntValue extract_rate;
        public IntValue insert_rate;
        public IntValue damage;
        public IntValue efficiency;
        public IntValue harvest_level;
        public BooleanValue overclock;
        public ConfigValue<List<? extends String>> modes;
        
        public DiggerItemConfig(int max_energy, int energy_per_use, int extract_rate,
                int insert_rate, int damage, int efficiency, int harvest_level, boolean overclock, List<String> modes, String toolName, Builder builder) {
            builder.push(toolName + " settings");
                this.max_energy = builder.comment("The tool's max energy capacity").defineInRange(toolName + "_max_energy", max_energy, 1, 10000000);
                this.energy_per_use = builder.comment("The tool's energy usage per block mined").defineInRange(toolName + "_energy_per_use", energy_per_use, 1, 10000000);
                this.extract_rate = builder.comment("How much FE/tick can be drained out of the tool").defineInRange(toolName + "_extract_rate", extract_rate, 1, 10000000);
                this.insert_rate = builder.comment("How much FE/tick can be inserted into the tool").defineInRange(toolName + "_insert_rate", insert_rate, 1, 10000000);
                this.damage = builder.comment("The tool's damage").defineInRange(toolName + "_damage", damage, 1, 10000000);
                this.efficiency = builder.comment("The tool's efficiency").defineInRange(toolName + "_efficiency", efficiency, 1, 10000000);
                this.harvest_level = builder.comment("The tool's harvest level").defineInRange(toolName + "_harvest_level", harvest_level, 1, 10000000);
                this.overclock = builder.comment("If true, the tool can be overclocked").define(toolName + "_overclock", overclock);
                this.modes = builder.comment("The tool's available modes\nValid options are \"NONE\", \"SILK\" and \"FORTUNE\"").defineList(toolName + "_modes", modes, entry -> true);
            builder.pop();
        }

    }
}
