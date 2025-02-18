package ru.openpulse.mod.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.jetbrains.annotations.NotNull;
import ru.openpulse.mod.events.impl.PacketEvent;
import ru.openpulse.mod.injection.accesors.IPlayerMoveC2SPacket;
import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.setting.Setting;

public class AntiHunger extends Module {
    public AntiHunger() {
        super("AntiHunger", Category.PLAYER);
    }

    private final Setting<Boolean> ground = new Setting<>("CancelGround", true);
    private final Setting<Boolean> sprint = new Setting<>("CancelSprint", true);


    @EventHandler
    public void onPacketSend(PacketEvent.@NotNull Send e) {
        if (e.getPacket() instanceof PlayerMoveC2SPacket pac && ground.getValue())
            ((IPlayerMoveC2SPacket) pac).setOnGround(false);

        if (e.getPacket() instanceof ClientCommandC2SPacket pac && sprint.getValue())
            if (pac.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING)
                e.cancel();
    }
}
