package net.noscape.project.tokenseco.utils;

import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

public class TokenManager {

    private OfflinePlayer player;
    private int tokens;

    public TokenManager(OfflinePlayer player, int tokens) {
        this.player = player;
        this.tokens = tokens;
    }

    public void setPlayer(OfflinePlayer player) { this.player = player; }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public void setTokens(int i) {
        this.tokens = i;
    }

    public void addTokens(int tokens) {
        int i = getTokens() + tokens;

        setTokens(i);
    }

    public void removeTokens(int tokens) {
        int i = getTokens() - tokens;

        setTokens(i);
    }

    public int getTokens() {
        return tokens;
    }
}