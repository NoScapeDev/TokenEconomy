package net.noscape.project.tokenseco.utils;

import net.noscape.project.tokenseco.data.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;

import java.util.*;

public class ConfigManager {

    private final FileConfiguration config;
    private final FileConfiguration messages;
    private final FileConfiguration tokenshop;
    private final FileConfiguration tokentop;

    public ConfigManager(FileConfiguration config, FileConfiguration messages, FileConfiguration tokenshop, FileConfiguration tokentop) {
        this.config = config;
        this.messages = messages;
        this.tokenshop = tokenshop;
        this.tokentop = tokentop;
    }

    public String getTokenSymbol() {
        return Utils.applyFormat(getMessages().getString("m.SYMBOL"));
    }

    public String getBalanceSQL(Player player) {
        return Utils.applyFormat(Objects.requireNonNull(this.getMessages().getString("m.BALANCE")).replaceAll("%tokens%", String.valueOf(UserData.getTokensDouble(player.getUniqueId()))));
    }

    public String getBalanceH2(Player player) {
        return Utils.applyFormat(Objects.requireNonNull(this.getMessages().getString("m.BALANCE")).replaceAll("%tokens%", String.valueOf(H2UserData.getTokensDouble(player.getUniqueId()))));
    }

    public String getPay() {
        return Utils.applyFormat(getMessages().getString("m.PAY"));
    }

    public String getReload() {
        return Utils.applyFormat(getMessages().getString("m.RELOADED"));
    }

    public int getDefaultTokens() {
        return getConfig().getInt("t.player.starting-balance");
    }

    public int getValue(String str) {
        return getConfig().getInt("t.player.events." + str + ".value");
    }

    public boolean isConfirmPay() {
        return getConfig().getBoolean("t.player.confirm-pay");
    }

    public boolean getValueEnabled(String str) {
            return getConfig().getInt("t.player.events." + str + ".value") != 0;
    }

    public String getTitleShop() {
        return getTokenshop().getString("gui.title");
    }

    public String getTitleTop() {
        return getTokenTop().getString("gui.title");
    }

    public int getSlotsShop() {
        return getTokenshop().getInt("gui.slots");
    }

    public int getSlotsTop() {
        return getTokenTop().getInt("gui.slots");
    }

    public boolean hasMaxBalanceSQL(Player player) {
        return UserData.getTokensInt(player.getUniqueId()) >= getConfig().getInt("t.player.max-balance");
    }

    public boolean hasMaxBalanceH2(Player player) {
        return H2UserData.getTokensInt(player.getUniqueId()) >= getConfig().getInt("t.player.max-balance");
    }

    public boolean isCommandCosts() {
        return getConfig().getBoolean("t.plugin.command-cost");
    }

    public boolean isTokenPay() {
        return getConfig().getBoolean("t.plugin.token-pay");
    }

    public HashMap<String, Integer> getCommandsCosts() {
        HashMap<String, Integer> commands = new HashMap<>();

        for (String cmds : Objects.requireNonNull(getConfig().getConfigurationSection("t.player.command-costs")).getKeys(false)) {
            int value = getConfig().getInt("t.player.commands-costs." + cmds + ".value");

            commands.put(cmds, value);
        }

        return commands;
    }

    public int getMinPay() {
        return getConfig().getInt("t.player.min-pay");
    }

    public int getMaxPay() {
        return getConfig().getInt("t.player.max-pay");
    }

    public List<String> getItems() {
        List<String> itemNames = new ArrayList<>();
        for (String items : Objects.requireNonNull(getTokenshop().getConfigurationSection("gui.items")).getKeys(false))
            itemNames.add(items);

        return itemNames;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public FileConfiguration getTokenshop() {
        return tokenshop;
    }

    public FileConfiguration getTokenTop() {
        return tokentop;
    }
}
