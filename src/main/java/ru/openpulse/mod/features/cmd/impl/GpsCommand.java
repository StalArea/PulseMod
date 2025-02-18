package ru.openpulse.mod.features.cmd.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import ru.openpulse.mod.PulseMod;
import ru.openpulse.mod.features.cmd.Command;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class GpsCommand extends Command {
    public GpsCommand() {
        super("gps");
    }

    @Override
    public void executeBuild(@NotNull LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("off").executes(context -> {
            PulseMod.gps_position = null;
            return SINGLE_SUCCESS;
        }));

        builder.then(arg("x", IntegerArgumentType.integer())
                .then(arg("z", IntegerArgumentType.integer()).executes(context -> {
                    final int x = context.getArgument("x", Integer.class);
                    final int z = context.getArgument("z", Integer.class);
                    PulseMod.gps_position = new BlockPos(x, 0, z);

                    sendMessage("GPS настроен на X: " + PulseMod.gps_position.getX() + " Z: " + PulseMod.gps_position.getZ());
                    return SINGLE_SUCCESS;
                })));
        builder.executes(context -> {
            sendMessage("Попробуй .gps off / .gps x z");
            return SINGLE_SUCCESS;
        });
    }
}
