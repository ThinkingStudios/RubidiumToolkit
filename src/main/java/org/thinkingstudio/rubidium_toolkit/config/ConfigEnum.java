package org.thinkingstudio.rubidium_toolkit.config;

import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class ConfigEnum {
    public static enum Complexity implements TextProvider
    {
        OFF("Off"),
        SIMPLE("Simple"),
        ADVANCED("Advanced");

        private final String name;

        private Complexity(String name) {
            this.name = name;
        }

        public Component getLocalizedName() {
            return new TextComponent(this.name);
        }
    }

    public static enum Quality implements TextProvider
    {
        OFF("Off"),
        FAST("Fast"),
        FANCY("Fancy");

        private final String name;

        private Quality(String name) {
            this.name = name;
        }

        public Component getLocalizedName() {

            return new TextComponent(this.name);
        }
    }

    public enum ZoomTransitionOptions {
        OFF,
        SMOOTH
    }

    public enum ZoomModes {
        HOLD,
        TOGGLE,
        PERSISTENT
    }

    public enum CinematicCameraOptions {
        OFF,
        VANILLA,
        MULTIPLIED
    }
}
