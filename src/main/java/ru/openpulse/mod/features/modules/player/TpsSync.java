package ru.openpulse.mod.features.modules.player;

import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import ru.openpulse.mod.PulseMod;
import ru.openpulse.mod.core.Managers;
import ru.openpulse.mod.core.manager.client.ModuleManager;
import ru.openpulse.mod.events.impl.EventTick;
import ru.openpulse.mod.features.modules.Module;

public class TpsSync extends Module {
    public TpsSync() {
        super("TpsSync", Module.Category.PLAYER);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTick(EventTick e) {
        if (ModuleManager.timer.isEnabled()) return;
        if (Managers.SERVER.getTPS() > 1)
            PulseMod.TICK_TIMER = Managers.SERVER.getTPS() / 20f;
        else PulseMod.TICK_TIMER = 1f;
    }

    @Override
    public void onDisable() {
        PulseMod.TICK_TIMER = 1f;
    }
}
