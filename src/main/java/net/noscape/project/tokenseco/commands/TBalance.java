package net.noscape.project.tokenseco.commands;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.managers.*;
import net.noscape.project.tokenseco.utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class TBalance implements CommandExecutor {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);
    private final ConfigManager config = TokensEconomy.getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {

            Player player = (Player) sender;

            // if (player.hasPermission("te.balance") || player.hasPermission("te.player")) {

            TokenManager tokens = TokensEconomy.getTokenManager(player);

            if (cmd.getName().equalsIgnoreCase("tbalance")) {
                // /tbalance - giving the player their balance
                if (args.length == 1) {
                    if (player.hasPermission("te.balance.other")) {
                        Player target = Bukkit.getPlayer(args[0]);

                        if (target != null) {
                            TokenManager tokensTarget = TokensEconomy.getTokenManager(player);
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    TokensEconomy.getConfigManager().getMessages().getString("m.BALANCE")).replaceAll("%tokens%",
                                    String.valueOf(tokensTarget.getTokens()).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()))));
                        }
                    } else {
                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));

                    }
                } else {
                    if (player.hasPermission("te.balance") || player.hasPermission("te.player")) {
                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                TokensEconomy.getConfigManager().getMessages().getString("m.BALANCE")).replaceAll("%tokens%",
                                String.valueOf(tokens.getTokens()).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()))));
                    } else {
                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));

                    }
                }
            } else if (cmd.getName().equalsIgnoreCase("balance")) {
                // /tbalance - giving the player their balance
                if (args.length == 1) {
                    if (player.hasPermission("te.balance.other")) {
                        Player target = Bukkit.getPlayer(args[0]);

                        if (target != null) {
                            TokenManager tokensTarget = TokensEconomy.getTokenManager(player);
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    TokensEconomy.getConfigManager().getMessages().getString("m.BALANCE")).replaceAll("%tokens%",
                                    String.valueOf(tokensTarget.getTokens()).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()))));
                        }
                    } else {
                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                    }
                } else {
                    if (player.hasPermission("te.balance") || player.hasPermission("te.player")) {
                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                TokensEconomy.getConfigManager().getMessages().getString("m.BALANCE")).replaceAll("%tokens%",
                                String.valueOf(tokens.getTokens()).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()))));
                    } else {
                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                    }
                }
            }
        }
        return false;
    }

}