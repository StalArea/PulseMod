package ru.openpulse.mod.events.impl;

import ru.openpulse.mod.events.Event;
import net.minecraft.util.math.Vec3d;

public class EventPlayerTravel extends Event {
    private Vec3d mVec;
    private boolean pre;

    public EventPlayerTravel(Vec3d mVec, boolean pre) {
        this.mVec = mVec;
        this.pre = pre;
    }

    public Vec3d getmVec() {
        return mVec;
    }

    public boolean isPre() {
        return pre;
    }
}