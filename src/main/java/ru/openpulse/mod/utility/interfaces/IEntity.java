package ru.openpulse.mod.utility.interfaces;

import net.minecraft.util.math.BlockPos;
import ru.openpulse.mod.features.modules.render.Trails;

import java.util.List;

public interface IEntity {
    List<Trails.Trail> getTrails();

    BlockPos thunderHack_Recode$getVelocityBP();
}
