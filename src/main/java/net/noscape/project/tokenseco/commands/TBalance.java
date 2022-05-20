package net.noscape.project.tokenseco.commands;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
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

            if (cmd.getName().equalsIgnoreCase("tbalance")) {
                // /tbalance - giving the player their balance
                if (args.length == 1) {
                    if (player.hasPermission("te.balance.other")) {
                        Player target = Bukkit.getPlayer(args[0]);

                        if (target != null) {
                            if (te.isMySQL()) {
                                player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                        TokensEconomy.getConfigManager().getMessages().getString("m.BALANCE")).replaceAll("%tokens%",
                                        String.valueOf(UserData.getTokensDouble(target.getUniqueId())))));
                            } else if (te.isH2()) {
                                player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                        TokensEconomy.getConfigManager().getMessages().getString("m.BALANCE")).replaceAll("%tokens%",
                                        String.valueOf(H2UserData.getTokensDouble(target.getUniqueId())))));
                            }
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Permission Required:" + ChatColor.GRAY + " te.balance.other");
                    }
                } else {
                    if (player.hasPermission("te.balance") || player.hasPermission("te.player")) {
                        if (te.isMySQL()) {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    TokensEconomy.getConfigManager().getMessages().getString("m.BALANCE")).replaceAll("%tokens%",
                                    String.valueOf(UserData.getTokensDouble(player.getUniqueId())))));
                        } else if (te.isH2()) {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    TokensEconomy.getConfigManager().getMessages().getString("m.BALANCE")).replaceAll("%tokens%",
                                    String.valueOf(H2UserData.getTokensDouble(player.getUniqueId())))));
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Permission Required:" + ChatColor.GRAY + " te.balance or te.player");
                    }
                }
            }
        }
        return false;
    }

}