package org.thinkingstudio.rubidium_toolkit.config;

import net.minecraft.network.chat.Component;

public class ConfigEnum {
    public enum QualityMode {
        OFF("Off"),
        SLOW("Slow"),
        FAST("Fast"),
        REALTIME("Realtime");

        private final String name;

        private QualityMode(String name) {
            this.name = name;
        }

        public Component getLocalizedName() {
            return Component.nullToEmpty(this.name);
        }
    }
}
