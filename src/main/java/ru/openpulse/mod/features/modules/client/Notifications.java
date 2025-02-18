package ru.openpulse.mod.features.modules.client;

import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.setting.Setting;

public final class Notifications extends Module {
    public Notifications() {
        super("Notifications", Category.CLIENT);
    }

    public final Setting<Mode> mode = new Setting<>("Mode", Mode.CrossHair);

    public enum Mode {
        Default, CrossHair, Text
    }
}
