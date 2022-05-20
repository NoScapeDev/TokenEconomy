package net.noscape.project.tokenseco.listeners;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.utils.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;

public class PlayerEvents implements Listener {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);
    private final ConfigManager config = TokensEconomy.getConfigManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        if (te.isMySQL()) {
            TokensEconomy.getUser().createPlayer(e.getPlayer());
        } else if (te.isH2()) {
            TokensEconomy.getH2user().createPlayer(e.getPlayer());
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            assert killer != null;

            if (TokensEconomy.getConfigManager().getValueEnabled("kill-players")) {
                if (te.isMySQL()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(killer)) {
                        UserData.addTokens(killer.getUniqueId(), TokensEconomy.getConfigManager().getValue("kill-players"));
                        killer.sendMessage(Utils.applyFormat("&e+" + TokensEconomy.getConfigManager().getValue("kill-players") + " &7Tokens for killing a player!"));
                    }
                } else {
                    if (te.isH2()) {
                        if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(killer)) {
                            H2UserData.addTokens(killer.getUniqueId(), TokensEconomy.getConfigManager().getValue("kill-players"));
                            killer.sendMessage(Utils.applyFormat("&e+" + TokensEconomy.getConfigManager().getValue("kill-players") + " &7Tokens for killing a player!"));
                        }
                    }
                }
            }
        } else if (e.getEntity() instanceof Monster && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            assert killer != null;

            if (TokensEconomy.getConfigManager().getValueEnabled("kill-mobs")) {
                if (te.isMySQL()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(killer)) {
                        UserData.addTokens(killer.getUniqueId(), TokensEconomy.getConfigManager().getValue("kill-mobs"));
                        killer.sendMessage(Utils.applyFormat("&e+" + TokensEconomy.getConfigManager().getValue("kill-mobs") + " &7Tokens for killing a mob!"));
                    }
                } else if (te.isMySQL()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(killer)) {
                        H2UserData.addTokens(killer.getUniqueId(), TokensEconomy.getConfigManager().getValue("kill-mobs"));
                        killer.sendMessage(Utils.applyFormat("&e+" + TokensEconomy.getConfigManager().getValue("kill-mobs") + " &7Tokens for killing a mob!"));
                    }
                }
            }
        } else if (e.getEntity() instanceof Animals && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            assert killer != null;

            if (TokensEconomy.getConfigManager().getValueEnabled("kill-animals")) {
                if (te.isMySQL()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(killer)) {
                        UserData.addTokens(killer.getUniqueId(), TokensEconomy.getConfigManager().getValue("kill-animals"));
                        killer.sendMessage(Utils.applyFormat("&e+" + TokensEconomy.getConfigManager().getValue("kill-animals") + " &7Tokens for killing animals!"));
                    }
                } else if (te.isH2()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(killer)) {
                        H2UserData.addTokens(killer.getUniqueId(), TokensEconomy.getConfigManager().getValue("kill-animals"));
                        killer.sendMessage(Utils.applyFormat("&e+" + TokensEconomy.getConfigManager().getValue("kill-animals") + " &7Tokens for killing animals!"));
                    }
                }
            }
        } else if (e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();

            String str = TokensEconomy.getConfigManager().getConfig().getString("t.player.events.player-death.value");

            assert str != null;
            if (str.startsWith("-")) {
                str = str.replaceAll("-", "");

                int value = Integer.parseInt(str);

                if (te.isMySQL()) {
                    UserData.removeTokens(victim.getUniqueId(), value);
                } else if (te.isH2()) {
                    H2UserData.removeTokens(victim.getUniqueId(), value);
                }
                victim.sendMessage(Utils.applyFormat("&c-" + value + " &7Tokens for dying!"));
            } else {
                int value = Integer.parseInt(str);

                if (te.isH2()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(victim)) {
                        UserData.addTokens(victim.getUniqueId(), value);
                        victim.sendMessage(Utils.applyFormat("&e+" + value + " &7Tokens for dying!"));
                    }
                } else if (te.isH2()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(victim)) {
                        H2UserData.addTokens(victim.getUniqueId(), value);
                        victim.sendMessage(Utils.applyFormat("&e+" + value + " &7Tokens for dying!"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Player player = (Player) e.getInventory().getHolder();

        if (TokensEconomy.getConfigManager().getValueEnabled("crafting")) {
            assert player != null;
            if (te.isMySQL()) {
                if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(player)) {
                    UserData.addTokens(player.getUniqueId(), TokensEconomy.getConfigManager().getValue("crafting"));
                    player.sendMessage(Utils.applyFormat("&e+" + TokensEconomy.getConfigManager().getValue("crafting") + " &7Tokens!"));
                }
            } else if (te.isH2()) {
                if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(player)) {
                    H2UserData.addTokens(player.getUniqueId(), TokensEconomy.getConfigManager().getValue("crafting"));
                    player.sendMessage(Utils.applyFormat("&e+" + TokensEconomy.getConfigManager().getValue("crafting") + " &7Tokens!"));
                }
            }
        }
    }

    @EventHandler
    public void onAdvance(PlayerAdvancementDoneEvent e) {
        Player player = e.getPlayer();

        if (TokensEconomy.getConfigManager().getValueEnabled("advancement-complete")) {
            if (te.isMySQL()) {
                if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(player)) {
                    UserData.addTokens(player.getUniqueId(), TokensEconomy.getConfigManager().getValue("advancement-complete"));
                    player.sendMessage(Utils.applyFormat("&e+" + TokensEconomy.getConfigManager().getValue("advancement-complete") + " &7Tokens!"));
                }
            } else if (te.isH2()) {
                if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(player)) {
                    H2UserData.addTokens(player.getUniqueId(), TokensEconomy.getConfigManager().getValue("advancement-complete"));
                    player.sendMessage(Utils.applyFormat("&e+" + TokensEconomy.getConfigManager().getValue("advancement-complete") + " &7Tokens!"));
                }
            }
        }
    }

    @EventHandler
    public void onNetherEnter(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {

            String str = TokensEconomy.getConfigManager().getConfig().getString("t.player.events.nether-portal.value");

            assert str != null;
            if (str.startsWith("-")) {
                str = str.replaceAll("-", "");

                int value = Integer.parseInt(str);

                if (te.isMySQL()) {
                    UserData.removeTokens(player.getUniqueId(), value);
                } else if (te.isH2()) {
                    H2UserData.removeTokens(player.getUniqueId(), value);
                }
                player.sendMessage(Utils.applyFormat("&c-" + value + " &7Tokens for using the nether portal!"));
            } else {
                int value = Integer.parseInt(str);
                if (te.isMySQL()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(player)) {
                        UserData.addTokens(player.getUniqueId(), value);
                        player.sendMessage(Utils.applyFormat("&e+" + value + " &7Tokens for using the nether portal!"));
                    }
                } else if (te.isH2()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(player)) {
                        H2UserData.addTokens(player.getUniqueId(), value);
                        player.sendMessage(Utils.applyFormat("&e+" + value + " &7Tokens for using the nether portal!"));
                    }
                }
            }
        } else {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {

                String str = TokensEconomy.getConfigManager().getConfig().getString("t.player.events.end-portal.value");

                assert str != null;
                if (str.startsWith("-")) {
                    str = str.replaceAll("-", "");

                    int value = Integer.parseInt(str);

                    if (te.isMySQL()) {
                        UserData.removeTokens(player.getUniqueId(), value);
                    } else if (te.isH2()) {
                        H2UserData.removeTokens(player.getUniqueId(), value);
                    }
                    player.sendMessage(Utils.applyFormat("&c-" + value + " &7Tokens for using the nether portal!"));
                } else {
                    int value = Integer.parseInt(str);
                    if (te.isMySQL()) {
                        if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(player)) {
                            UserData.addTokens(player.getUniqueId(), value);
                            player.sendMessage(Utils.applyFormat("&e+" + value + " &7Tokens for using the nether portal!"));
                        }
                    } else if (te.isH2()) {
                        if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(player)) {
                            H2UserData.addTokens(player.getUniqueId(), value);
                            player.sendMessage(Utils.applyFormat("&e+" + value + " &7Tokens for using the nether portal!"));
                        }
                    }
                }
            }
        }
    }

}