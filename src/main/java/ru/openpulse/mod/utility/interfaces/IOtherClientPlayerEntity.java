package ru.openpulse.mod.utility.interfaces;

import ru.openpulse.mod.features.modules.combat.Aura;

public interface IOtherClientPlayerEntity {
    void resolve(Aura.Resolver mode);

    void releaseResolver();
}
