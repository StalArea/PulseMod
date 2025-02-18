package ru.openpulse.mod.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import ru.openpulse.mod.events.impl.EventSync;
import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.setting.Setting;

public class Regen extends Module {
    public Regen() {
        super("Regen", Category.PLAYER);
    }

    private final Setting<Integer> health = new Setting<>("Health", 10, 0, 20);
    private final Setting<Integer> packetsPerTick = new Setting<>("Packets/Tick", 20, 2, 120);

    @EventHandler
    public void onSync(EventSync e) {
        if (mc.player.getHealth() + mc.player.getAbsorptionAmount() <= health.getValue())
            for (int i = 0; i < packetsPerTick.getValue(); i++)
                sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround()));
    }
}
