package ru.openpulse.mod.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.NotNull;
import ru.openpulse.mod.features.cmd.Command;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class GarbageCleanerCommand extends Command {
    public GarbageCleanerCommand() {
        super("gc", "garbagecleaner", "clearram");
    }

    @Override
    public void executeBuild(@NotNull LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            sendMessage("Cleaning RAM..");
            System.gc();
            sendMessage("Successfully cleaned RAM!");
            return SINGLE_SUCCESS;
        });
    }
}
