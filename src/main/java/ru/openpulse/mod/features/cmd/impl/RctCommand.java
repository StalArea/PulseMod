package ru.openpulse.mod.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.scoreboard.ScoreboardObjective;
import ru.openpulse.mod.PulseMod;
import ru.openpulse.mod.core.Managers;
import ru.openpulse.mod.features.cmd.Command;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static ru.openpulse.mod.features.modules.client.ClientSettings.isRu;

public class RctCommand extends Command {
    public RctCommand() {
        super("rct");
    }

    @Override
    public void executeBuild(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            String sName = mc.player.networkHandler.getServerInfo() == null ? "none" : mc.player.networkHandler.getServerInfo().address;

            if (!sName.contains("funtime") && !sName.contains("spookytime")) {
                sendMessage(isRu() ? "Rct работает только на фанике и спуки" : "Rct works only on funtime and spookytime");
                return SINGLE_SUCCESS;
            }

            String an = "an" + ((ScoreboardObjective) mc.player.getScoreboard().getObjectives().toArray()[0]).getDisplayName().getString().substring(10);

            Managers.ASYNC.run(() -> {
                mc.player.networkHandler.sendCommand("hub");
                long failSafe = System.currentTimeMillis();
                while (PulseMod.core.getSetBackTime() > 600) {
                    if (System.currentTimeMillis() - failSafe > 1000)
                        break;
                }
                mc.player.networkHandler.sendCommand(an);
            });
            return SINGLE_SUCCESS;
        });
    }
}
