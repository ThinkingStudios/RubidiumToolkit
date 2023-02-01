package org.thinkingstudio.rubidium_toolkit.keybinds;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import org.thinkingstudio.rubidium_toolkit.RubidiumToolkit;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyboardInput
{
    public static final KeyBinding zoomKey = new KeyBinding(RubidiumToolkit.MODID + ".key.zoom",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            RubidiumToolkit.MODID + ".key.category");

}