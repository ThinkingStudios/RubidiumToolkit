package org.thinkingstudio.rubidium_toolkit.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyBindingMap;
import org.thinkingstudio.rubidium_toolkit.RubidiumToolkit;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyboardInput {
    public static final KeyMapping zoomKey = new KeyMapping(RubidiumToolkit.MODID + ".key.zoom",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            RubidiumToolkit.MODID + ".key.category"
    );

}