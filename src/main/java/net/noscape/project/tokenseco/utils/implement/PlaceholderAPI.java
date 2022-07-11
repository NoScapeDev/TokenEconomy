package net.noscape.project.tokenseco.utils.implement;

import me.clip.placeholderapi.expansion.*;
import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.managers.*;
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
        
        if (params.equalsIgnoreCase(player.getName() + "_money")) {
            String text = null;

            if (player.isOnline()) {
                TokenManager man = TokensEconomy.getTokenManager(player);
                text = String.valueOf(man.getTokens());
            } else {
                text = String.valueOf(UserData.getTokensInt(player.getUniqueId()));
            }
            
            return text;
        }

        if (params.equalsIgnoreCase(player.getName() + "_bank")) {
            String text = null;

            if (player.isOnline()) {
                BankManager bank = TokensEconomy.getBankManager(player);
                text = String.valueOf(bank.getBank());
            } else {
                text = String.valueOf(UserData.getBankInt(player.getUniqueId()));
            }

            return text;
        }

        if (params.equalsIgnoreCase(player.getName() + "_money_formatted")) {
            String text = null;

            if (player.isOnline()) {
                TokenManager man = TokensEconomy.getTokenManager(player);
                text = TokensEconomy.getConfigManager().getTokenSymbol() + man.getTokens();
            } else {
                text = String.valueOf(UserData.getTokensInt(player.getUniqueId()));
            }

            return text;
        }

        if (params.equalsIgnoreCase(player.getName() + "_bank_formatted")) {
            String text = null;

            if (player.isOnline()) {
                BankManager bank = TokensEconomy.getBankManager(player);
                text = TokensEconomy.getConfigManager().getTokenSymbol() + bank.getBank();
            } else {
                text = String.valueOf(UserData.getTokensInt(player.getUniqueId()));
            }

            return text;
        }

        if (params.equalsIgnoreCase("player_bank")){
            String text;

            BankManager bank = TokensEconomy.getBankManager(player);
            text = String.valueOf(bank.getBank());

            return text;
        }

        if (params.equalsIgnoreCase("player_money")){
            String text;

            TokenManager man = TokensEconomy.getTokenManager(player);
            text = String.valueOf(man.getTokens());

            return text;
        }

        if (params.equalsIgnoreCase("player_money_formatted")){
            String text;

            TokenManager man = TokensEconomy.getTokenManager(player);
            text = TokensEconomy.getConfigManager().getTokenSymbol() + man.getTokens();

            return text;
        }

        return null;
    }

}
