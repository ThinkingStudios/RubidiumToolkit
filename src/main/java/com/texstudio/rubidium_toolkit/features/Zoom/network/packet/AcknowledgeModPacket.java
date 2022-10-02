package com.texstudio.rubidium_toolkit.features.Zoom.network.packet;

import com.texstudio.rubidium_toolkit.RubidiumToolkitClient;
import com.texstudio.rubidium_toolkit.features.Zoom.network.RubidiumToolkitNetwork;
import com.texstudio.rubidium_toolkit.features.Zoom.utils.ZoomUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public record AcknowledgeModPacket() implements Packet {

    public static AcknowledgeModPacket decode(FriendlyByteBuf buf) {
        return new AcknowledgeModPacket();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            final var res = RubidiumToolkitNetwork.checkRestrictions();
            if (res == RubidiumToolkitNetwork.Acknowledgement.HAS_RESTRICTIONS) {
                ZoomUtils.LOGGER.info("This server acknowledges the mod and has established some restrictions");
                doSendToast(new TranslatableComponent("toast.rubidium_toolkit.acknowledge_mod_restrictions"));
            } else {
                ZoomUtils.LOGGER.info("This server acknowledges the mod and establishes no restrictions");
                doSendToast(new TranslatableComponent("toast.rubidium_toolkit.acknowledge_mod"));
            }
        });
    }

    private void doSendToast(Component component) {
        RubidiumToolkitClient.sendToast(component);
    }
}
