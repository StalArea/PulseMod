package ru.openpulse.mod.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.NotNull;
import ru.openpulse.mod.core.Managers;
import ru.openpulse.mod.features.cmd.Command;
import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.setting.impl.Bind;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class ResetBindsCommand extends Command {
    public ResetBindsCommand() {
        super("resetbinds", "unbind", "fuckbinds");
    }

    @Override
    public void executeBuild(@NotNull LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            for (Module mod : Managers.MODULE.modules) mod.setBind(new Bind(-1, false, false));
            return SINGLE_SUCCESS;
        });
    }
}
