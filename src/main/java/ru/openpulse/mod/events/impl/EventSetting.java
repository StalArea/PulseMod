package ru.openpulse.mod.events.impl;

import ru.openpulse.mod.events.Event;
import ru.openpulse.mod.setting.Setting;

public class EventSetting extends Event {
    final Setting<?> setting;

    public EventSetting(Setting<?> setting){
        this.setting = setting;
    }

    public Setting<?> getSetting() {
        return setting;
    }
}
