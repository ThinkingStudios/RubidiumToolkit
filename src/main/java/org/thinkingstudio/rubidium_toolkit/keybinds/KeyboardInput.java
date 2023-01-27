package org.thinkingstudio.rubidium_toolkit.keybinds;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.thinkingstudio.rubidium_toolkit.RubidiumToolkit;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyboardInput {
    public static final KeyBinding zoomKey = new KeyBinding(RubidiumToolkit.MODID + ".key.zoom",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            RubidiumToolkit.MODID + ".key.category"
    );

}