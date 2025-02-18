package ru.openpulse.mod.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ru.openpulse.mod.features.cmd.Command;
import ru.openpulse.mod.utility.world.ExplosionUtility;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static ru.openpulse.mod.features.modules.client.ClientSettings.isRu;

public class BenchMarkCommand extends Command {
    public BenchMarkCommand() {
        super("benchmark");
    }

    @Override
    public void executeBuild(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {

            new Thread(() -> {
                long time = System.currentTimeMillis();

                BlockPos playerPos = BlockPos.ofFloored(mc.player.getPos());
                int r = 6;

                for (int x = playerPos.getX() - r; x <= playerPos.getX() + r; x++)
                    for (int y = playerPos.getY() - r; y <= playerPos.getY() + r; y++)
                        for (int z = playerPos.getZ() - r; z <= playerPos.getZ() + r; z++) {
                            float dmg = ExplosionUtility.getExplosionDamage(new Vec3d(x, y, z), mc.player, false);
                        }

                time = System.currentTimeMillis() - time;
                int score = (int) ((216f / (float) time) * 10000);
                sendMessage((isRu() ? "Твой CPU набрал: " : "Your CPU score: ") + Formatting.GRAY + score);
            }).start();

            return SINGLE_SUCCESS;
        });
    }
}
