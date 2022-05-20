package net.noscape.project.tokenseco.commands;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;

import java.io.*;

public class TAdmin implements CommandExecutor {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);
    private final ConfigManager config = TokensEconomy.getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (cmd.getName().equalsIgnoreCase("tadmin")) {
                // /tadmin give/set/remove <name> <amount>

                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {

                        try {
                            te.getConfig().load(te.getDataFolder() + "/config.yml");
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }

                        try {
                            TokensEconomy.messageConfig.load(TokensEconomy.messageFile);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                            Bukkit.getConsoleSender().sendMessage("Couldn't save message.yml properly!");
                        }

                        try {
                            TokensEconomy.tokenShopConfig.load(TokensEconomy.tokenShopFile);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                            Bukkit.getConsoleSender().sendMessage("Couldn't save tokenshop.yml properly!");
                        }

                        try {
                            TokensEconomy.tokenTopConfig.load(TokensEconomy.tokenTopFile);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                            Bukkit.getConsoleSender().sendMessage("Couldn't save tokentop.yml properly!");
                        }

                        sender.sendMessage(TokensEconomy.getConfigManager().getReload());
                        return true;
                    } else if (args[0].equalsIgnoreCase("version")) {
                        sender.sendMessage(Utils.applyFormat("&e&lADMIN &7You are using &ev" + te.getDescription().getVersion() + " &7of &e" + te.getDescription().getName()));
                        return true;
                    }
                }

                if (args.length == 3) {

                    String option = args[0];
                    OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[1]);
                    int amount1 = Integer.parseInt(args[2]);

                    if (option.equalsIgnoreCase("give")) {
                        if (te.isMySQL()) {
                            UserData.addTokens(receiver.getUniqueId(), amount1);
                        } else if (te.isH2()) {
                            H2UserData.addTokens(receiver.getUniqueId(), amount1);
                        }
                        sender.sendMessage(Utils.applyFormat("&7Given " + receiver.getName() + " &e" + amount1 + " &7Tokens."));
                        return true;
                    } else if (option.equalsIgnoreCase("set")) {
                        if (te.isMySQL()) {
                            UserData.setTokens(receiver.getUniqueId(), amount1);
                        } else if (te.isH2()) {
                            H2UserData.setTokens(receiver.getUniqueId(), amount1);
                        }
                        sender.sendMessage(Utils.applyFormat("&7" + receiver.getName() + "'s token balance has been set to &e" + amount1));
                        return true;
                    } else if (option.equalsIgnoreCase("remove")) {
                        if (te.isMySQL()) {
                            UserData.removeTokens(receiver.getUniqueId(), amount1);
                        } else if (te.isH2()) {
                            H2UserData.removeTokens(receiver.getUniqueId(), amount1);
                        }
                        sender.sendMessage(Utils.applyFormat("&7Removed &e" + amount1 + "&7 tokens from &e" + receiver.getName() + "&7."));
                        return true;
                    } else {
                        sender.sendMessage(Utils.applyFormat("&cUsage: &7/tadmin give/set/remove <player> <amount>"));
                        return true;
                    }
                }
            }
        } else {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("tadmin")) {
                if (player.hasPermission("te.admin")) {
                    // /tadmin give/set/remove <name> <amount>
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("reload")) {

                            try {
                                te.getConfig().load(te.getDataFolder() + "/config.yml");
                            } catch (IOException | InvalidConfigurationException e) {
                                e.printStackTrace();
                            }

                            try {
                                TokensEconomy.messageConfig.load(TokensEconomy.messageFile);
                            } catch (IOException | InvalidConfigurationException e) {
                                e.printStackTrace();
                                Bukkit.getConsoleSender().sendMessage("Couldn't save message.yml properly!");
                            }

                            try {
                                TokensEconomy.tokenShopConfig.load(TokensEconomy.tokenShopFile);
                            } catch (IOException | InvalidConfigurationException e) {
                                e.printStackTrace();
                                Bukkit.getConsoleSender().sendMessage("Couldn't save tokenshop.yml properly!");
                            }

                            try {
                                TokensEconomy.tokenTopConfig.load(TokensEconomy.tokenTopFile);
                            } catch (IOException | InvalidConfigurationException e) {
                                e.printStackTrace();
                                Bukkit.getConsoleSender().sendMessage("Couldn't save tokentop.yml properly!");
                            }

                            player.sendMessage(TokensEconomy.getConfigManager().getReload());
                            return true;
                        } else if (args[0].equalsIgnoreCase("version")) {
                            player.sendMessage(Utils.applyFormat("&e&lADMIN &7You are using &ev" + te.getDescription().getVersion() + " &7of &e" + te.getDescription().getName()));
                            return true;
                        }
                    }

                    if (args.length == 3) {

                        String option = args[0];
                        OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[1]);
                        int amount1 = Integer.parseInt(args[2]);

                        if (option.equalsIgnoreCase("give")) {
                            if (te.isMySQL()) {
                                UserData.addTokens(receiver.getUniqueId(), amount1);
                            } else if (te.isH2()) {
                                H2UserData.addTokens(receiver.getUniqueId(), amount1);
                            }

                            player.sendMessage(Utils.applyFormat("&7Given " + receiver.getName() + " &e" + amount1 + " &7Tokens."));
                            return true;
                        } else if (option.equalsIgnoreCase("set")) {
                            if (te.isMySQL()) {
                                UserData.setTokens(receiver.getUniqueId(), amount1);
                            } else if (te.isH2()) {
                                H2UserData.setTokens(receiver.getUniqueId(), amount1);
                            }
                            player.sendMessage(Utils.applyFormat("&7" + receiver.getName() + "'s token balance has been set to &e" + amount1));
                            return true;
                        } else if (option.equalsIgnoreCase("remove")) {
                            if (te.isMySQL()) {
                                UserData.removeTokens(receiver.getUniqueId(), amount1);
                            } else if (te.isH2()) {
                                H2UserData.removeTokens(receiver.getUniqueId(), amount1);
                            }
                            player.sendMessage(Utils.applyFormat("&7Removed &e" + amount1 + "&7 tokens from &e" + receiver.getName() + "&7."));
                            return true;
                        } else {
                            player.sendMessage(Utils.applyFormat("&cUsage: &7/tadmin give/set/remove <player> <amount>"));
                            return true;
                        }
                    } else {
                        player.sendMessage(Utils.applyFormat("&cTokenAdmin Commands:"));
                        player.sendMessage(Utils.applyFormat(""));
                        player.sendMessage(Utils.applyFormat("&c/tadmin &7[give/set/remove] <player> <amount> - player setting commands"));
                        player.sendMessage(Utils.applyFormat("&c/tadmin &7reload - reload all files."));
                        player.sendMessage(Utils.applyFormat("&c/tadmin &7version - check what version the plugin is on"));
                        player.sendMessage(Utils.applyFormat(""));
                        player.sendMessage(Utils.applyFormat("&c<> &7Required. &c[] &7Option."));
                        return true;
                    }
                } else {
                    player.sendMessage(Utils.applyFormat("&cPermission Required: &7te.admin"));
                }
            }
        }
        return false;
    }

}
