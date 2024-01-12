package net.noscape.project.tokenseco.listeners;

import net.noscape.project.tokenseco.TokensEconomy;
import net.noscape.project.tokenseco.managers.TokenManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementNewEvent implements Listener {

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
}
