package ru.openpulse.mod.events.impl;

import net.minecraft.client.gui.screen.Screen;
import ru.openpulse.mod.events.Event;

public class EventScreen extends Event {
    private final Screen screen;

    public EventScreen(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }
}
