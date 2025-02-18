package ru.openpulse.mod.features.modules.client;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import org.jetbrains.annotations.NotNull;
import ru.openpulse.mod.events.impl.PacketEvent;
import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.setting.Setting;
import ru.openpulse.mod.utility.Timer;
import ru.openpulse.mod.utility.math.MathUtility;

public final class FastLatency extends Module {
    private final Setting<Integer> delay = new Setting<>("Delay", 80, 0, 1000);

    private final Timer timer = new Timer();
    private final Timer limitTimer = new Timer();
    private long ping;
    public int resolvedPing;

    public FastLatency() {
        super("FastLatency", Category.CLIENT);
    }

    @Override
    public void onRender3D(MatrixStack stack) {
        if (timer.passedMs(5000) && limitTimer.every(delay.getValue())) {
            sendPacket(new RequestCommandCompletionsC2SPacket(1337, "w "));
            ping = System.currentTimeMillis();
            timer.reset();
        }
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.@NotNull Receive e) {
        if (e.getPacket() instanceof CommandSuggestionsS2CPacket c && c.id() == 1337) {
            resolvedPing = (int) MathUtility.clamp(System.currentTimeMillis() - ping, 0, 1000);
            timer.setMs(5000);
        }
    }
}
