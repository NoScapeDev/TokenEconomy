package net.noscape.project.tokenseco.utils.api.events;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class PlayerReceiveTokensEvent extends Event {

    private final Player player;
    private int tokens;

    public PlayerReceiveTokensEvent(Player player, int tokens) {
        this.player = player;
        this.tokens = tokens;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return null;
    }

    public void setTokens(int i) {
        this.tokens = i;
    }

    public int getTokens() {
        return tokens;
    }

    public Player getPlayer() {
        return player;
    }
}
