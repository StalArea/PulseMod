package ru.openpulse.mod.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import ru.openpulse.mod.events.impl.PacketEvent;
import ru.openpulse.mod.injection.accesors.IPlayerPositionLookS2CPacket;
import ru.openpulse.mod.features.modules.Module;

public class NoServerRotate extends Module {
    public NoServerRotate() {
        super("NoServerRotate", Category.PLAYER);
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive e) {
        if (fullNullCheck()) return;
        if (e.getPacket() instanceof PlayerPositionLookS2CPacket pac) {
            ((IPlayerPositionLookS2CPacket) pac).setYaw(mc.player.getYaw());
            ((IPlayerPositionLookS2CPacket) pac).setPitch(mc.player.getPitch());
        }
    }
}
