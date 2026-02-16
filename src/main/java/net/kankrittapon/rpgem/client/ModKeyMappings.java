package net.kankrittapon.rpgem.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {
    public static final String CATEGORY = "key.category.rpgem";
    public static final String OPEN_FAIRY_GUI = "key.rpgem.open_fairy_gui";

    public static final KeyMapping OPEN_FAIRY_GUI_KEY = new KeyMapping(
            OPEN_FAIRY_GUI,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_SLASH,
            CATEGORY);
}
