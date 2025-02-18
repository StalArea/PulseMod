package ru.openpulse.mod.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import ru.openpulse.mod.events.impl.PacketEvent;
import ru.openpulse.mod.features.modules.Module;

public class XCarry extends Module {
    public XCarry() {
        super("XCarry", Category.PLAYER);
    }

    @EventHandler
    public void onPacketSend(PacketEvent.Send e) {
        if (e.getPacket() instanceof CloseHandledScreenC2SPacket) e.cancel();
    }
}
