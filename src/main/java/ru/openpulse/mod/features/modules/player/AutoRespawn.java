package ru.openpulse.mod.features.modules.player;

import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.util.Formatting;
import ru.openpulse.mod.core.Managers;
import ru.openpulse.mod.core.manager.world.WayPointManager;
import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.setting.Setting;
import ru.openpulse.mod.utility.Timer;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn", Category.PLAYER);
    }

    private final Setting<Boolean> deathcoords = new Setting<>("deathcoords", true);
    private final Setting<Boolean> autokit = new Setting<>("Auto Kit", false);
    private final Setting<String> kit = new Setting<>("kit name", "kitname", v -> autokit.getValue());
    private final Setting<Boolean> autohome = new Setting<>("Auto Home", false);
    private final Setting<Boolean> autowaypoint = new Setting<>("Auto Waypoint", false);

    private boolean flag;
    private int waypointCount = 0;
    private final Timer timer = new Timer();

    @Override
    public void onUpdate() {
        if (fullNullCheck()) return;

        if (timer.passedMs(2100)) {
            timer.reset();
        }

        if (mc.currentScreen instanceof DeathScreen) {
            if (flag){
                waypointCount += 1;
                if(deathcoords.getValue())
                    sendMessage(Formatting.GOLD + "[PlayerDeath] " + Formatting.YELLOW + (int) mc.player.getX() + " " + (int) mc.player.getY() + " " + (int) mc.player.getZ());
                if(autowaypoint.getValue()) {
                    WayPointManager.WayPoint wp = new WayPointManager.WayPoint((int) mc.player.getX(), (int) mc.player.getY(), (int) mc.player.getZ(), "Death №" + waypointCount, (mc.isInSingleplayer() ? "SinglePlayer" : mc.getNetworkHandler().getServerInfo().address), mc.world.getRegistryKey().getValue().getPath());
                    Managers.WAYPOINT.addWayPoint(wp);
                }
                mc.player.requestRespawn();
                mc.setScreen(null);

                Managers.ASYNC.run(()-> {
                    if (autokit.getValue() && mc.player != null) {
                        mc.player.networkHandler.sendChatCommand("kit " + kit.getValue());
                    }
                    if (autohome.getValue() && mc.player != null) {
                        mc.player.networkHandler.sendChatCommand("home");
                    }
                },1000);
                flag = false;
            }
        } else {
            flag = true;
        }
    }
}
