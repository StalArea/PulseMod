package ru.openpulse.mod.features.modules.render;

import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.setting.Setting;

public class AspectRatio extends Module {
    public AspectRatio() {
        super("AspectRatio", Category.RENDER);
    }

    public Setting<Float> ratio = new Setting<>("Ratio", 1.78f, 0.1f, 5f);
}
