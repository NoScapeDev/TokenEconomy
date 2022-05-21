package net.noscape.project.tokenseco.utils.api;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import org.bukkit.*;

public class TokenAPI {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);

    public int getTokensInt(OfflinePlayer player) {
        if (te.isMySQL()) {
            return UserData.getTokensInt(player.getUniqueId());
        } else if (te.isH2()) {
            return H2UserData.getTokensInt(player.getUniqueId());
        }

        return 0;
    }

    public double getTokensDouble(OfflinePlayer player) {
        if (te.isMySQL()) {
            return UserData.getTokensDouble(player.getUniqueId());
        } else if (te.isH2()) {
            return H2UserData.getTokensDouble(player.getUniqueId());
        }

        return 0.0;
    }

    public void setTokens(OfflinePlayer player, int amount) {
        if (te.isMySQL()) {
            UserData.setTokens(player.getUniqueId(), amount);
        } else if (te.isH2()) {
            H2UserData.setTokens(player.getUniqueId(), amount);
        }
    }

    public void setTokens(OfflinePlayer player, double amount) {
        if (te.isMySQL()) {
            UserData.setTokens(player.getUniqueId(), amount);
        } else if (te.isH2()) {
            H2UserData.setTokens(player.getUniqueId(), amount);
        }
    }

    public void addTokens(OfflinePlayer player, int amount) {
        if (te.isMySQL()) {
            UserData.addTokens(player.getUniqueId(), amount);
        } else if (te.isH2()) {
            H2UserData.addTokens(player.getUniqueId(), amount);
        }
    }

    public void addTokens(OfflinePlayer player, double amount) {
        if (te.isMySQL()) {
            UserData.addTokens(player.getUniqueId(), amount);
        } else if (te.isH2()) {
            H2UserData.addTokens(player.getUniqueId(), amount);
        }
    }

    public void removeTokens(OfflinePlayer player, int amount) {
        if (te.isMySQL()) {
            UserData.removeTokens(player.getUniqueId(), amount);
        } else if (te.isH2()) {
            H2UserData.removeTokens(player.getUniqueId(), amount);
        }
    }

    public void removeTokens(OfflinePlayer player, double amount) {
        if (te.isMySQL()) {
            UserData.removeTokens(player.getUniqueId(), amount);
        } else if (te.isH2()) {
            H2UserData.removeTokens(player.getUniqueId(), amount);
        }
    }

    public void resetTokens(OfflinePlayer player) {
        if (te.isMySQL()) {
            UserData.setTokens(player.getUniqueId(), TokensEconomy.getConfigManager().getDefaultTokens());
        } else if (te.isH2()) {
            H2UserData.setTokens(player.getUniqueId(), TokensEconomy.getConfigManager().getDefaultTokens());
        }
    }

}
