package net.noscape.project.tokenseco.commands;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.managers.*;
import net.noscape.project.tokenseco.utils.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class TBank implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("bank")) {
                if (player.hasPermission("te.bank") || player.hasPermission("te.player")) {
                    if (args.length == 0) {
                        BankManager bank = TokensEconomy.getBankManager(player);

                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                TokensEconomy.getConfigManager().getMessages().getString("m.BANK-BALANCE")).replace("%tokens%",
                                String.valueOf(bank.getBank()).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()))));
                    } else if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("help")) {
                            for (String bank_help : TokensEconomy.getConfigManager().getMessages().getStringList("m.BANK-HELP")) {
                                player.sendMessage(Utils.applyFormat(bank_help).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()));
                            }
                        } else {
                            for (String bank_help : TokensEconomy.getConfigManager().getMessages().getStringList("m.BANK-HELP")) {
                                player.sendMessage(Utils.applyFormat(bank_help).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()));
                            }
                        }
                    } else if (args.length == 2) {
                        BankManager bank = TokensEconomy.getBankManager(player);
                        TokenManager tokens = TokensEconomy.getTokenManager(player);
                        // tbank withdraw <amount>
                        // tbank deposit <amount>
                        if (args[0].equalsIgnoreCase("withdraw")) {
                            int amount = Integer.parseInt(args[1]);
                            // remove from bank and add to token balance
                            // check if they have enough bank balance

                            if (!(amount > TokensEconomy.getConfigManager().getMaxWithdraw())) {
                                if (!(amount < TokensEconomy.getConfigManager().getMinWithdraw())) {
                                    if (bank.getBank() >= amount) {
                                        bank.removeBank(amount);
                                        tokens.addTokens(amount);

                                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                                TokensEconomy.getConfigManager().getMessages().getString("m.BANK-WITHDRAW")).replaceAll("%amount%",
                                                String.valueOf(amount)).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                                    } else {
                                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                                TokensEconomy.getConfigManager().getMessages().getString("m.NOT-ENOUGH-TO-WITHDRAW")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                                    }
                                } else {
                                    player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                            TokensEconomy.getConfigManager().getMessages().getString("m.MIN-WITHDRAW")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                                }
                            } else {
                                player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                        TokensEconomy.getConfigManager().getMessages().getString("m.MAX-WITHDRAW")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                            }
                        }

                        if (args[0].equalsIgnoreCase("deposit")) {
                            int amount = Integer.parseInt(args[1]);
                            // add into the bank and remove from token balance
                            // check if they have enough token balance

                            if (!(amount > TokensEconomy.getConfigManager().getMaxDeposit())) {
                                if (!(amount < TokensEconomy.getConfigManager().getMinDeposit())) {
                                    if (tokens.getTokens() >= amount) {
                                        tokens.removeTokens(amount);
                                        bank.addBank(amount);

                                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                                TokensEconomy.getConfigManager().getMessages().getString("m.BANK-DEPOSIT")).replaceAll("%amount%",
                                                String.valueOf(amount)).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                                    } else {
                                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                                TokensEconomy.getConfigManager().getMessages().getString("m.NOT-ENOUGH-TO-DEPOSIT")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                                    }
                                } else {
                                    player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                            TokensEconomy.getConfigManager().getMessages().getString("m.MIN-DEPOSIT")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                                }
                            } else {
                                player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                        TokensEconomy.getConfigManager().getMessages().getString("m.MAX-DEPOSIT")).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix())));
                            }
                        }
                    } else {
                        for (String bank_help : TokensEconomy.getConfigManager().getMessages().getStringList("m.BANK-HELP")) {
                            player.sendMessage(Utils.applyFormat(bank_help).replaceAll("%PREFIX%", TokensEconomy.getConfigManager().getPrefix()));
                        }
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

