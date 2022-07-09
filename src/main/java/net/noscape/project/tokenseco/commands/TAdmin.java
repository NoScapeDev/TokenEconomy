package net.noscape.project.tokenseco.commands;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.managers.*;
import net.noscape.project.tokenseco.utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;

import java.io.*;
import java.util.*;

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
                        if (!receiver.isOnline()) {
                            if (te.isMySQL()) {
                                UserData.addTokens(receiver.getUniqueId(), amount1);
                            } else if (te.isH2()) {
                                H2UserData.addTokens(receiver.getUniqueId(), amount1);
                            }
                        } else {
                            TokenManager tokens = TokensEconomy.getTokenManager(receiver);
                            tokens.addTokens(amount1);
                        }

                        sender.sendMessage(Utils.applyFormat("&7Given " + receiver.getName() + " &e" + amount1 + " &7Tokens."));
                        return true;
                    } else if (option.equalsIgnoreCase("set")) {
                        if (!receiver.isOnline()) {
                            if (te.isMySQL()) {
                                UserData.setTokens(receiver.getUniqueId(), amount1);
                            } else if (te.isH2()) {
                                H2UserData.setTokens(receiver.getUniqueId(), amount1);
                            }
                        } else {
                            TokenManager tokens = TokensEconomy.getTokenManager(receiver);
                            tokens.setTokens(amount1);
                        }

                        sender.sendMessage(Utils.applyFormat("&7" + receiver.getName() + "'s token balance has been set to &e" + amount1));
                        return true;
                    } else if (option.equalsIgnoreCase("remove")) {
                        if (!receiver.isOnline()) {
                            if (te.isMySQL()) {
                                UserData.removeTokens(receiver.getUniqueId(), amount1);
                            } else if (te.isH2()) {
                                H2UserData.removeTokens(receiver.getUniqueId(), amount1);
                            }
                        } else {
                            TokenManager tokens = TokensEconomy.getTokenManager(receiver);
                            tokens.removeTokens(amount1);
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

                    if (args.length == 4) {

                        String option = args[1];
                        OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[2]);
                        int amount1 = Integer.parseInt(args[3]);

                        if (args[0].equalsIgnoreCase("tokens")) {
                            if (option.equalsIgnoreCase("give")) {
                                if (!receiver.isOnline()) {
                                    if (te.isMySQL()) {
                                        UserData.addTokens(receiver.getUniqueId(), amount1);
                                    } else if (te.isH2()) {
                                        H2UserData.addTokens(receiver.getUniqueId(), amount1);
                                    }
                                } else {
                                    TokenManager tokens = TokensEconomy.getTokenManager(receiver);
                                    tokens.addTokens(amount1);
                                }

                                player.sendMessage(Utils.applyFormat("&eTokens> &7Given " + receiver.getName() + " &e" + amount1 + " &7Tokens."));
                                return true;
                            } else if (option.equalsIgnoreCase("set")) {
                                if (!receiver.isOnline()) {
                                    if (te.isMySQL()) {
                                        UserData.setTokens(receiver.getUniqueId(), amount1);
                                    } else if (te.isH2()) {
                                        H2UserData.setTokens(receiver.getUniqueId(), amount1);
                                    }
                                } else {
                                    TokenManager tokens = TokensEconomy.getTokenManager(receiver);
                                    tokens.setTokens(amount1);
                                }

                                player.sendMessage(Utils.applyFormat("&eTokens> &7" + receiver.getName() + "'s token balance has been set to &e" + amount1));
                                return true;
                            } else if (option.equalsIgnoreCase("remove")) {
                                if (!receiver.isOnline()) {
                                    if (te.isMySQL()) {
                                        UserData.removeTokens(receiver.getUniqueId(), amount1);
                                    } else if (te.isH2()) {
                                        H2UserData.removeTokens(receiver.getUniqueId(), amount1);
                                    }
                                } else {
                                    TokenManager tokens = TokensEconomy.getTokenManager(receiver);
                                    tokens.removeTokens(amount1);
                                }
                                player.sendMessage(Utils.applyFormat("&eTokens> &7Removed &e" + amount1 + "&7 tokens from &e" + receiver.getName() + "&7."));
                                return true;
                            } else {
                                player.sendMessage(Utils.applyFormat("&cUsage: &7/tadmin tokens give/set/remove <player> <amount>"));
                                return true;
                            }
                        } else if (args[0].equalsIgnoreCase("bank")) {
                            if (option.equalsIgnoreCase("give")) {
                                if (!receiver.isOnline()) {
                                    if (te.isMySQL()) {
                                        UserData.addBank(receiver.getUniqueId(), amount1);
                                    } else if (te.isH2()) {
                                        H2UserData.addBank(receiver.getUniqueId(), amount1);
                                    }
                                } else {
                                    BankManager bank = TokensEconomy.getBankManager(receiver);
                                    bank.addBank(amount1);
                                }

                                player.sendMessage(Utils.applyFormat("&bBank> &7Given " + receiver.getName() + " &e" + amount1 + " &7Tokens."));
                                return true;
                            } else if (option.equalsIgnoreCase("set")) {
                                if (!receiver.isOnline()) {
                                    if (te.isMySQL()) {
                                        UserData.setBank(receiver.getUniqueId(), amount1);
                                    } else if (te.isH2()) {
                                        H2UserData.setBank(receiver.getUniqueId(), amount1);
                                    }
                                } else {
                                    BankManager bank = TokensEconomy.getBankManager(receiver);
                                    bank.setBank(amount1);
                                }

                                player.sendMessage(Utils.applyFormat("&bBank> &7" + receiver.getName() + "'s bank balance has been set to &e" + amount1));
                                return true;
                            } else if (option.equalsIgnoreCase("remove")) {
                                if (!receiver.isOnline()) {
                                    if (te.isMySQL()) {
                                        UserData.removeBank(receiver.getUniqueId(), amount1);
                                    } else if (te.isH2()) {
                                        H2UserData.removeBank(receiver.getUniqueId(), amount1);
                                    }
                                } else {
                                    BankManager bank = TokensEconomy.getBankManager(receiver);
                                    bank.removeBank(amount1);
                                }
                                player.sendMessage(Utils.applyFormat("&bBank> &7Removed &e" + amount1 + "&7 tokens from &e" + receiver.getName() + "&7."));
                                return true;
                            } else {
                                player.sendMessage(Utils.applyFormat("&cUsage: &7/tadmin bank give/set/remove <player> <amount>"));
                                return true;
                            }
                        }
                    } else {
                        for (String admin_help : TokensEconomy.getConfigManager().getMessages().getStringList("m.ADMIN-HELP")) {
                            player.sendMessage(Utils.applyFormat(admin_help).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()));
                        }
                        return true;
                    }
                } else {
                    player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                            TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                }
            }
        }
        return false;
    }

}
