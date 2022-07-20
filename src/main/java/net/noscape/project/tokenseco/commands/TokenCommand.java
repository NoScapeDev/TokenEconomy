package net.noscape.project.tokenseco.commands;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.managers.*;
import net.noscape.project.tokenseco.utils.*;
import net.noscape.project.tokenseco.utils.menu.menus.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

import static net.noscape.project.tokenseco.utils.Utils.msgPlayer;

public class TokenCommand implements CommandExecutor {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);
    private final ConfigManager config = TokensEconomy.getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {

            Player player = (Player) sender;

            TokenManager tokens = TokensEconomy.getTokenManager(player);

            if (cmd.getName().equalsIgnoreCase("tokens")) {
                if (args.length == 0) {
                    if (player.hasPermission("te.mainmenu")) {
                        new TokenMenu(TokensEconomy.getMenuUtil(player)).open();
                    } else {
                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                    }
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("help")) {
                        msgPlayer(player, "&e&nToken Commands&r",
                                "",
                                "&6/tokens pay <user> <amount> &7- pay a user amount of tokens.",
                                "&6/tokens balance &7- get amount of tokens you have.",
                                "&6/tokens bank &7- get amount of tokens in your bank.",
                                "&6/tokens exchange &7- opens the exchange gui",
                                "&6/tokens top &7- opens the top players gui",
                                "&6/tokens stats &7- gets the server tokens stats",
                                "&6/tokens toggle &7- toggles token request for yourself.",
                                "&6/tokens help &7- shows this help guide.",
                                "",
                                "&b&nBank Commands&r",
                                "",
                                "&3/bank &7- returns the bank balance.",
                                "&3/bank withdraw <amount> &7- withdraws the amount from your bank.",
                                "&3/bank deposit <amount> &7- deposits the amount in your bank.",
                                "");
                    } else if (args[0].equalsIgnoreCase("top")) {
                        if (player.hasPermission("te.baltop") || player.hasPermission("te.player")) {
                            new TopMenu(TokensEconomy.getMenuUtil(player)).open();
                        } else {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                        }
                    } else if (args[0].equalsIgnoreCase("toggle")) {
                        if (player.hasPermission("te.toggle")) {
                            if (UserData.getIgnore(player.getUniqueId())) {
                                UserData.setIgnore(player.getUniqueId(), false);
                                player.sendMessage(Utils.applyFormat("&e&lTOKENS &7Players will now be able to send you tokens!"));
                            } else {
                                UserData.setIgnore(player.getUniqueId(), true);
                                player.sendMessage(Utils.applyFormat("&e&lTOKENS &7Players will no longer be able to send you tokens!"));
                            }
                        } else {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                        }
                    } else if (args[0].equalsIgnoreCase("bank")) {
                        if (player.hasPermission("te.bank") || player.hasPermission("te.player")) {
                            BankManager bank = TokensEconomy.getBankManager(player);

                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    TokensEconomy.getConfigManager().getMessages().getString("m.BANK-BALANCE")).replaceAll("%tokens%",
                                    String.valueOf(bank.getBank()).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()))));
                        } else {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                        }
                    } else if (args[0].equalsIgnoreCase("balance")) {
                        if (player.hasPermission("te.balance") || player.hasPermission("te.player")) {

                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    TokensEconomy.getConfigManager().getMessages().getString("m.BALANCE")).replaceAll("%tokens%",
                                    String.valueOf(tokens.getTokens()).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()))));
                        } else {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                        }
                    } else if (args[0].equalsIgnoreCase("exchange")) {
                        if (player.hasPermission("te.exchange") || player.hasPermission("te.player")) {
                            new ExchangeMenu(TokensEconomy.getMenuUtil(player)).open();
                        } else {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                        }
                    } else if (args[0].equalsIgnoreCase("stats")) {
                        if (player.hasPermission("te.stats") || player.hasPermission("te.player")) {
                            player.sendMessage(Utils.applyFormat("&e&lTOKEN STATS &8&l>"));
                            player.sendMessage(Utils.applyFormat("&7Server Total: &e" + UserData.getServerTotalTokens()));
                            player.sendMessage(Utils.applyFormat("&7Your Balance: &e" + UserData.getTokensInt(player.getUniqueId())));
                        } else {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                        }
                    } else {
                        player.sendMessage(Utils.applyFormat("&c[&l!&c] &7Invalid Command! Use &c/tokens help"));
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("pay")) {
                        if (player.hasPermission("te.pay") || player.hasPermission("te.player")) {
                            Player receiver = Bukkit.getPlayer(args[1]);
                            int amount1 = Integer.parseInt(args[2]);
                            double amount2 = Double.parseDouble(args[2]);
                            if (receiver != null) {
                                if (!UserData.getIgnore(receiver.getUniqueId())) {
                                    TokenManager ptokens = TokensEconomy.getTokenManager(player);
                                    TokenManager rtokens = TokensEconomy.getTokenManager(receiver);
                                    if (!(rtokens.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                                        if (receiver != player) {
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
                                            player.sendMessage(Utils.applyFormat("&c[&l!&c] &7You cannot send tokens to yourself!"));
                                        }
                                    } else {
                                        player.sendMessage(Utils.applyFormat("&c[&l!&c] &7This player has reached the max balance!"));
                                    }
                                } else {
                                    player.sendMessage(Utils.applyFormat("&c[&l!&c] &7You cannot send payments to this player!"));
                                }
                            } else {
                                player.sendMessage(Utils.applyFormat("&c[&l!&c] &7This user is not online!"));
                            }
                        } else {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    TokensEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                        }
                    } else {
                        player.sendMessage(Utils.applyFormat("&c[&l!&c] &7Invalid Command! Use &c/tokens help"));
                    }
                } else {
                    player.sendMessage(Utils.applyFormat("&c[&l!&c] &7Invalid format! Use &c/tokens help"));
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