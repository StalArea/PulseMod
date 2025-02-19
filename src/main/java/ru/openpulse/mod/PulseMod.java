package ru.openpulse.mod;

import com.mojang.logging.LogUtils;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import ru.openpulse.mod.core.Core;
import ru.openpulse.mod.core.Managers;
import ru.openpulse.mod.core.hooks.ManagerShutdownHook;
import ru.openpulse.mod.core.hooks.ModuleShutdownHook;
import ru.openpulse.mod.core.manager.client.ModuleManager;
import ru.openpulse.mod.utility.PulseUtility;
import ru.openpulse.mod.utility.render.Render2DEngine;

import java.awt.*;
import java.lang.invoke.MethodHandles;

public class PulseMod implements ModInitializer {
    public static final ModMetadata MOD_META;

    public static final String MOD_ID = "modernfix";
    public static final String VERSION = "1.0";
    public static String GITHUB_HASH = "0";
    public static String BUILD_DATE = "1 Jan 1970";

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Runtime RUNTIME = Runtime.getRuntime();

    public static final boolean baritone = FabricLoader.getInstance().isModLoaded("baritone")
            || FabricLoader.getInstance().isModLoaded("baritone-meteor");

    public static final IEventBus EVENT_BUS = new EventBus();
    public static String[] contributors = new String[32];
    public static Color copy_color = new Color(-1);
    public static KeyListening currentKeyListener;
    public static boolean isOutdated = false;
    public static BlockPos gps_position;
    public static float TICK_TIMER = 1f;
    public static MinecraftClient mc;
    public static long initTime;

    public static Core core = new Core();

    static {
        MOD_META = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata();
    }

    @Override
    public void onInitialize() {
        mc = MinecraftClient.getInstance();
        initTime = System.currentTimeMillis();

        BUILD_DATE = PulseUtility.readManifestField("Build-Timestamp");
        GITHUB_HASH = PulseUtility.readManifestField("Git-Commit");
        PulseUtility.syncVersion();

        EVENT_BUS.registerLambdaFactory("ru.openpulse.mod",
                (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
        EVENT_BUS.subscribe(core);

        Managers.init();
        Managers.subscribe();

        Render2DEngine.initShaders();
        ModuleManager.rpc.startRpc();

        LOGGER.info("[PulseMod] Init time: {} ms.", System.currentTimeMillis() - initTime);
        initTime = System.currentTimeMillis();

        RUNTIME.addShutdownHook(new ManagerShutdownHook());
        RUNTIME.addShutdownHook(new ModuleShutdownHook());
    }

    public static boolean isFuturePresent() {
        return FabricLoader.getInstance().getModContainer("future").isPresent();
    }

    public enum KeyListening {
        ThunderGui, ClickGui, Search, Sliders, Strings
    }
}