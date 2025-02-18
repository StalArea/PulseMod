package ru.openpulse.mod.events.impl;

import net.minecraft.entity.player.PlayerEntity;
import ru.openpulse.mod.events.Event;

public class EventDeath extends Event {
    private final PlayerEntity player;

    public EventDeath(PlayerEntity player) {
        this.player = player;
    }

    public PlayerEntity getPlayer(){
        return player;
    }
}
