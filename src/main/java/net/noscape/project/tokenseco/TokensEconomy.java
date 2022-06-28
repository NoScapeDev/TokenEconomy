package net.noscape.project.tokenseco;

import net.milkbowl.vault.economy.*;
import net.noscape.project.tokenseco.commands.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.listeners.*;
import net.noscape.project.tokenseco.managers.*;
import net.noscape.project.tokenseco.utils.api.*;
import net.noscape.project.tokenseco.utils.bstats.*;
import net.noscape.project.tokenseco.utils.implement.*;
import net.noscape.project.tokenseco.utils.menu.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class TokensEconomy extends JavaPlugin {

    private static TokensEconomy instance;
    private static MySQL mysql;
    private static H2Database h2;
    private final H2UserData h2user = new H2UserData();
    private TokenManager tm;
    private static String connectionURL;
    private final UserData user = new UserData();
    public static TokenAPI tokenAPI;
    private EconomyVault eco_vault;
    private static final HashMap<Player, MenuUtil> menuUtilMap = new HashMap<>();
    private static final HashMap<OfflinePlayer, TokenManager> tokenMap = new HashMap<>();
    private static final HashMap<OfflinePlayer, BankManager> bankMap = new HashMap<>();

    public static File messageFile;
    public static FileConfiguration messageConfig;

    public static File tokenShopFile;
    public static FileConfiguration tokenShopConfig;

    public static File tokenTopFile;
    public static FileConfiguration tokenTopConfig;

    public static File latestConfigFile;
    public static FileConfiguration latestConfigConfig;

    public static ConfigManager config;

    private final String host = getConfig().getString("t.data.address");
    private final int port = getConfig().getInt("t.data.port");
    private final String database = getConfig().getString("t.data.database");
    private final String username = getConfig().getString("t.data.username");
    private final String password = getConfig().getString("t.data.password");
    private final String options = getConfig().getString("t.data.options");

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        this.saveDefaultConfig();
        this.callMetrics();

        messageFile = new File(getDataFolder(), "messages.yml");
        if (!messageFile.exists())
            saveResource("messages.yml", false);
        messageConfig = new YamlConfiguration();
        try {
            messageConfig.load(messageFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }

        tokenShopFile = new File(getDataFolder(), "tokenshop.yml");
        if (!tokenShopFile.exists())
            saveResource("tokenshop.yml", false);
        tokenShopConfig = new YamlConfiguration();
        try {
            tokenShopConfig.load(tokenShopFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }

        tokenTopFile = new File(getDataFolder(), "tokentop.yml");
        if (!tokenTopFile.exists())
            saveResource("tokentop.yml", false);
        tokenTopConfig = new YamlConfiguration();
        try {
            tokenTopConfig.load(tokenTopFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (latestConfigFile != null) {
            if (deleteConfig()) {
                latestConfigFile = new File(getDataFolder(), "latest-config.yml");
                if (!latestConfigFile.exists())
                    saveResource("latest-config.yml", true);
                latestConfigConfig = new YamlConfiguration();
                try {
                    latestConfigConfig.load(latestConfigFile);
                } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
                    e.printStackTrace();
                }
            }
        } else {
            latestConfigFile = new File(getDataFolder(), "latest-config.yml");
            if (!latestConfigFile.exists())
                saveResource("latest-config.yml", true);
            latestConfigConfig = new YamlConfiguration();
            try {
                latestConfigConfig.load(latestConfigFile);
            } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        }

        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        Objects.requireNonNull(getCommand("tadmin")).setExecutor(new TAdmin());
        if (getConfig().getBoolean("t.plugin.token-pay")) {
            Objects.requireNonNull(getCommand("tpay")).setExecutor(new TPay());
        }

        Objects.requireNonNull(getCommand("tbalance")).setExecutor(new TBalance());
        Objects.requireNonNull(getCommand("tbaltop")).setExecutor(new TBalTop());

        if (getConfig().getBoolean("t.plugin.token-bank")) {
            Objects.requireNonNull(getCommand("tbank")).setExecutor(new TBank());
        }
        if (getConfig().getBoolean("t.plugin.token-toggle")) {
            Objects.requireNonNull(getCommand("ttoggle")).setExecutor(new TToggle());
        }
        if (getConfig().getBoolean("t.plugin.token-shop")) {
            Objects.requireNonNull(getCommand("tshop")).setExecutor(new TShop());
        }
        if (getConfig().getBoolean("t.plugin.token-stats")) {
            Objects.requireNonNull(getCommand("tstats")).setExecutor(new TStats());
        }

        if (getConfig().getBoolean("t.support.base-commands.shop")) {
            Objects.requireNonNull(getCommand("shop")).setExecutor(new TShop());
        }

        if (getConfig().getBoolean("t.support.base-commands.baltop")) {
            Objects.requireNonNull(getCommand("baltop")).setExecutor(new TBalTop());
        }

        if (getConfig().getBoolean("t.support.base-commands.pay")) {
            Objects.requireNonNull(getCommand("pay")).setExecutor(new TPay());
        }

        if (getConfig().getBoolean("t.support.base-commands.balance")) {
            Objects.requireNonNull(getCommand("balance")).setExecutor(new TBalance());
        }

        if (getConfig().getBoolean("t.support.base-commands.toggle")) {
            Objects.requireNonNull(getCommand("toggle")).setExecutor(new TToggle());
        }

        if (getConfig().getBoolean("t.support.base-commands.bank")) {
            Objects.requireNonNull(getCommand("bank")).setExecutor(new TBank());
        }

        if (isMySQL()) {
            mysql = new MySQL(host, port, database, username, password, options);
        }

        if (isH2()) {
            connectionURL = "jdbc:h2:" + getDataFolder().getAbsolutePath() + "/database";
            h2 = new H2Database(connectionURL);
        }

        if (getConfig().getBoolean("t.support.tokeneco-vault-dependency")) {
            if (!setupVault()) {
                Bukkit.getConsoleSender().sendMessage("Vault-Setup: vault support in config is enabled but 'Vault Plugin' seems to be missing...");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }

        config = new ConfigManager(getInstance().getConfig(), messageConfig, tokenShopConfig, tokenTopConfig);
        tokenAPI = new TokenAPI();
    }

    @Override
    public void onDisable() {
        // stop
    }

    public static TokensEconomy getInstance() {
        return instance;
    }

    public static ConfigManager getConfigManager() {
        return config;
    }

    public static UserData getUser() {
        return instance.user;
    }

    public static MySQL getMysql() {
        return mysql;
    }

    public static H2Database getH2Database() {
        return h2;
    }

    public static MenuUtil getMenuUtil(Player player) {
        MenuUtil menuUtil;

        if (menuUtilMap.containsKey(player)) {
            return menuUtilMap.get(player);
        } else {
            menuUtil = new MenuUtil(player);
            menuUtilMap.put(player, menuUtil);
        }

        return menuUtil;
    }

    public static BankManager getBankManager(OfflinePlayer player) {
        BankManager bank = null;

        if (TokensEconomy.instance.isMySQL()) {
            if (bankMap.containsKey(player)) {
                return bankMap.get(player);
            } else {
                bank = new BankManager(player, UserData.getBankInt(player.getUniqueId()));
                bankMap.put(player, bank);
            }
        } else if (TokensEconomy.instance.isH2()) {
            if (bankMap.containsKey(player)) {
                return bankMap.get(player);
            } else {
                bank = new BankManager(player, H2UserData.getBankInt(player.getUniqueId()));
                bankMap.put(player, bank);
            }
        }

        return bank;
    }

    public static TokenManager getTokenManager(OfflinePlayer player) {
        TokenManager token = null;

        if (TokensEconomy.instance.isMySQL()) {
            if (tokenMap.containsKey(player)) {
                return tokenMap.get(player);
            } else {
                token = new TokenManager(player, UserData.getTokensInt(player.getUniqueId()));
                tokenMap.put(player, token);
            }
        } else if (TokensEconomy.instance.isH2()) {
            if (tokenMap.containsKey(player)) {
                return tokenMap.get(player);
            } else {
                token = new TokenManager(player, H2UserData.getTokensInt(player.getUniqueId()));
                tokenMap.put(player, token);
            }
        }

        return token;
    }

    private boolean deleteConfig() {
        latestConfigFile = new File(getDataFolder(), "latest-config.yml");
        Path path = latestConfigFile.toPath();
        try {
            Files.delete(path);
            return true;
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", path);
            return false;
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", path);
            return false;
        } catch (IOException x) {
            System.err.println(x);
            return false;
        }
    }

    public static String getConnectionURL() {
        return connectionURL;
    }

    public static H2UserData getH2user() {
        return instance.h2user;
    }

    public Boolean isH2() {
        return Objects.requireNonNull(getConfig().getString("t.data.type")).equalsIgnoreCase("H2");
    }

    public Boolean isMySQL() {
        return Objects.requireNonNull(getConfig().getString("t.data.type")).equalsIgnoreCase("MYSQL");
    }

    public static HashMap<OfflinePlayer, TokenManager> getTokenMap() {
        return tokenMap;
    }

    public static HashMap<OfflinePlayer, BankManager> getBankMap() {
        return bankMap;
    }

    private void callMetrics() {
        int pluginId = 15240;
        Metrics metrics = new Metrics(this, pluginId);

        metrics.addCustomChart(new Metrics.SimplePie("used_language", () -> getConfig().getString("language", "en")));

        metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            String javaVersion = System.getProperty("java.version");
            Map<String, Integer> entry = new HashMap<>();
            entry.put(javaVersion, 1);
            if (javaVersion.startsWith("1.7")) {
                map.put("Java 1.7", entry);
            } else if (javaVersion.startsWith("1.8")) {
                map.put("Java 1.8", entry);
            } else if (javaVersion.startsWith("1.9")) {
                map.put("Java 1.9", entry);
            } else {
                map.put("Other", entry);
            }
            return map;
        }));
    }

    private boolean setupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        getServer().getServicesManager().register(Economy.class, eco_vault, this, ServicePriority.Highest);
        Bukkit.getConsoleSender().sendMessage("Vault-Setup: has been registered now.");
        return true;
    }

}