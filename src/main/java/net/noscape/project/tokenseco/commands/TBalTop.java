package net.noscape.project.tokenseco.commands;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.utils.*;
import net.noscape.project.tokenseco.utils.menu.menus.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class TBalTop implements CommandExecutor {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);
    private final ConfigManager config = TokensEconomy.getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("tbaltop")) {
                if (player.hasPermission("te.baltop") || player.hasPermission("te.player")) {

                    new TokenTop(TokensEconomy.getMenuUtil(player)).open();

                    //player.sendMessage(Utils.applyFormat("&eTop 10:"));

                    //for (String top : UserData.getTop10().keySet()){
                    //player.sendMessage(Utils.applyFormat("&7" + top + " &e⛃&f" + UserData.getTop10().get(top).longValue()));

                    //for (int i1 = 0; i1 < 10; i1++) {
                    //String currentPlayer = top10Players[i1];
                    //int currentNumber = i1 + 1;

                    //if (UserData.getTop10().get(currentPlayer) != null) {
                    //player.sendMessage(Utils.applyFormat("&f" + currentNumber + ". &7" + top +
                    //" &e⛃&f" + UserData.getTop10().get(top).longValue()));
                    //} else {
                    //player.sendMessage(Utils.applyFormat("&f" + currentNumber + ". &7N/A" + " &e⛃&f0"));
                    //}
                    //}
                } else {
                    player.sendMessage(ChatColor.RED + "Permission Required:" + ChatColor.GRAY + " te.baltop or te.player");
                }

                // player.sendMessage(Utils.applyFormat(UserData.getTop10().get(top).hashCode() + ". &7" +
                // top + " &e⛃&f" + UserData.getTop10().get(top).longValue()));
            } else if (cmd.getName().equalsIgnoreCase("baltop")) {
                if (player.hasPermission("te.baltop") || player.hasPermission("te.player")) {
                    new TokenTop(TokensEconomy.getMenuUtil(player)).open();
                } else {
                    player.sendMessage(ChatColor.RED + "Permission Required:" + ChatColor.GRAY + " te.baltop or te.player");
                }
            }
        }
        return false;
    }

}