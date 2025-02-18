package ru.openpulse.mod.features.modules.render;

import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.setting.Setting;

public class Fullbright extends Module {
    public Fullbright() {
        super("Fullbright", Category.RENDER);
    }

    public static Setting<Float> minBright = new Setting<>("MinBright", 0.5f, 0f, 1f);
}
