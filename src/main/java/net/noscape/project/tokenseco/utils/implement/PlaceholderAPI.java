package net.noscape.project.tokenseco.utils.implement;

import me.clip.placeholderapi.expansion.*;
import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final TokensEconomy plugin;

    public PlaceholderAPI(TokensEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getAuthor() {
        return "noscape";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "tokens";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("player_tokens")){
            String text = null;

            if (plugin.isMySQL()) {
                text = String.valueOf(UserData.getTokensDouble(player.getUniqueId()));
            } else if (plugin.isH2()) {
                text = String.valueOf(H2UserData.getTokensDouble(player.getUniqueId()));
            }

            return text;
        }

        return null;
    }

}
