package ru.openpulse.mod.api;

import com.mojang.logging.LogUtils;
import ru.openpulse.mod.features.cmd.Command;
import ru.openpulse.mod.features.hud.HudElement;
import ru.openpulse.mod.features.modules.Module;

import java.util.List;

public interface IAddon {
    void onInitialize();

    List<Module> getModules();

    List<Command> getCommands();

    List<HudElement> getHudElements();

    String getPackage();

    String getName();

    String getAuthor();

    String getRepo();

    String getVersion();

    default String getDescription() {
        return "";
    }

    default void onShutdown() {
        LogUtils.getLogger().info("Shutting down addon: " + getName());
    }
}