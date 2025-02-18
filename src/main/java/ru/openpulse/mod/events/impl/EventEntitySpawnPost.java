package ru.openpulse.mod.events.impl;

import net.minecraft.entity.Entity;
import ru.openpulse.mod.events.Event;

public class EventEntitySpawnPost extends Event {
    private final Entity entity;

    public EventEntitySpawnPost(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}
