package ru.openpulse.mod.features.modules.render;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import ru.openpulse.mod.events.impl.PacketEvent;
import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.setting.Setting;
import ru.openpulse.mod.setting.impl.BooleanSettingGroup;
import ru.openpulse.mod.setting.impl.ColorSetting;

import java.awt.*;

public class WorldTweaks extends Module {
    public WorldTweaks() {
        super("WorldTweaks", Category.RENDER);
    }

    public static final Setting<BooleanSettingGroup> fogModify = new Setting<>("FogModify", new BooleanSettingGroup(true));
    public static final Setting<Integer> fogStart = new Setting<>("FogStart", 0, 0, 256).addToGroup(fogModify);
    public static final Setting<Integer> fogEnd = new Setting<>("FogEnd", 64, 10, 256).addToGroup(fogModify);
    public static final Setting<ColorSetting> fogColor = new Setting<>("FogColor", new ColorSetting(new Color(0xA900FF))).addToGroup(fogModify);
    public final Setting<Boolean> ctime = new Setting<>("ChangeTime", false);
    public final Setting<Integer> ctimeVal = new Setting<>("Time", 21, 0, 23);

    long oldTime;

    @Override
    public void onEnable() {
        oldTime = mc.world.getTime();
    }

    @Override
    public void onDisable() {
        mc.world.setTimeOfDay(oldTime);
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket && ctime.getValue()) {
            oldTime = ((WorldTimeUpdateS2CPacket) event.getPacket()).getTime();
            event.cancel();
        }
    }

    @Override
    public void onUpdate() {
        if (ctime.getValue()) mc.world.setTimeOfDay(ctimeVal.getValue() * 1000);
    }
}
