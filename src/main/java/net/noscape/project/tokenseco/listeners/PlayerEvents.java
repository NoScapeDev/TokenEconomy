package net.noscape.project.tokenseco.listeners;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.managers.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;

public class PlayerEvents implements Listener {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        UserData.createUserAccount(player);
        TokensEconomy.getTokenManager(player);
        TokensEconomy.getBankManager(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (TokensEconomy.getTokenMap().containsKey(player)) {
            TokenManager tokens = TokensEconomy.getTokenManager(player);
            BankManager bank = TokensEconomy.getBankManager(player);

            UserData.setTokens(player.getUniqueId(), tokens.getTokens());
            UserData.setBank(player.getUniqueId(), bank.getBank());

            TokensEconomy.getTokenMap().remove(e.getPlayer());
            TokensEconomy.getBankMap().remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Player player = (Player) e.getInventory().getHolder();
        TokenManager man = TokensEconomy.getTokenManager(player);
        if (TokensEconomy.getConfigManager().isInDisabledWorld(player)) {
            assert player != null;
            if (TokensEconomy.getConfigManager().getValueEnabled("crafting")) {
                if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                    int tokens = TokensEconomy.getConfigManager().getValue("crafting");

                    man.addTokens(tokens);
                    if (TokensEconomy.getConfigManager().isEventMessage()) {
                        player.sendMessage(TokensEconomy.getConfigManager().getEventMessage("CRAFTING", "&a+" + tokens).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAdvance(PlayerAdvancementDoneEvent e) {
        Player player = e.getPlayer();

        TokenManager man = TokensEconomy.getTokenManager(player);

        if (TokensEconomy.getConfigManager().isInDisabledWorld(player)) {
            if (TokensEconomy.getConfigManager().getValueEnabled("advancement-complete")) {
                if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                    int tokens = TokensEconomy.getConfigManager().getValue("advancement-complete");

                    man.addTokens(tokens);

                    if (TokensEconomy.getConfigManager().isEventMessage()) {
                        player.sendMessage(TokensEconomy.getConfigManager().getEventMessage("ADVANCEMENT", "&a+" + tokens).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()));
                    }
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

                if (TokensEconomy.getConfigManager().isEventMessage()) {
                    player.sendMessage(TokensEconomy.getConfigManager().getEventMessage("PORTAL-NETHER", "&c-" + value).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()));
                }
            } else {
                int value = Integer.parseInt(str);
                if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                    man.addTokens(value);

                    if (TokensEconomy.getConfigManager().isEventMessage()) {
                        player.sendMessage(TokensEconomy.getConfigManager().getEventMessage("PORTAL-NETHER", "&a+" + value).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()));
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

                    man.removeTokens(value);

                    if (TokensEconomy.getConfigManager().isEventMessage()) {
                        player.sendMessage(TokensEconomy.getConfigManager().getEventMessage("PORTAL-END", "&c-" + value).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()));
                    }
                } else {
                    int value = Integer.parseInt(str);
                    if (!(man.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                        man.addTokens(value);

                        if (TokensEconomy.getConfigManager().isEventMessage()) {
                            player.sendMessage(TokensEconomy.getConfigManager().getEventMessage("PORTAL-END", "&a+" + value).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()));
                        }
                    }
                }
            }
        }
    }
}