package com.texstudio.rubidium_toolkit.features.Zoom.network.packet;

import com.texstudio.rubidium_toolkit.features.Zoom.network.RubidiumToolkitNetwork;
import com.texstudio.rubidium_toolkit.features.Zoom.utils.ZoomUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record ForceClassicModePacket(boolean forceClassicMode) implements Packet {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(forceClassicMode);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        RubidiumToolkitNetwork.disableZoomScrolling = forceClassicMode;
        RubidiumToolkitNetwork.forceClassicMode = forceClassicMode;
        RubidiumToolkitNetwork.configureZoomInstance();
        RubidiumToolkitNetwork.checkRestrictions();
        if (forceClassicMode) {
            ZoomUtils.LOGGER.info("This server has imposed classic mode");
        }
    }

    public static ForceClassicModePacket decode(FriendlyByteBuf buf) {
        return new ForceClassicModePacket(buf.readBoolean());
    }
}
