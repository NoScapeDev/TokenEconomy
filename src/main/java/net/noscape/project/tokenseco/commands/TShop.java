package net.noscape.project.tokenseco.commands;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.managers.*;
import net.noscape.project.tokenseco.utils.*;
import net.noscape.project.tokenseco.utils.menu.menus.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class TShop implements CommandExecutor {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);
    private final ConfigManager config = TokensEconomy.getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("tshop")) {
                if (player.hasPermission("te.shop") || player.hasPermission("te.player")) {
                    // /tbalance - giving the player their balance
                    new TokenShop(TokensEconomy.getMenuUtil(player)).open();
                } else {
                    player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                            TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                }
            } else if (cmd.getName().equalsIgnoreCase("shop")) {
                if (player.hasPermission("te.shop") || player.hasPermission("te.player")) {
                    // /tbalance - giving the player their balance
                    new TokenShop(TokensEconomy.getMenuUtil(player)).open();
                } else {
                    player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                            TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                }
            }
        }
        return false;
    }
}