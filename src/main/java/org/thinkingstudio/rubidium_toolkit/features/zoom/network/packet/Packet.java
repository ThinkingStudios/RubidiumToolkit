package org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public interface Packet {
    void encode(FriendlyByteBuf buf);

    void handle(NetworkEvent.Context context);
}
