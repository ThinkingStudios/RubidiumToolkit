package org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet;

import org.thinkingstudio.rubidium_toolkit.features.zoom.network.RubidiumToolkitNetwork;
import org.thinkingstudio.rubidium_toolkit.features.zoom.utils.ZoomUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record DisableZoomPacket(boolean disableZoom) implements Packet {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(disableZoom);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        RubidiumToolkitNetwork.disableZoom = disableZoom;
        RubidiumToolkitNetwork.checkRestrictions();
        if (disableZoom) {
            ZoomUtils.LOGGER.info("This server has disabled zooming");
        }
        RubidiumToolkitNetwork.configureZoomInstance();
    }

    public static DisableZoomPacket decode(FriendlyByteBuf buf) {
        return new DisableZoomPacket(buf.readBoolean());
    }
}
