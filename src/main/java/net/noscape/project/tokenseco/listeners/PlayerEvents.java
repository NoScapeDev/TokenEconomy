package net.noscape.project.tokenseco.listeners;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.managers.*;
import net.noscape.project.tokenseco.utils.*;
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
            TokensEconomy.getBankManager(e.getPlayer());
        } else if (te.isH2()) {
            TokensEconomy.getH2user().createPlayer(e.getPlayer());
            TokensEconomy.getTokenManager(e.getPlayer());
            TokensEconomy.getBankManager(e.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (TokensEconomy.getTokenMap().containsKey(player)) {
            TokenManager tokens = TokensEconomy.getTokenManager(player);
            BankManager bank = TokensEconomy.getBankManager(player);

            if (te.isMySQL()) {
                UserData.setTokens(player.getUniqueId(), tokens.getTokens());
                UserData.setBank(player.getUniqueId(), bank.getBank());
            } else if (te.isH2()) {
                H2UserData.setTokens(player.getUniqueId(), tokens.getTokens());
                H2UserData.setBank(player.getUniqueId(), bank.getBank());
            }

            TokensEconomy.getTokenMap().remove(e.getPlayer());
            TokensEconomy.getBankMap().remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            assert killer != null;
            TokenManager man = TokensEconomy.getTokenManager(killer);

            if (!TokensEconomy.getConfigManager().isInDisabledWorld(killer)) {
                if (TokensEconomy.getConfigManager().getValueEnabled("kill-players")) {
                    if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                        int tokens = TokensEconomy.getConfigManager().getValue("kill-players");

                        man.addTokens(tokens);
                        killer.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens for killing a player!"));
                    }
                }
            }
        } else if (e.getEntity() instanceof Monster && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            assert killer != null;

            TokenManager man = TokensEconomy.getTokenManager(killer);
            if (!TokensEconomy.getConfigManager().isInDisabledWorld(killer)) {
                if (TokensEconomy.getConfigManager().getValueEnabled("kill-mobs")) {
                    if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                        int tokens = TokensEconomy.getConfigManager().getValue("kill-mobs");

                        man.addTokens(tokens);
                        killer.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens for killing a mob!"));
                    }
                }
            }
        } else if (e.getEntity() instanceof Animals && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            assert killer != null;

            TokenManager man = TokensEconomy.getTokenManager(killer);
            if (!TokensEconomy.getConfigManager().isInDisabledWorld(killer)) {
                if (TokensEconomy.getConfigManager().getValueEnabled("kill-animals")) {
                    if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                        int tokens = TokensEconomy.getConfigManager().getValue("kill-animals");

                        man.addTokens(tokens);
                        killer.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens for killing an animal!"));
                    }
                }
            }
        } else if (e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();

            String str = TokensEconomy.getConfigManager().getConfig().getString("t.player.events.player-death.value");
            TokenManager man = TokensEconomy.getTokenManager(victim);
            if (!TokensEconomy.getConfigManager().isInDisabledWorld(victim)) {
                assert str != null;
                if (str.startsWith("-")) {
                    str = str.replaceAll("-", "");

                    int value = Integer.parseInt(str);

                    man.removeTokens(value);
                    victim.sendMessage(Utils.applyFormat("&c-" + value + " &7Tokens for dying!"));
                } else {
                    int value = Integer.parseInt(str);

                    if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                        man.addTokens(value);
                        victim.sendMessage(Utils.applyFormat("&e+" + value + " &7Tokens for dying!"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Player player = (Player) e.getInventory().getHolder();
        TokenManager man = TokensEconomy.getTokenManager(player);
        if (!TokensEconomy.getConfigManager().isInDisabledWorld(player)) {
            assert player != null;
            if (TokensEconomy.getConfigManager().getValueEnabled("crafting")) {
                if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                    int tokens = TokensEconomy.getConfigManager().getValue("crafting");

                    man.addTokens(tokens);
                    player.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens!"));
                }
            }
        }
    }

    @EventHandler
    public void onAdvance(PlayerAdvancementDoneEvent e) {
        Player player = e.getPlayer();

        TokenManager man = TokensEconomy.getTokenManager(player);

        if (!TokensEconomy.getConfigManager().isInDisabledWorld(player)) {
            if (TokensEconomy.getConfigManager().getValueEnabled("advancement-complete")) {
                if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                    int tokens = TokensEconomy.getConfigManager().getValue("advancement-complete");

                    man.addTokens(tokens);
                    player.sendMessage(Utils.applyFormat("&e+" + tokens + " &7Tokens!"));
                }
            }
        }
    }

    @EventHandler
    public void onNetherEnter(PlayerTeleportEvent e) {
        Player player = e.getPlayer();

        TokenManager man = TokensEconomy.getTokenManager(player);

        if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            String str = TokensEconomy.getConfigManager().getConfig().getString("t.player.events.nether-portal.value");

            assert str != null;
            if (str.startsWith("-")) {
                str = str.replaceAll("-", "");

                int value = Integer.parseInt(str);

                man.removeTokens(value);
                player.sendMessage(Utils.applyFormat("&c-" + value + " &7Tokens for using the nether portal!"));
            } else {
                int value = Integer.parseInt(str);
                if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                    man.addTokens(value);
                    player.sendMessage(Utils.applyFormat("&e+" + value + " &7Tokens for using the nether portal!"));
                }
            }
        } else {

            if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {

                String str = TokensEconomy.getConfigManager().getConfig().getString("t.player.events.end-portal.value");

                assert str != null;
                if (str.startsWith("-")) {
                    str = str.replaceAll("-", "");

                    int value = Integer.parseInt(str);

                    man.removeTokens(value);
                    player.sendMessage(Utils.applyFormat("&c-" + value + " &7Tokens for using the nether portal!"));
                } else {
                    int value = Integer.parseInt(str);
                    if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                        man.addTokens(value);
                        player.sendMessage(Utils.applyFormat("&e+" + value + " &7Tokens for using the nether portal!"));
                    }
                }
            }
        }
    }
}