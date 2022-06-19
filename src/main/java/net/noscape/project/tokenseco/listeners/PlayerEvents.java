package net.noscape.project.tokenseco.listeners;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.utils.*;
import net.noscape.project.tokenseco.utils.api.events.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;

public class PlayerEvents implements Listener {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        if (te.isMySQL()) {
            TokensEconomy.getUser().createPlayer(e.getPlayer());
            TokensEconomy.getTokenManager(e.getPlayer());
        } else if (te.isH2()) {
            TokensEconomy.getH2user().createPlayer(e.getPlayer());
            TokensEconomy.getTokenManager(e.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (TokensEconomy.getTokenMap().containsKey(player)) {
            TokenManager man = TokensEconomy.getTokenManager(player);
            if (te.isMySQL()) {
                UserData.setTokens(player.getUniqueId(), man.getTokens());
            } else if (te.isH2()) {
                H2UserData.setTokens(player.getUniqueId(), man.getTokens());
            }

            TokensEconomy.getTokenMap().remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            assert killer != null;
            if (!TokensEconomy.getConfigManager().isInDisabledWorld(killer)) {
                if (TokensEconomy.getConfigManager().getValueEnabled("kill-players")) {
                    if (te.isMySQL()) {
                        if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(killer)) {
                            int tokens = TokensEconomy.getConfigManager().getValue("kill-players");

                            PlayerReceiveTokensEvent event = new PlayerReceiveTokensEvent(killer, tokens);
                            Bukkit.getPluginManager().callEvent(event);

                            TokenManager man = TokensEconomy.getTokenManager(killer);
                            man.addTokens(tokens);

                            killer.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens for killing a player!"));
                        }
                    } else {
                        if (te.isH2()) {
                            if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(killer)) {
                                int tokens = TokensEconomy.getConfigManager().getValue("kill-players");

                                PlayerReceiveTokensEvent event = new PlayerReceiveTokensEvent(killer, tokens);
                                Bukkit.getPluginManager().callEvent(event);

                                TokenManager man = TokensEconomy.getTokenManager(killer);
                                man.addTokens(tokens);
                                killer.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens for killing a player!"));
                            }
                        }
                    }
                }
            }
        } else if (e.getEntity() instanceof Monster && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            assert killer != null;
            if (!TokensEconomy.getConfigManager().isInDisabledWorld(killer)) {
                if (TokensEconomy.getConfigManager().getValueEnabled("kill-mobs")) {
                    if (te.isMySQL()) {
                        if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(killer)) {
                            int tokens = TokensEconomy.getConfigManager().getValue("kill-mobs");

                            PlayerReceiveTokensEvent event = new PlayerReceiveTokensEvent(killer, tokens);
                            Bukkit.getPluginManager().callEvent(event);

                            TokenManager man = TokensEconomy.getTokenManager(killer);
                            man.addTokens(tokens);
                            killer.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens for killing a mob!"));
                        }
                    } else if (te.isMySQL()) {
                        if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(killer)) {
                            int tokens = TokensEconomy.getConfigManager().getValue("kill-mobs");

                            PlayerReceiveTokensEvent event = new PlayerReceiveTokensEvent(killer, tokens);
                            Bukkit.getPluginManager().callEvent(event);

                            TokenManager man = TokensEconomy.getTokenManager(killer);
                            man.addTokens(tokens);
                            killer.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens for killing a mob!"));
                        }
                    }
                }
            }
        } else if (e.getEntity() instanceof Animals && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            assert killer != null;

            if (!TokensEconomy.getConfigManager().isInDisabledWorld(killer)) {
                if (TokensEconomy.getConfigManager().getValueEnabled("kill-animals")) {
                    if (te.isMySQL()) {
                        if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(killer)) {
                            int tokens = TokensEconomy.getConfigManager().getValue("kill-animals");

                            PlayerReceiveTokensEvent event = new PlayerReceiveTokensEvent(killer, tokens);
                            Bukkit.getPluginManager().callEvent(event);

                            TokenManager man = TokensEconomy.getTokenManager(killer);
                            man.addTokens(tokens);
                            killer.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens for killing animals!"));
                        }
                    } else if (te.isH2()) {
                        if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(killer)) {
                            int tokens = TokensEconomy.getConfigManager().getValue("kill-animals");

                            PlayerReceiveTokensEvent event = new PlayerReceiveTokensEvent(killer, tokens);
                            Bukkit.getPluginManager().callEvent(event);

                            TokenManager man = TokensEconomy.getTokenManager(killer);
                            man.addTokens(tokens);
                            killer.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens for killing animals!"));
                        }
                    }
                }
            }
        } else if (e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();

            String str = TokensEconomy.getConfigManager().getConfig().getString("t.player.events.player-death.value");
            if (!TokensEconomy.getConfigManager().isInDisabledWorld(victim)) {
                assert str != null;
                if (str.startsWith("-")) {
                    str = str.replaceAll("-", "");

                    int value = Integer.parseInt(str);

                    TokenManager man = TokensEconomy.getTokenManager(victim);
                    man.removeTokens(value);
                    victim.sendMessage(Utils.applyFormat("&c-" + value + " &7Tokens for dying!"));
                } else {
                    int value = Integer.parseInt(str);

                    if (te.isMySQL()) {
                        if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(victim)) {
                            PlayerReceiveTokensEvent event = new PlayerReceiveTokensEvent(victim, value);
                            Bukkit.getPluginManager().callEvent(event);

                            TokenManager man = TokensEconomy.getTokenManager(victim);
                            man.addTokens(value);
                            victim.sendMessage(Utils.applyFormat("&e+" + value + " &7Tokens for dying!"));
                        }
                    } else if (te.isH2()) {
                        if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(victim)) {
                            PlayerReceiveTokensEvent event = new PlayerReceiveTokensEvent(victim, value);
                            Bukkit.getPluginManager().callEvent(event);

                            TokenManager man = TokensEconomy.getTokenManager(victim);
                            man.addTokens(value);
                            victim.sendMessage(Utils.applyFormat("&e+" + value + " &7Tokens for dying!"));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Player player = (Player) e.getInventory().getHolder();
        if (!TokensEconomy.getConfigManager().isInDisabledWorld(player)) {
            assert player != null;
            if (TokensEconomy.getConfigManager().getValueEnabled("crafting")) {
                if (te.isMySQL()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(player)) {
                        int tokens = TokensEconomy.getConfigManager().getValue("crafting");

                        PlayerReceiveTokensEvent event = new PlayerReceiveTokensEvent(player, tokens);
                        Bukkit.getPluginManager().callEvent(event);

                        TokenManager man = TokensEconomy.getTokenManager(player);
                        man.addTokens(tokens);
                        player.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens!"));
                    }
                } else if (te.isH2()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(player)) {
                        int tokens = TokensEconomy.getConfigManager().getValue("kill-animals");

                        PlayerReceiveTokensEvent event = new PlayerReceiveTokensEvent(player, tokens);
                        Bukkit.getPluginManager().callEvent(event);

                        TokenManager man = TokensEconomy.getTokenManager(player);
                        man.addTokens(tokens);
                        player.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens!"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAdvance(PlayerAdvancementDoneEvent e) {
        Player player = e.getPlayer();

        if (!TokensEconomy.getConfigManager().isInDisabledWorld(player)) {
            if (TokensEconomy.getConfigManager().getValueEnabled("advancement-complete")) {
                if (te.isMySQL()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(player)) {
                        int tokens = TokensEconomy.getConfigManager().getValue("advancement-complete");

                        PlayerReceiveTokensEvent event = new PlayerReceiveTokensEvent(player, tokens);
                        Bukkit.getPluginManager().callEvent(event);

                        TokenManager man = TokensEconomy.getTokenManager(player);
                        man.addTokens(tokens);
                        player.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens!"));
                    }
                } else if (te.isH2()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(player)) {
                        int tokens = TokensEconomy.getConfigManager().getValue("advancement-complete");

                        PlayerReceiveTokensEvent event = new PlayerReceiveTokensEvent(player, tokens);
                        Bukkit.getPluginManager().callEvent(event);

                        TokenManager man = TokensEconomy.getTokenManager(player);
                        man.addTokens(tokens);
                        player.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens!"));
                    }
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

                TokenManager man = TokensEconomy.getTokenManager(player);
                man.removeTokens(value);
                player.sendMessage(Utils.applyFormat("&c-" + value + " &7Tokens for using the nether portal!"));
            } else {
                int value = Integer.parseInt(str);
                if (te.isMySQL()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(player)) {
                        TokenManager man = TokensEconomy.getTokenManager(player);
                        man.addTokens(value);
                        player.sendMessage(Utils.applyFormat("&e+" + value + " &7Tokens for using the nether portal!"));
                    }
                } else if (te.isH2()) {
                    if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(player)) {
                        TokenManager man = TokensEconomy.getTokenManager(player);
                        man.addTokens(value);
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

                    TokenManager man = TokensEconomy.getTokenManager(player);
                    man.removeTokens(value);
                    player.sendMessage(Utils.applyFormat("&c-" + value + " &7Tokens for using the nether portal!"));
                } else {
                    int value = Integer.parseInt(str);
                    if (te.isMySQL()) {
                        if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(player)) {
                            TokenManager man = TokensEconomy.getTokenManager(player);
                            man.addTokens(value);
                            player.sendMessage(Utils.applyFormat("&e+" + value + " &7Tokens for using the nether portal!"));
                        }
                    } else if (te.isH2()) {
                        if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(player)) {
                            TokenManager man = TokensEconomy.getTokenManager(player);
                            man.addTokens(value);
                            player.sendMessage(Utils.applyFormat("&e+" + value + " &7Tokens for using the nether portal!"));
                        }
                    }
                }
            }
        }
    }
}