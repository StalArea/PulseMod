package ru.openpulse.mod.core;

import ru.openpulse.mod.PulseMod;
import ru.openpulse.mod.core.manager.IManager;
import ru.openpulse.mod.core.manager.client.*;
import ru.openpulse.mod.core.manager.player.CombatManager;
import ru.openpulse.mod.core.manager.player.FriendManager;
import ru.openpulse.mod.core.manager.player.PlayerManager;
import ru.openpulse.mod.core.manager.world.HoleManager;
import ru.openpulse.mod.core.manager.world.WayPointManager;
import ru.openpulse.mod.utility.ThunderUtility;

import static ru.openpulse.mod.PulseMod.EVENT_BUS;

/**
 * Class with all PulseMod Managers' instances
 *
 * @author 06ED
 * @see IManager - base interface for all managers
 * @see PulseMod - managers init process
 * @since 1.7
 */
public class Managers {
    // Player
    public static final CombatManager COMBAT = new CombatManager();
    public static final FriendManager FRIEND = new FriendManager();
    public static final PlayerManager PLAYER = new PlayerManager();

    // World
    public static final HoleManager HOLE = new HoleManager(); //todo ???
    public static final WayPointManager WAYPOINT = new WayPointManager();

    // Client
    public static final AddonManager ADDON = new AddonManager();
    public static final AsyncManager ASYNC = new AsyncManager();
    public static final ModuleManager MODULE = new ModuleManager();
    public static final ConfigManager CONFIG = new ConfigManager();
    public static final MacroManager MACRO = new MacroManager();
    public static final NotificationManager NOTIFICATION = new NotificationManager();
    public static final ProxyManager PROXY = new ProxyManager();
    public static final ServerManager SERVER = new ServerManager();
    public static final ShaderManager SHADER = new ShaderManager();
    public static final SoundManager SOUND = new SoundManager();
    public static final CommandManager COMMAND = new CommandManager();

    public static void init() {
        ADDON.initAddons();
        CONFIG.load(CONFIG.getCurrentConfig());
        MODULE.onLoad("none");
        FRIEND.loadFriends();
        MACRO.onLoad();
        WAYPOINT.onLoad();
        PROXY.onLoad();
        SOUND.registerSounds();

        ASYNC.run(() -> {
            ThunderUtility.syncContributors();
            ThunderUtility.parseStarGazer();
            ThunderUtility.parseCommits();
        });
    }

    public static void subscribe() {
        EVENT_BUS.subscribe(NOTIFICATION);
        EVENT_BUS.subscribe(SERVER);
        EVENT_BUS.subscribe(PLAYER);
        EVENT_BUS.subscribe(COMBAT);
        EVENT_BUS.subscribe(ASYNC);
    }
}
