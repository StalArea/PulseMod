package ru.openpulse.mod.events.impl;

import net.minecraft.item.ItemStack;
import ru.openpulse.mod.events.Event;

public class EventEatFood extends Event {
    private final ItemStack stack;

    public EventEatFood(ItemStack stack){
        this.stack = stack;
    }

    public ItemStack getFood() {
        return stack;
    }
}
