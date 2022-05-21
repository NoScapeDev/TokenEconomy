package net.noscape.project.tokenseco;

import net.noscape.project.tokenseco.commands.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.listeners.*;
import net.noscape.project.tokenseco.utils.*;
import net.noscape.project.tokenseco.utils.api.*;
import net.noscape.project.tokenseco.utils.bstats.*;
import net.noscape.project.tokenseco.utils.implement.*;
import net.noscape.project.tokenseco.utils.menu.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;

public final class TokensEconomy extends JavaPlugin {

    private static TokensEconomy instance;
    private static MySQL mysql;
    private static H2Database h2;
    private final H2UserData h2user = new H2UserData();
    private static String connectionURL;
    private final UserData user = new UserData();
    public TokenAPI tokenAPI;
    private final TokenAPI api = new TokenAPI();
    private static final Logger log = Logger.getLogger("Minecraft");
    private static final HashMap<Player, MenuUtil> menuUtilMap = new HashMap<>();

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
        this.tokenAPI = new TokenAPI();

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

        // Small check to make sure that PlaceholderAPI is installed
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
        if (getConfig().getBoolean("t.plugin.token-toggle")) {
            Objects.requireNonNull(getCommand("ttoggle")).setExecutor(new TToggle());
        }
        if (getConfig().getBoolean("t.plugin.token-shop")) {
            Objects.requireNonNull(getCommand("tshop")).setExecutor(new TShop());
        }
        if (getConfig().getBoolean("t.plugin.token-stats")) {
            Objects.requireNonNull(getCommand("tstats")).setExecutor(new TStats());
        }


        if (isMySQL()) {
            mysql = new MySQL(host, port, database, username, password, options);
        }

        if (isH2()) {
            connectionURL = "jdbc:h2:" + getDataFolder().getAbsolutePath() + "/database";
            h2 = new H2Database(connectionURL);
        }

        config = new ConfigManager(getInstance().getConfig(), messageConfig, tokenShopConfig, tokenTopConfig);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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

}