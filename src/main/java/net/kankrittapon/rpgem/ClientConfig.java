package net.kankrittapon.rpgem;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ClientConfig {
    public static final ModConfigSpec SPEC;
    public static final ClientConfig INSTANCE;

    static {
        Pair<ClientConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(ClientConfig::new);
        SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }

    public final ModConfigSpec.BooleanValue HUD_ENABLED;
    public final ModConfigSpec.IntValue HUD_X_OFFSET;
    public final ModConfigSpec.IntValue HUD_Y_OFFSET;

    public ClientConfig(ModConfigSpec.Builder builder) {
        builder.push("hud");

        HUD_ENABLED = builder
                .comment("Enable or disable the RPG Stats Overlay")
                .define("enableOverlay", false);

        HUD_X_OFFSET = builder
                .comment("X Offset from the right side of the screen")
                .defineInRange("xOffset", 5, 0, 10000);

        HUD_Y_OFFSET = builder
                .comment("Y Offset from the bottom of the screen")
                .defineInRange("yOffset", 10, 0, 10000);

        builder.pop();
    }
}
