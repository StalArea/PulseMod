package ru.openpulse.mod.events.impl;

import net.minecraft.entity.Entity;
import ru.openpulse.mod.events.Event;

public class EventEntityRemoved extends Event {
    public Entity entity;

    public EventEntityRemoved(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
