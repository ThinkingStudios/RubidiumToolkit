package com.texstudio.rubidium_toolkit.features.Zoom.network.packet;

import com.texstudio.rubidium_toolkit.features.Zoom.network.RubidiumToolkitNetwork;
import com.texstudio.rubidium_toolkit.features.Zoom.utils.ZoomUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record ResetRestrictionsPacket() implements Packet {
    @Override
    public void encode(FriendlyByteBuf buf) {

    }

    @Override
    public void handle(NetworkEvent.Context context) {
        ZoomUtils.LOGGER.info("Disconnected from server... Resetting restrictions.");
        RubidiumToolkitNetwork.resetRestrictions();
    }

    public static ResetRestrictionsPacket decode(FriendlyByteBuf buf) {
        return new ResetRestrictionsPacket();
    }
}
