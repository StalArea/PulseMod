package ru.openpulse.mod.features.modules.misc;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import org.jetbrains.annotations.NotNull;
import ru.openpulse.mod.events.impl.PacketEvent;
import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.setting.Setting;
import ru.openpulse.mod.utility.Timer;

import static ru.openpulse.mod.PulseMod.LOGGER;

public final class AutoDuel extends Module {
    private final Setting<Mode> mode = new Setting<>("Mode", Mode.Accept);
    private final Setting<String> nickname = new Setting<>("Nickname", "06ED");
    private final Setting<Float> delay = new Setting<>("Delay", 2f, 0f, 30f);

    private final Timer timer = new Timer();
    private boolean waiting = false;

    public AutoDuel() {
        super("AutoDuel", Module.Category.MISC);
    }

    @Override
    public void onEnable() {
        waiting = false;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.@NotNull Receive event) {
        if (waiting && timer.passedMs((long) (1000 * delay.getValue()))) {
            clickSlot(0);
            timer.reset();
            waiting = false;
        }

        if (event.getPacket() instanceof GameMessageS2CPacket pac) {
            final String message = pac.content().getString().toLowerCase();

            switch (mode.getValue()) {
                case Accept -> {
                    if (!pac.content.getString().contains("duel request received from " + nickname.getValue().toLowerCase()))
                        return;

                    sendChatCommand("duel accept " + nickname.getValue());
                }
                case Send -> {
                    LOGGER.info(message);

                    if (message.contains("[duels]")
                            && message.contains(nickname.toString().toLowerCase())
                            && message.contains(mc.getSession().getUsername().toLowerCase())) {
                        sendChatCommand("duel " + nickname.getValue());
                        waiting = true;
                    }
                }
            }
        }
    }

    private enum Mode {
        Send,
        Accept
    }

    private enum Server {
        CC,
    }
}
