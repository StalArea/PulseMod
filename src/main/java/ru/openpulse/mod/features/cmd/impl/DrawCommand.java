package ru.openpulse.mod.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import ru.openpulse.mod.features.cmd.Command;
import ru.openpulse.mod.features.cmd.args.ModuleArgumentType;
import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.features.modules.client.ClientSettings;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class DrawCommand extends Command {
    public DrawCommand() {
        super("draw");
    }

    @Override
    public void executeBuild(@NotNull LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(arg("module", ModuleArgumentType.create()).executes(context -> {
            Module module = context.getArgument("module", Module.class);

            module.setDrawn(!module.isDrawn());

            if(ClientSettings.isRu()){
                sendMessage("Модуль " + Formatting.GREEN + module.getName() + Formatting.WHITE + " теперь " + (module.isDrawn() ? "виден в ArrayList" : "не виден в ArrayList"));
            } else {
                sendMessage(Formatting.GREEN + module.getName() + Formatting.WHITE + " is now " + (module.isDrawn() ? "visible in ArrayList" : "invisible in ArrayList"));
            }

            return SINGLE_SUCCESS;
        }));
    }
}
