package ru.openpulse.mod.utility.discord;

import java.util.Arrays;
import java.util.List;
import ru.openpulse.mod.utility.discord.callbacks.JoinGameCallback;
import ru.openpulse.mod.utility.discord.callbacks.ErroredCallback;
import ru.openpulse.mod.utility.discord.callbacks.ReadyCallback;
import ru.openpulse.mod.utility.discord.callbacks.SpectateGameCallback;
import ru.openpulse.mod.utility.discord.callbacks.JoinRequestCallback;
import ru.openpulse.mod.utility.discord.callbacks.DisconnectedCallback;
import com.sun.jna.Structure;

public class DiscordEventHandlers extends Structure {
    public DisconnectedCallback disconnected;
    public JoinRequestCallback joinRequest;
    public SpectateGameCallback spectateGame;
    public ReadyCallback ready;
    public ErroredCallback errored;
    public JoinGameCallback joinGame;
    
    protected List<String> getFieldOrder() {
        return Arrays.asList("ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest");
    }
}