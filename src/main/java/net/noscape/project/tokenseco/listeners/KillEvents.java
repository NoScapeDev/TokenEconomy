package net.noscape.project.tokenseco.listeners;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.managers.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

import java.util.*;

public class KillEvents implements Listener {

    @EventHandler
    public void onKill(EntityDeathEvent e) {

        if (e.getEntity() instanceof Player && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            assert killer != null;
            TokenManager man = TokensEconomy.getTokenManager(killer);

            if (TokensEconomy.getConfigManager().isInDisabledWorld(killer)) {
                if (TokensEconomy.getConfigManager().getValueEnabled("kill-players")) {
                    if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                        int tokens = TokensEconomy.getConfigManager().getValue("kill-players");

                        man.addTokens(tokens);

                        if (TokensEconomy.getConfigManager().isEventMessage()) {
                            killer.sendMessage(TokensEconomy.getConfigManager().getEventMessage("PLAYER-KILL", "&a+" + tokens).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()));
                        }
                    }
                }
            }
        } else if (e.getEntity() instanceof Mob && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            assert killer != null;

            TokenManager man = TokensEconomy.getTokenManager(killer);
            if (!TokensEconomy.getConfigManager().isInDisabledWorld(killer)) {
                for (String entity : Objects.requireNonNull(TokensEconomy.getConfigManager().getConfig().getConfigurationSection("t.player.events.kill-entities")).getKeys(false)) {
                    if (entity.contains(e.getEntity().getName().toUpperCase())) {

                        if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                            int tokens = TokensEconomy.getConfigManager().getConfig().getInt("t.player.events.kill-entities." + entity.toUpperCase() + ".value");

                            man.addTokens(tokens);

                            if (TokensEconomy.getConfigManager().getConfig().getBoolean("t.player.events.enable-messages")) {
                                killer.sendMessage(TokensEconomy.getConfigManager().getEventMessage("ENTITY-KILL", "&a+" + tokens).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())
                                        .replaceAll("%entity%", e.getEntity().getName()));
                            }
                        }
                    }
                }
            }
        } else if (e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();

            String str = TokensEconomy.getConfigManager().getConfig().getString("t.player.events.player-death.value");
            TokenManager man = TokensEconomy.getTokenManager(victim);
            if (TokensEconomy.getConfigManager().isInDisabledWorld(victim)) {
                assert str != null;
                if (str.startsWith("-")) {
                    str = str.replaceAll("-", "");

                    int value = Integer.parseInt(str);

                    man.removeTokens(value);

                    if (TokensEconomy.getConfigManager().isEventMessage()) {
                        victim.sendMessage(TokensEconomy.getConfigManager().getEventMessage("PLAYER-DEATH", "&c-" + value).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()));
                    }
                } else {
                    int value = Integer.parseInt(str);

                    if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                        man.addTokens(value);

                        if (TokensEconomy.getConfigManager().isEventMessage()) {
                            victim.sendMessage(TokensEconomy.getConfigManager().getEventMessage("PLAYER-DEATH", "&a+" + value).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()));
                        }
                    }
                }
            }
        }
    }
}
