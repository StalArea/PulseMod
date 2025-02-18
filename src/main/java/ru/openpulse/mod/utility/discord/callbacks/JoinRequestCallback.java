package ru.openpulse.mod.utility.discord.callbacks;

import ru.openpulse.mod.utility.discord.DiscordUser;
import com.sun.jna.Callback;

public interface JoinRequestCallback extends Callback {
    void apply(final DiscordUser p0);
}
