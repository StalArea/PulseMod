package ru.openpulse.mod.features.modules.misc;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import org.jetbrains.annotations.NotNull;
import ru.openpulse.mod.core.Managers;
import ru.openpulse.mod.events.impl.PacketEvent;
import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.setting.Setting;

import static ru.openpulse.mod.features.modules.combat.Criticals.getEntity;

public final class AntiAttack extends Module {
    private final Setting<Boolean> friend = new Setting<>("Friend", true);
    private final Setting<Boolean> zoglin = new Setting<>("Zoglin", true);
    private final Setting<Boolean> villager = new Setting<>("Villager", false);
    private final Setting<Boolean> oneHp = new Setting<>("OneHp", false);
    private final Setting<Float> hp = new Setting<>("Hp", 1f, 0f, 20f, v -> oneHp.getValue());

    public AntiAttack() {
        super("AntiAttack", Category.PLAYER);
    }

    @EventHandler
    @SuppressWarnings("unused")
    private void onPacketSend(PacketEvent.@NotNull Send e) {
        if (e.getPacket() instanceof PlayerInteractEntityC2SPacket pac) {
            Entity entity = getEntity(pac);
            if (entity == null) return;
            if (Managers.FRIEND.isFriend(entity.getName().getString()) && friend.getValue())
                e.cancel();
            if (entity instanceof ZombifiedPiglinEntity && zoglin.getValue())
                e.cancel();
            if (entity instanceof VillagerEntity && villager.getValue()) {
                e.cancel();
            } else if (oneHp.getValue() && entity instanceof LivingEntity lent) {
                if (lent.getHealth() <= hp.getValue()) {
                    e.cancel();
                }
            }
        }
    }
}
