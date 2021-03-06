package net.noscape.project.tokenseco.commands;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.managers.*;
import net.noscape.project.tokenseco.utils.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class TToggle implements CommandExecutor {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);
    private final ConfigManager config = TokensEconomy.getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("toggle")) {
                if (player.hasPermission("te.toggle")) {
                    if (UserData.getIgnore(player.getUniqueId())) {
                        UserData.setIgnore(player.getUniqueId(), false);
                        player.sendMessage(Utils.applyFormat("&e&lTOKENS &7Players will now be able to send you tokens!"));
                    } else {
                        UserData.setIgnore(player.getUniqueId(), true);
                        player.sendMessage(Utils.applyFormat("&e&lTOKENS &7Players will no longer be able to send you tokens!"));
                    }
                } else {
                    player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                            TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                }
            }
        }
        return false;
    }

}