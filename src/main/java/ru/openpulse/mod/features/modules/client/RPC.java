package ru.openpulse.mod.features.modules.client;

import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.AddServerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import ru.openpulse.mod.PulseMod;
import ru.openpulse.mod.core.Managers;
import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.setting.Setting;
import ru.openpulse.mod.utility.Timer;
import ru.openpulse.mod.utility.discord.DiscordEventHandlers;
import ru.openpulse.mod.utility.discord.DiscordRPC;
import ru.openpulse.mod.utility.discord.DiscordRichPresence;

import java.io.*;
import java.util.Objects;

import static ru.openpulse.mod.features.modules.client.ClientSettings.isRu;

public final class RPC extends Module {
    private static final DiscordRPC rpc = DiscordRPC.INSTANCE;
    public static Setting<Mode> mode = new Setting<>("Picture", Mode.Recode);
    public static Setting<Boolean> showIP = new Setting<>("ShowIP", true);
    public static Setting<sMode> smode = new Setting<>("StateMode", sMode.Version);
    public static Setting<String> state = new Setting<>("State", "Free?");
    public static Setting<Boolean> nickname = new Setting<>("Nickname", false);
    public static DiscordRichPresence presence = new DiscordRichPresence();
    public static boolean started;
    static String String1 = "none";
    private final Timer timer_delay = new Timer();
    private static Thread thread;
    String slov;
    String[] rpc_perebor_en = {"Parkour", "Reporting cheaters", "Touching grass", "Asks how to bind", "Reporting bugs", "Watching Kilab"};
    String[] rpc_perebor_ru = {"Паркурит", "Репортит читеров", "Трогает траву", "Спрашивает как забиндить", "Репортит баги", "Смотрит Флюгера"};
    int randomInt;

    public RPC() {
        super("DiscordRPC", Category.CLIENT);
    }

    public static void readFile() {
        try {
            File file = new File("PlasmoVoice/misc/RPC.txt");
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    while (reader.ready()) {
                        String1 = reader.readLine();
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public static void WriteFile(String url1, String url2) {
        File file = new File("PlasmoVoice/misc/RPC.txt");
        try {
            file.createNewFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(url1 + "SEPARATOR" + url2 + '\n');
            } catch (Exception ignored) {
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onDisable() {
        started = false;
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
        rpc.Discord_Shutdown();
    }

    @Override
    public void onUpdate() {
        startRpc();
    }

    public void startRpc() {
        if (isDisabled()) return;
        if (!started) {
            started = true;
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            rpc.Discord_Initialize("1341860447695147008", handlers, true, "");
            presence.startTimestamp = (System.currentTimeMillis() / 1000L);
            presence.largeImageText = "v" + PulseMod.VERSION;
            rpc.Discord_UpdatePresence(presence);

            thread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    rpc.Discord_RunCallbacks();
                    presence.details = getDetails();

                    switch (smode.getValue()) {
                        case Stats ->
                                presence.state = "Hacks: " + Managers.MODULE.getEnabledModules().size() + " / " + Managers.MODULE.modules.size();
                        case Custom -> presence.state = state.getValue();
                        case Version -> presence.state = "v" + PulseMod.VERSION +" for mc 1.21.1";
                    }

                    if (nickname.getValue()) {
                        presence.smallImageText = "logged as - " + mc.getSession().getUsername();
                        presence.smallImageKey = "https://minotar.net/helm/" + mc.getSession().getUsername() + "/100.png";
                    } else {
                        presence.smallImageText = "";
                        presence.smallImageKey = "";
                    }

                    presence.button_label_1 = "Download";
                    presence.button_url_1 = "https://github.com/StalArea/PulseMod/";

                    switch (mode.getValue()) {
                        case Recode -> presence.largeImageKey = "https://i.imgur.com/GGJ1yNA.gif";
                        case MegaCute ->
                                presence.largeImageKey = "https://media1.tenor.com/images/6bcbfcc0be97d029613b54f97845bc59/tenor.gif?itemid=26823781";
                        case Custom -> {
                            readFile();
                            presence.largeImageKey = String1.split("SEPARATOR")[0];
                            if (!Objects.equals(String1.split("SEPARATOR")[1], "none")) {
                                presence.smallImageKey = String1.split("SEPARATOR")[1];
                            }
                        }
                    }
                    rpc.Discord_UpdatePresence(presence);
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "PM-RPC-Handler");
            thread.start();
        }
    }

    private String getDetails() {
        String result = "";

        if (mc.currentScreen instanceof MultiplayerScreen || mc.currentScreen instanceof AddServerScreen || mc.currentScreen instanceof TitleScreen) {
            if(timer_delay.passedMs(60 * 1000)){
                randomInt = (int)(Math.random() * (5 - 0 + 1) + 0);
                slov = isRu() ? rpc_perebor_ru[randomInt] : rpc_perebor_en[randomInt];
                timer_delay.reset();
            }
            result = slov;
        } else if (mc.getCurrentServerEntry() != null) {
            result = isRu() ? (showIP.getValue() ? "Играет на " + mc.getCurrentServerEntry().address : "Играет на сервере") : (showIP.getValue() ? "Playing on " + mc.getCurrentServerEntry().address : "Playing on server");
        } else if (mc.isInSingleplayer()) {
            result = isRu() ? "Читерит в одиночке" : "SinglePlayer hacker";
        }
        return result;
    }

    public enum Mode {Custom, MegaCute, Recode}

    public enum sMode {Custom, Stats, Version}
}
