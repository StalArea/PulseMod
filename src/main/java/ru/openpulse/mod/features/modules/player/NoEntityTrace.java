package ru.openpulse.mod.features.modules.player;

import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.setting.Setting;

public final class NoEntityTrace extends Module {
    public NoEntityTrace() {
        super("NoEntityTrace", Category.PLAYER);
    }

    public static final Setting<Boolean> ponly = new Setting<>("Pickaxe Only", true);
    public static final Setting<Boolean> noSword = new Setting<>("No Sword", true);
}