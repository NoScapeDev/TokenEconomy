package net.noscape.project.tokenseco.commands;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class TStats implements CommandExecutor {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("tstats")) {
                if (player.hasPermission("te.stats") || player.hasPermission("te.player")) {
                    if (te.isMySQL()) {
                        player.sendMessage(Utils.applyFormat("&e&lTOKEN STATS &8&l>"));
                        player.sendMessage(Utils.applyFormat("&7Server Total: &e" + UserData.getServerTotalTokens()));
                        player.sendMessage(Utils.applyFormat("&7Your Balance: &e" + UserData.getTokensDouble(player.getUniqueId())));
                    } else if (te.isH2()) {
                        player.sendMessage(Utils.applyFormat("&e&lTOKEN STATS &8&l>"));
                        player.sendMessage(Utils.applyFormat("&7Server Total: &e" + H2UserData.getServerTotalTokens()));
                        player.sendMessage(Utils.applyFormat("&7Your Balance: &e" + H2UserData.getTokensDouble(player.getUniqueId())));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Permission Required:" + ChatColor.GRAY + " te.stats or te.player");
                }
            }
        }
        return false;
    }

}