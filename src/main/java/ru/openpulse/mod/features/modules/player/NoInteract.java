package ru.openpulse.mod.features.modules.player;

import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.setting.Setting;

public class NoInteract extends Module {
    public NoInteract() {
        super("NoInteract", Category.PLAYER);
    }

    public static Setting<Boolean> onlyAura = new Setting<>("OnlyAura", false);
}
