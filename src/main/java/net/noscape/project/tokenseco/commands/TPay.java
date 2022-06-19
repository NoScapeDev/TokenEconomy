package net.noscape.project.tokenseco.commands;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class TPay implements CommandExecutor  {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("tpay")) {
                if (player.hasPermission("te.pay") || player.hasPermission("te.player")) {
                    // /tpay <name> <amount>
                    if (args.length == 2) {
                        Player receiver = Bukkit.getPlayer(args[0]);
                        int amount1 = Integer.parseInt(args[1]);
                        double amount2 = Double.parseDouble(args[1]);

                        if (receiver != null) {
                            if (te.isMySQL()) {
                                if (!UserData.getIgnore(receiver.getUniqueId())) {
                                    if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(receiver)) {
                                        if (receiver != player) {
                                            TokenManager ptokens = TokensEconomy.getTokenManager(player);
                                            TokenManager rtokens = TokensEconomy.getTokenManager(receiver);
                                            if (amount1 >= TokensEconomy.getConfigManager().getMinPay() || amount2 >= TokensEconomy.getConfigManager().getMinPay()) {
                                                if (amount1 <= TokensEconomy.getConfigManager().getMaxPay() || amount2 <= TokensEconomy.getConfigManager().getMaxPay()) {
                                                    if (ptokens.getTokens() >= amount1) {
                                                        if (isInt(args[1])) {
                                                            rtokens.addTokens(amount1);
                                                            ptokens.removeTokens(amount1);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount1 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount1 + " &7Tokens from &e" + player.getName()));
                                                        } else if (isDouble(args[1])) {
                                                            rtokens.addTokens((int) amount2);
                                                            ptokens.removeTokens((int) amount2);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount2 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount2 + " &7Tokens from &e" + player.getName()));
                                                        }
                                                    } else if (ptokens.getTokens() >= amount2) {
                                                        if (isInt(args[1])) {
                                                            // UserData.addTokens(receiver.getUniqueId(), amount1);
                                                            rtokens.addTokens(amount1);
                                                            ptokens.removeTokens(amount1);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount1 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount1 + " &7Tokens from &e" + player.getName()));
                                                        } else if (isDouble(args[1])) {
                                                            rtokens.addTokens((int) amount2);
                                                            ptokens.removeTokens((int) amount2);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount2 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount2 + " &7Tokens from &e" + player.getName()));
                                                        }
                                                    } else {
                                                        player.sendMessage(ChatColor.RED + "You have enough tokens!");
                                                    }
                                                } else {
                                                    int value = TokensEconomy.getConfigManager().getMaxPay();
                                                    player.sendMessage(Utils.applyFormat("&7Maximum pay is &c" + value + " &7tokens."));
                                                }
                                            } else {
                                                int value = TokensEconomy.getConfigManager().getMinPay();
                                                player.sendMessage(Utils.applyFormat("&7Minimum pay is &c" + value + " &7tokens."));
                                            }
                                        } else {
                                            player.sendMessage(ChatColor.RED + "You cannot send tokens to yourself!");
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + "This player has reached the max balance.");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "You cannot send payments to this player!");
                                }
                            } else if (te.isH2()) {
                                if (!H2UserData.getIgnore(receiver.getUniqueId())) {
                                    if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(receiver)) {
                                        if (receiver != player) {
                                            TokenManager ptokens = TokensEconomy.getTokenManager(player);
                                            TokenManager rtokens = TokensEconomy.getTokenManager(receiver);
                                            if (amount1 >= TokensEconomy.getConfigManager().getMinPay() || amount2 >= TokensEconomy.getConfigManager().getMinPay()) {
                                                if (amount1 <= TokensEconomy.getConfigManager().getMaxPay() || amount2 <= TokensEconomy.getConfigManager().getMaxPay()) {
                                                    if (ptokens.getTokens() >= amount1) {
                                                        if (isInt(args[1])) {
                                                            UserData.addTokens(receiver.getUniqueId(), amount1);
                                                            UserData.removeTokens(player.getUniqueId(), amount1);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount1 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount1 + " &7Tokens from &e" + player.getName()));
                                                        } else if (isDouble(args[1])) {
                                                            UserData.addTokens(receiver.getUniqueId(), amount2);
                                                            UserData.removeTokens(player.getUniqueId(), amount2);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount2 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount2 + " &7Tokens from &e" + player.getName()));
                                                        }
                                                    } else if (ptokens.getTokens() >= amount2) {
                                                        if (isInt(args[1])) {
                                                            // UserData.addTokens(receiver.getUniqueId(), amount1);
                                                            rtokens.addTokens(amount1);
                                                            ptokens.removeTokens(amount1);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount1 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount1 + " &7Tokens from &e" + player.getName()));
                                                        } else if (isDouble(args[1])) {
                                                            rtokens.addTokens((int) amount2);
                                                            ptokens.removeTokens((int) amount2);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount2 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount2 + " &7Tokens from &e" + player.getName()));
                                                        }
                                                    } else {
                                                        player.sendMessage(ChatColor.RED + "You have enough tokens!");
                                                    }
                                                } else {
                                                    int value = TokensEconomy.getConfigManager().getMaxPay();
                                                    player.sendMessage(Utils.applyFormat("&7Maximum pay is &c" + value + " &7tokens."));
                                                }
                                            } else {
                                                int value = TokensEconomy.getConfigManager().getMinPay();
                                                player.sendMessage(Utils.applyFormat("&7Minimum pay is &c" + value + " &7tokens."));
                                            }
                                        } else {
                                            player.sendMessage(ChatColor.RED + "You cannot send tokens to yourself!");
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + "This player has reached the max balance.");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "You cannot send payments to this player!");
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "This player is not online.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /tpay <player> <amount>");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Permission Required: " + ChatColor.GRAY + " te.pay or te.player");
                }
            } else if (cmd.getName().equalsIgnoreCase("pay")) {
                if (player.hasPermission("te.pay") || player.hasPermission("te.player")) {
                    // /tpay <name> <amount>
                    if (args.length == 2) {
                        Player receiver = Bukkit.getPlayer(args[0]);
                        int amount1 = Integer.parseInt(args[1]);
                        double amount2 = Double.parseDouble(args[1]);

                        if (receiver != null) {
                            if (te.isMySQL()) {
                                if (!UserData.getIgnore(receiver.getUniqueId())) {
                                    if (!TokensEconomy.getConfigManager().hasMaxBalanceSQL(receiver)) {
                                        if (receiver != player) {
                                            TokenManager ptokens = TokensEconomy.getTokenManager(player);
                                            TokenManager rtokens = TokensEconomy.getTokenManager(receiver);
                                            if (amount1 >= TokensEconomy.getConfigManager().getMinPay() || amount2 >= TokensEconomy.getConfigManager().getMinPay()) {
                                                if (amount1 <= TokensEconomy.getConfigManager().getMaxPay() || amount2 <= TokensEconomy.getConfigManager().getMaxPay()) {
                                                    if (ptokens.getTokens() >= amount1) {
                                                        if (isInt(args[1])) {
                                                            rtokens.addTokens(amount1);
                                                            ptokens.removeTokens(amount1);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount1 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount1 + " &7Tokens from &e" + player.getName()));
                                                        } else if (isDouble(args[1])) {
                                                            rtokens.addTokens((int) amount2);
                                                            ptokens.removeTokens((int) amount2);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount2 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount2 + " &7Tokens from &e" + player.getName()));
                                                        }
                                                    } else if (ptokens.getTokens() >= amount2) {
                                                        if (isInt(args[1])) {
                                                            // UserData.addTokens(receiver.getUniqueId(), amount1);
                                                            rtokens.addTokens(amount1);
                                                            ptokens.removeTokens(amount1);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount1 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount1 + " &7Tokens from &e" + player.getName()));
                                                        } else if (isDouble(args[1])) {
                                                            rtokens.addTokens((int) amount2);
                                                            ptokens.removeTokens((int) amount2);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount2 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount2 + " &7Tokens from &e" + player.getName()));
                                                        }
                                                    } else {
                                                        player.sendMessage(ChatColor.RED + "You have enough tokens!");
                                                    }
                                                } else {
                                                    int value = TokensEconomy.getConfigManager().getMaxPay();
                                                    player.sendMessage(Utils.applyFormat("&7Maximum pay is &c" + value + " &7tokens."));
                                                }
                                            } else {
                                                int value = TokensEconomy.getConfigManager().getMinPay();
                                                player.sendMessage(Utils.applyFormat("&7Minimum pay is &c" + value + " &7tokens."));
                                            }
                                        } else {
                                            player.sendMessage(ChatColor.RED + "You cannot send tokens to yourself!");
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + "This player has reached the max balance.");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "You cannot send payments to this player!");
                                }
                            } else if (te.isH2()) {
                                if (!H2UserData.getIgnore(receiver.getUniqueId())) {
                                    if (!TokensEconomy.getConfigManager().hasMaxBalanceH2(receiver)) {
                                        if (receiver != player) {
                                            TokenManager ptokens = TokensEconomy.getTokenManager(player);
                                            TokenManager rtokens = TokensEconomy.getTokenManager(receiver);
                                            if (amount1 >= TokensEconomy.getConfigManager().getMinPay() || amount2 >= TokensEconomy.getConfigManager().getMinPay()) {
                                                if (amount1 <= TokensEconomy.getConfigManager().getMaxPay() || amount2 <= TokensEconomy.getConfigManager().getMaxPay()) {
                                                    if (ptokens.getTokens() >= amount1) {
                                                        if (isInt(args[1])) {
                                                            UserData.addTokens(receiver.getUniqueId(), amount1);
                                                            UserData.removeTokens(player.getUniqueId(), amount1);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount1 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount1 + " &7Tokens from &e" + player.getName()));
                                                        } else if (isDouble(args[1])) {
                                                            UserData.addTokens(receiver.getUniqueId(), amount2);
                                                            UserData.removeTokens(player.getUniqueId(), amount2);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount2 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount2 + " &7Tokens from &e" + player.getName()));
                                                        }
                                                    } else if (ptokens.getTokens() >= amount2) {
                                                        if (isInt(args[1])) {
                                                            // UserData.addTokens(receiver.getUniqueId(), amount1);
                                                            rtokens.addTokens(amount1);
                                                            ptokens.removeTokens(amount1);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount1 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount1 + " &7Tokens from &e" + player.getName()));
                                                        } else if (isDouble(args[1])) {
                                                            rtokens.addTokens((int) amount2);
                                                            ptokens.removeTokens((int) amount2);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount2 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount2 + " &7Tokens from &e" + player.getName()));
                                                        }
                                                    } else {
                                                        player.sendMessage(ChatColor.RED + "You have enough tokens!");
                                                    }
                                                } else {
                                                    int value = TokensEconomy.getConfigManager().getMaxPay();
                                                    player.sendMessage(Utils.applyFormat("&7Maximum pay is &c" + value + " &7tokens."));
                                                }
                                            } else {
                                                int value = TokensEconomy.getConfigManager().getMinPay();
                                                player.sendMessage(Utils.applyFormat("&7Minimum pay is &c" + value + " &7tokens."));
                                            }
                                        } else {
                                            player.sendMessage(ChatColor.RED + "You cannot send tokens to yourself!");
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + "This player has reached the max balance.");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "You cannot send payments to this player!");
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "This player is not online.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /tpay <player> <amount>");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Permission Required: " + ChatColor.GRAY + " te.pay or te.player");
                }
            }
        }
        return false;
    }

    public boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
