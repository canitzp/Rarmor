/*
 * This file ("Config.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class Config{
    
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final ForgeConfigSpec FORGE_CONFIG_SPEC = BUILDER.build();
    
    public static class General {
        public final ForgeConfigSpec.BooleanValue OPEN_CONFIRMATION_PACKET;
        public final ForgeConfigSpec.IntValue OVERLAY_X;
        public final ForgeConfigSpec.IntValue OVERLAY_Y;
        public final ForgeConfigSpec.DoubleValue OVERLAY_SCALE;
        public final ForgeConfigSpec.BooleanValue OVERLAY_ENERGY_ONLY;
        public final ForgeConfigSpec.IntValue INVENTORY_OPENING_MODE;
        public final ForgeConfigSpec.BooleanValue SHOW_INVENTORY_BUTTON;
        
        public General(ForgeConfigSpec.Builder builder){
            OPEN_CONFIRMATION_PACKET = builder
                .comment("Turn this off to disable the packet that gets sent from the client back to the server to ensure that it has gotten all of the data a Rarmor contains before opening its GUI.",
                "Turning this off might reduce server load, but could cause bugs.",
                "Use at your own risk.")
                .translation("Open Confirmation Packet")
                .define("open_confirmation_packet", true);
            OVERLAY_X = builder
                .comment("The X position of the Rarmor overlay. Set this or the y value to a negative number to disable it.")
                .translation("Overlay X")
                .defineInRange("rarmor_overlay_x", 3, 0, 10000);
            OVERLAY_Y = builder
                .comment("The Y position of the Rarmor overlay. Set this or the x value to a negative number to disable it.")
                .translation("Overlay Y")
                .defineInRange("rarmor_overlay_y", 3, 0, 10000);
            OVERLAY_SCALE = builder
                .comment("The scale of the Rarmor overlay")
                .translation("Overlay Scale")
                .defineInRange("rarmor_overlay_scale", 1.0, 0.1, 10.0);
            OVERLAY_ENERGY_ONLY = builder
                .comment("If the Rarmor overlay should only show the energy amount")
                .translation("Overlay Energy only")
                .define("rarmor_overlay_energy_only", false);
            INVENTORY_OPENING_MODE = builder
                .comment("The way the Rarmor GUI can be accessed. 0 is inventory key to open the Rarmor, sneak for normal inventory. 1 is inventory key for normal inventory, sneak to open the Rarmor. 2 is always open the Rarmor, and any other value is never open the Rarmor.")
                .translation("Inventory opening mode")
                .defineInRange("opening_mode", 0, 0, 2);
            SHOW_INVENTORY_BUTTON = builder
                .comment("Show a button to open the Rarmor GUI in the normal inventory")
                .translation("Show inventory button")
                .define("show_inventory_button", true);
        }
    }
    
}
