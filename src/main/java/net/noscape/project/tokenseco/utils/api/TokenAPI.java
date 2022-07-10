package net.noscape.project.tokenseco.utils.api;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import org.bukkit.*;

public class TokenAPI {
    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);

    public int getTokensInt(OfflinePlayer player) {
        return UserData.getTokensInt(player.getUniqueId());
    }

    public double getTokensDouble(OfflinePlayer player) {
        return UserData.getTokensDouble(player.getUniqueId());
    }

    public void setTokens(OfflinePlayer player, int amount) {
        UserData.setTokens(player.getUniqueId(), amount);
    }

    public void setTokens(OfflinePlayer player, double amount) {
        UserData.setTokens(player.getUniqueId(), amount);
    }

    public void addTokens(OfflinePlayer player, int amount) {
        UserData.addTokens(player.getUniqueId(), amount);
    }

    public void addTokens(OfflinePlayer player, double amount) {
        UserData.addTokens(player.getUniqueId(), amount);
    }

    public void removeTokens(OfflinePlayer player, int amount) {
        UserData.removeTokens(player.getUniqueId(), amount);
    }

    public void removeTokens(OfflinePlayer player, double amount) {
        UserData.removeTokens(player.getUniqueId(), amount);
    }

    public void resetTokens(OfflinePlayer player) {
        UserData.setTokens(player.getUniqueId(), TokensEconomy.getConfigManager().getDefaultTokens());
    }
}