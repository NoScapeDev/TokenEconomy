package net.noscape.project.tokenseco.utils.implement;

import me.clip.placeholderapi.expansion.*;
import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.utils.*;
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
        return "te";
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
        if (params.equalsIgnoreCase("player_balance")){
            String text = null;

            TokenManager man = TokensEconomy.getTokenManager(player);
            text = String.valueOf(man.getTokens());

            return text;
        }

        if (params.equalsIgnoreCase("player_balance_formatted")){
            String text = null;

            TokenManager man = TokensEconomy.getTokenManager(player);
            text = "" + man.getTokens();

            return text;
        }

        return null;
    }

}
